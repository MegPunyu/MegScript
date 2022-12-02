package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Empty extends Factor {

  public final boolean implicit;

  public Empty(boolean implicit) {
    this.implicit = implicit;
  }

  public NodeType getType() {
    return NodeType.EMPTY;
  }

  @Override
  public boolean isValue() {
    return true;
  }
}
