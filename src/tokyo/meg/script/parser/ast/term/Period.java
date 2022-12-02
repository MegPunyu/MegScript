package tokyo.meg.script.parser.ast.term;

import tokyo.meg.script.parser.ast.*;

public final class Period extends Term {

  public final Term term;
  public final String attribute;

  public Period(Term term, String attribute) {
    this.term = term;
    this.attribute = attribute;
  }

  public NodeType getType() {
    return NodeType.PERIOD;
  }
}
