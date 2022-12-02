package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.*;

public final class Quest extends Formula {

  public final Formula condition;
  public final Formula consequent;
  public final Formula alternative;

  public Quest(Formula condition, Formula consequent, Formula alternative) {
    this.condition = condition;
    this.consequent = consequent;
    this.alternative = alternative;
  }

  public NodeType getType() {
    return NodeType.QUEST;
  }
}
