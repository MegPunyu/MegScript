package tokyo.meg.script.parser.ast.procedural;

import java.util.*;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.sequential.*;

public final class Semi extends Procedural {

  public final Deque<Sequential> sequentials;

  public Semi(Deque<Sequential> sequentials) {
    this.sequentials = sequentials;
  }

  public NodeType getType() {
    return NodeType.SEMI;
  }
}
