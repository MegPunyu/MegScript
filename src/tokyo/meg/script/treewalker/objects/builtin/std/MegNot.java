package tokyo.meg.script.treewalker.objects.builtin.std;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegNot extends MegObject {

  public MegNot() {
    super();
    this.name = "not";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return new IntObject(arg.isTruthy() ? 1L : 0L);
  }
}
