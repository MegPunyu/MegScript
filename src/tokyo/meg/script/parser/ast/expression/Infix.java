package tokyo.meg.script.parser.ast.expression;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.formula.*;

public final class Infix extends Expression {

  public final String operator;
  public final Formula left;
  public final Formula right;

  public Infix(String operator, Formula left, Formula right) {
    this.operator = operator;
    this.left = left;
    this.right = right;
  }

  public NodeType getType() {
    return NodeType.INFIX;
  }
}
