package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.procedural.*;

public final class Par extends Factor {

  public final Procedural procedural;

  public Par(Procedural procedural) {
    this.procedural = procedural;
  }

  public NodeType getType() {
    return NodeType.PAR;
  }
}
