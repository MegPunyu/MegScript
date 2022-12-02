package tokyo.meg.script.parser;

import java.nio.file.*;
import java.util.*;
import java.util.function.*;

import tokyo.meg.script.lexer.*;
import tokyo.meg.script.lexer.token.*;
import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.expression.*;
import tokyo.meg.script.parser.ast.factor.*;
import tokyo.meg.script.parser.ast.formula.*;
import tokyo.meg.script.parser.ast.procedural.*;
import tokyo.meg.script.parser.ast.sequential.*;
import tokyo.meg.script.parser.ast.term.*;

/**
 * Parser class for MegScript.
 */
public final class Parser {

  private final Lexer lex;

  private Token current; // token currently being read
  private Token next; // next token

  /**
   * Constructor.
   * 
   */
  public Parser(Path sourcePath) {
    this.lex = new Lexer(sourcePath);

    this.advance().advance();
  }

  /**
   * Creates an AST.
   */
  public Brace parse() {
    return new Brace(this.parseProcedural());
  }

  private Procedural parseProcedural() {
    final var sequentials = new ArrayDeque<Sequential>();

    boolean returnsSemi = false; // true if it returns a Semi
    Sequential sequential = null;

    while (true) {
      sequential = this.parseSequential();

      if (this.current.type == TokenType.SEMI) {
        returnsSemi = true;

        this.advance();

        sequentials.add(sequential);

      } else {
        if (returnsSemi) {
          sequentials.add(sequential);
        }

        break;
      }
    }

    return returnsSemi ? new Semi(sequentials) : sequential;
  }

  private Procedural parseEnclosedProcedural(TokenType end) {
    this.advance();

    final Procedural procedure = this.parseProcedural();

    this.find(end);

    return procedure;
  }

  private Sequential parseSequential() {
    final var formulaList = new ArrayDeque<Formula>();

    boolean returnsComma = false; // true if it returns a Comma
    Formula formula = null;

    while (true) {
      formula = this.parseFormula();

      if (this.current.type == TokenType.COMMA) {
        returnsComma = true;

        this.advance();

        if (this.isNotImplicitEmpty(formula)) {
          formulaList.add(formula);
        }

        while (this.current.type == TokenType.COMMA) {
          formulaList.add(new Empty(true));

          this.advance();
        }
      } else {
        if (returnsComma && this.isNotImplicitEmpty(formula)) {
          formulaList.add(formula);
        }

        break;
      }
    }

    return returnsComma ? new Comma(formulaList) : formula;
  }

  private Formula parseFormula() {
    return switch (this.current.type) {
      case COLON -> this.parseColon();
      case COMMAT -> this.parseCommat();
      case DOLLAR -> this.parseDollar();
      case NUM -> this.parseNum();
      case QUEST -> this.parseQuest();
      default -> this.parseExpression();
    };
  }

  private Formula parseColon() {
    this.advance();

    final String arg = this.find(TokenType.IDENTIFIER, TokenType.OPERATOR).value;
    final Formula formula = this.parseFormula();

    return new Colon(arg, formula);
  }

  private Formula parseCommat() {
    this.advance();

    final String attribute = this.find(TokenType.OPERATOR, TokenType.IDENTIFIER).value;
    final Formula target = this.parseFormula();
    final Formula formula = this.parseFormula();

    return new Commat(attribute, target, formula);
  }

  private Formula parseDollar() {
    this.advance();

    final String symbol = this.find(TokenType.IDENTIFIER, TokenType.OPERATOR).value;
    final Formula formula = this.parseFormula();

    return new Dollar(symbol, formula);
  }

  private Formula parseNum() {
    this.advance();

    final Formula parent = this.parseFormula();
    final Formula child = this.parseFormula();

    return new Num(parent, child);
  }

  private Formula parseQuest() {
    this.advance();

    final Formula condition = this.parseFormula();
    final Formula consequent = this.parseFormula();
    final Formula alternative = this.parseFormula();

    return new Quest(condition, consequent, alternative);
  }

