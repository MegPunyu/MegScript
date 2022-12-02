package tokyo.meg.script.parser.ast.sequential;

import java.util.*;

import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.formula.*;

public final class Comma extends Sequential {

  public final Deque<Formula> formulae;

  public Comma(Deque<Formula> formulae) {
    this.formulae = formulae;
  }

  public NodeType getType() {
    return NodeType.COMMA;
  }
}
