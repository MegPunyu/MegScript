package tokyo.meg.script.lexer.token;

public final class Token {

  public final TokenType type;
  public final String value;

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  public Token(TokenType type) {
    this(type, null);
  }
}
