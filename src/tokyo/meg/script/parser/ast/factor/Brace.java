package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.procedural.*;

public final class Brace extends Factor {

  public final Procedural procedural;

  public Brace(Procedural procedural) {
    this.procedural = procedural;
  }

  public NodeType getType() {
    return NodeType.BRACE;
  }
}
