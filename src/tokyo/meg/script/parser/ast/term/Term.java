package tokyo.meg.script.parser.ast.term;

import tokyo.meg.script.parser.ast.expression.*;

public abstract class Term extends Expression {

  @Override
  public boolean isTerm() {
    return true;
  }
}
