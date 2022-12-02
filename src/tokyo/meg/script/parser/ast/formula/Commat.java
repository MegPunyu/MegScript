package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.*;

public final class Commat extends Formula {

  public final String attribute;
  public final Formula target;
  public final Formula formula;

  public Commat(String attribute, Formula target, Formula formula) {
    this.attribute = attribute;
    this.target = target;
    this.formula = formula;
  }

  public NodeType getType() {
    return NodeType.COMMAT;
  }
}
