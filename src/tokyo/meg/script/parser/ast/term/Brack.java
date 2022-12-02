package tokyo.meg.script.parser.ast.term;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.procedural.*;

public final class Brack extends Term {

  public final Term func;
  public final Procedural arg;

  public Brack(Term func, Procedural arg) {
    this.func = func;
    this.arg = arg;
  }

  public NodeType getType() {
    return NodeType.BRACK;
  }
}
