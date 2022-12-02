package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.*;

public final class Colon extends Formula {

  public final String arg;
  public final Formula formula;

  public Colon(String arg, Formula formula) {
    this.arg = arg;
    this.formula = formula;
  }

  public NodeType getType() {
    return NodeType.COLON;
  }
}
