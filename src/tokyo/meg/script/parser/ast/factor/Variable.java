package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Variable extends Factor {

  public final String name;

  public Variable(String name) {
    this.name = name;
  }

  public NodeType getType() {
    return NodeType.VARIABLE;
  }

  @Override
  public boolean isValue() {
    return true;
  }
}
