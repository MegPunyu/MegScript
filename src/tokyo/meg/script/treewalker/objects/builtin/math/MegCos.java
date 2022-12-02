package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegCos extends MegObject {

  MegCos() {
    super();
    this.name = "cos";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Object argValue = arg.getPrimitiveValue();

    if (arg.instanceOf(IntObject.class)) {
      return new RealObject(Math.cos(((Long) argValue).doubleValue()));

    } else if (arg.instanceOf(RealObject.class)) {
      return new RealObject(Math.cos(((Double) argValue)));

    } else {
      return new RealObject(0.0D);
    }
  }
}
