package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Bsol extends Factor {

  public final String name;
  public final boolean isRelative;

  public Bsol(String name, boolean isRelative) {
    this.name = name;
    this.isRelative = isRelative;
  }

  public NodeType getType() {
    return NodeType.BSOL;
  }

  @Override
  public boolean isEnvironmental() {
    return true;
  }
}
