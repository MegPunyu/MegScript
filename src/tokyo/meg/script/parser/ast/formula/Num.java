package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.*;

public final class Num extends Formula {

  public final Formula parent;
  public final Formula child;

  public Num(Formula parent, Formula child) {
    this.parent = parent;
    this.child = child;
  }

  public NodeType getType() {
    return NodeType.NUM;
  }
}
