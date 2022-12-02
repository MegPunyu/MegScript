package tokyo.meg.script.lexer;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import tokyo.meg.script.lexer.chars.*;
import tokyo.meg.script.lexer.token.*;

/**
 * Lexer class for MegScript.
 */
public final class Lexer {

  private final BufferedReader program;

  private char current = '\0'; // current character
  private char next = '\0'; // next character

  /**
   * Constructor.
   * 
   */
  public Lexer(Path sourcePath) {
    try {
      final var fis = new FileInputStream(sourcePath.toString());
      this.program = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

    } catch (FileNotFoundException e) {
      throw new UncheckedIOException("Cannot find file " + sourcePath.toString(), e);

    }

    this.move().move().eat();
  }

  /**
   * Reads and returns a token for each call.
   */
  public Token read() {
    return switch (this.current) {
      case '\0' -> this.readToken(TokenType.NULL);
      case '\'' -> this.readToken(TokenType.APOS);
      case '\\' -> this.readBsol();
      case ':' -> this.readToken(TokenType.COLON);
      case ',' -> this.readToken(TokenType.COMMA);
      case '@' -> this.readToken(TokenType.COMMAT);
      case '$' -> this.readToken(TokenType.DOLLAR);
      case '{' -> this.readToken(TokenType.LBRACE);
      case '[' -> this.readToken(TokenType.LBRACK);
      case '(' -> this.readToken(TokenType.LPAR);
      case '#' -> this.readToken(TokenType.NUM);
      case '.' -> this.readToken(TokenType.PERIOD);
      case '?' -> this.readToken(TokenType.QUEST);
      case '}' -> this.readToken(TokenType.RBRACE);
      case ']' -> this.readToken(TokenType.RBRACK);
      case ')' -> this.readToken(TokenType.RPAR);
      case ';' -> this.readToken(TokenType.SEMI);
      case '"' -> this.readStrLiteral();
      default -> this.readVariableLengthToken();
    };
  }

  /**
   * Returns a token of the specified type, whose value is the character at the
   * current position.
   * 
   * @param type the token type
   */
  private Token readToken(TokenType type) {
    final var token = new Token(type, String.valueOf(this.current));

    this.move().eat();

    return token;
  }

  /**
   * Reads and returns the backslash (\) token.
   * If a comment is found, skips it and returns the next token.
   */
  private Token readBsol() {
    this.move().eat();

    if (this.current == '\\') {
      return this.skipSingleLineComments();

    } else if (Chars.isOperator(this.current)) {
      return this.skipMultiLineComments(this.current);

    } else {
      return new Token(TokenType.BSOL, "\\");
    }
  }

  /**
   * Parses a Str literal and returns it as Token.
   */
  private Token readStrLiteral() {
    final var sb = new StringBuilder();

    this.move();

    while (this.current != '"' && this.current != '\0') {

      // detect escape characters
      if (this.current == '\\') {
        this.move();

        sb.append(switch (this.current) {
          case '\\' -> '\\';
          case '"' -> '"';
          case 'b' -> '\b';
          case 'f' -> '\f';
          case 'n' -> '\n';
          case 'r' -> '\r';
          case 's' -> '\s';
          case 't' -> '\t';
          case 'u' -> this.unescape();
          default -> this.current;
        });

      } else {
        sb.append(this.current);
      }

      this.move();
    }

    this.move().eat();

    return new Token(TokenType.STR_LITERAL, sb.toString());
  }

  private char unescape() {
    final var sb = new StringBuilder();

    for (int i = 0; i < 4; ++i) {
      this.move();
      sb.append(this.current);
    }

    return (char) Integer.parseInt(sb.toString(), 16);
  }

  /**
   * Reads and returns variable length tokens such as operators, numbers,
   * identifiers.
   */
  private Token readVariableLengthToken() {
    if (Chars.isOperator(this.current)) {
      return this.readOperator();

    } else if (Character.isDigit(current)) {
      return this.readNumeric();

    } else {
      return this.readIdentifier();
    }
  }

  /**
   * Skips single line comments (starting with "\\").
   */
  private Token skipSingleLineComments() {
    while (this.current != '\n' && this.current != '\0') {
      this.move();
    }

    this.eat();

    return this.read();
  }

  /**
   * Skips multi line comments (starting with "\" and an operator).
   * 
   * @param end stops skipping when this character is encountered
   */
  private Token skipMultiLineComments(char end) {
    this.move();

    while (this.current != '\0' && !(this.current == end && this.next == '\\')) {
      this.move();
    }

    this.move().move().eat();

    return this.read();
  }

  /**
   * Read and returns an operator.
   */
  private Token readOperator() {
    final var sb = new StringBuilder();

    while (Chars.isOperator(this.current)) {
      sb.append(this.current);
      this.move();
    }

    this.eat();

    return new Token(TokenType.OPERATOR, sb.toString());
  }

  /**
   * Read and returns a numeric token (INT_LITERAL or REAL_LITERAL).
   */
  private Token readNumeric() {
    final var sb = new StringBuilder();

    sb.append(this.getNumericSequence());

    final int len = sb.length();

    sb.append(this.getCharacterSequence()); // read suffix characters

    this.eat();

    // when no suffix is found
    if (sb.length() > len) {
      return new Token(TokenType.INT_LITERAL, sb.toString());
    }

    // when a decimal point is found
    if (this.current == '.') {
      sb.append('.');

      this.move().eat();

      sb.append(this.getNumericSequence()).append(this.getCharacterSequence());

      this.eat();

      return new Token(TokenType.REAL_LITERAL, sb.toString());

    } else {
      return new Token(TokenType.INT_LITERAL, sb.toString());
    }
  }

  /**
   * Reads and returns an identifier.
   */
  private Token readIdentifier() {
    final var token = new Token(TokenType.IDENTIFIER, this.getCharacterSequence());

    this.eat();

    return token;
  }

  /**
   * Reads character sequence (strings that do not contain operators or reserved
   * symbols).
   */
  private String getCharacterSequence() {
    final var sb = new StringBuilder();

    while (!Character.isWhitespace(this.current)
        && !Chars.isReserved(this.current)
        && !Chars.isOperator(this.current)) {
      sb.append(this.current);
      this.move();
    }

    return sb.toString();
  }

  /**
   * Reads numeric character sequence (0-9).
   */
  private String getNumericSequence() {
    final var sb = new StringBuilder();

    while (Character.isDigit(this.current)) {
      sb.append(this.current);
      this.move();
    }

    return sb.toString();
  }

  /**
   * Skips whitespace characters.
   */
  private Lexer eat() {
    while (Character.isWhitespace(this.current)) {
      this.move();
    }

    return this;
  }

  /**
   * Increments the current position.
   */
  private Lexer move() {
    this.current = this.next;

    int c = 0;

    try {
      c = this.program.read();

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    this.next = c == -1 ? 0 : (char) c;

    return this;
  }
}