  private Expression parseExpression() {
    Expression expression = this.parseTerm();

    if (this.current.type != TokenType.OPERATOR) {
      return expression;
    }

    do {
      final String operator = this.current.value;

      this.advance();

      final Formula right = switch (this.current.type) {
        case COLON, COMMAT, DOLLAR, NUM, QUEST -> this.parseFormula();
        default -> this.parseTerm();
      };

      expression = new Infix(operator, expression, right);

    } while (this.current.type == TokenType.OPERATOR);

    return expression;
  }

  private Term parseTerm() {
    Term term = this.parseFactor();

    while (true) {
      switch (this.current.type) {
        case APOS: {
          this.advance();
          term = new Apos(term, this.find(TokenType.IDENTIFIER, TokenType.OPERATOR).value);
          break;
        }
        case PERIOD: {
          this.advance();
          term = new Period(term, this.find(TokenType.IDENTIFIER, TokenType.OPERATOR).value);
          break;
        }
        case LBRACK: {
          term = new Brack(term, this.parseEnclosedProcedural(TokenType.RBRACK));
          break;
        }
        default: {
          return term;
        }
      }
    }
  };

  private Factor parseFactor() {
    return switch (this.current.type) {
      case BSOL -> this.parseBsol();
      case IDENTIFIER -> this.parseLiteral(Variable::new);
      case INT_LITERAL -> this.parseLiteral(Int::new);
      case LBRACE -> new Brace(this.parseEnclosedProcedural(TokenType.RBRACE));
      case LPAR -> new Par(this.parseEnclosedProcedural(TokenType.RPAR));
      case OPERATOR -> this.parseOperator();
      case REAL_LITERAL -> this.parseLiteral(Real::new);
      case STR_LITERAL -> this.parseLiteral(Str::new);
      default -> new Empty(true);
    };
  }

  private Factor parseOperator() {
    return switch (this.next.type) {
      case APOS:
      case COMMA:
      case NULL:
      case OPERATOR:
      case PERIOD:
      case RBRACE:
      case RBRACK:
      case RPAR:
      case SEMI: {
        yield this.parseLiteral(Variable::new);
      }
      default: {
        yield new Empty(true);
      }
    };
  }

  private Factor parseBsol() {
    this.advance();

    final String separator = FileSystems.getDefault().getSeparator();
    final var sb = new StringBuilder();
    final boolean isRelative = this.current.type == TokenType.PERIOD;

    if (isRelative) {
      sb.append(".");
    }

    while (true) {
      if (this.current.type == TokenType.PERIOD) {
        if (this.next.type != TokenType.PERIOD) {
          sb.append(separator);
        }

        this.advance();

        while (this.current.type == TokenType.PERIOD) {
          sb.append(separator);
          sb.append("..");

          this.advance();

          if (this.current.type != TokenType.PERIOD) {
            sb.append(separator);
          }
        }
      } else if (this.current.type == TokenType.IDENTIFIER) {
        sb.append(this.current.value);

        this.advance();

        if (this.current.type == TokenType.IDENTIFIER) {
          break;
        }
      } else {
        break;
      }
    }

    return new Bsol(sb.toString(), isRelative);
  }

  private Factor parseLiteral(Function<String, Factor> constructor) {
    final Factor factor = constructor.apply(this.current.value);

    this.advance();

    return factor;
  }

  private boolean isNotImplicitEmpty(Node node) {
    return node.getType() != NodeType.EMPTY || !((Empty) node).implicit;
  }

  private Token find(TokenType... types) {
    Token token = this.current;

    for (final TokenType type : types) {
      if (type == token.type) {
        this.advance();
        return token;
      }
    }

    return new Token(TokenType.NULL);
  }

  /**
   * Reads the next token.
   */
  private Parser advance() {
    this.current = this.next;
    this.next = this.lex.read();

    return this;
  }
}
