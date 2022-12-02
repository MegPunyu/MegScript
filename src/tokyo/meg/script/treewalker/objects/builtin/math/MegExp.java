package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegExp extends MegObject {

  MegExp() {
    super();
    this.name = "exp";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Object argValue = arg.getPrimitiveValue();

    if (arg.instanceOf(IntObject.class)) {
      return new RealObject(Math.exp(((Long) argValue).doubleValue()));

    } else if (arg.instanceOf(RealObject.class)) {
      return new RealObject(Math.exp(((Double) argValue)));

    } else {
      return new RealObject(0.0D);
    }
  }
}
