package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Str extends Factor {

  public final String value;

  public Str(String value) {
    this.value = value;
  }

  public NodeType getType() {
    return NodeType.STR;
  }

  @Override
  public boolean isValue() {
    return true;
  }
}
