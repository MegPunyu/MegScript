package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegRand extends MegObject {

  MegRand() {
    super();
    this.name = "rand";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return new RealObject(Math.random());
  }
}
