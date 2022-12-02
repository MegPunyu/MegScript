package tokyo.meg.script.parser.ast.formula;

import tokyo.meg.script.parser.ast.sequential.*;

public abstract class Formula extends Sequential {

  @Override
  public boolean isFormula() {
    return true;
  }

  @Override
  public boolean isEnvironmental() {
    return false;
  }
}
