package tokyo.meg.script.parser.ast.expression;

import tokyo.meg.script.parser.ast.formula.*;

public abstract class Expression extends Formula {

  @Override
  public boolean isExpression() {
    return true;
  }
}
