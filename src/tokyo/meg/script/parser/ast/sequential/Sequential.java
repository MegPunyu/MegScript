package tokyo.meg.script.parser.ast.sequential;

import tokyo.meg.script.parser.ast.procedural.*;

public abstract class Sequential extends Procedural {

  @Override
  public boolean isSequential() {
    return true;
  }
}
