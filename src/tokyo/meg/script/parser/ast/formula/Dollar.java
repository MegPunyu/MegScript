package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.*;

public final class Dollar extends Formula {

  public final String symbol;
  public final Formula formula;

  public Dollar(String symbol, Formula formula) {
    this.symbol = symbol;
    this.formula = formula;
  }

  public NodeType getType() {
    return NodeType.DOLLAR;
  }

  @Override
  public boolean isEnvironmental() {
    return true;
  }
}
