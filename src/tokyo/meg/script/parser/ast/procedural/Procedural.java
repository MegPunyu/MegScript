package tokyo.meg.script.parser.ast.procedural;

import tokyo.meg.script.parser.ast.*;

public abstract class Procedural implements Node {
  public boolean isProcedural() {
    return true;
  }

  public boolean isSequential() {
    return false;
  }

  public boolean isFormula() {
    return false;
  }

  public boolean isExpression() {
    return false;
  }

  public boolean isTerm() {
    return false;
  }

  public boolean isFactor() {
    return false;
  }

  public boolean isValue() {
    return false;
  }

  public boolean isEnvironmental() {
    return true;
  }
}
