package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.term.*;

public abstract class Factor extends Term {

  @Override
  public boolean isFactor() {
    return true;
  }
}
