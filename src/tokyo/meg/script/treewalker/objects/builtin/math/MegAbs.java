package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegAbs extends MegObject {

  MegAbs() {
    super();
    this.name = "abs";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Object argValue = arg.getPrimitiveValue();

    if (arg.instanceOf(IntObject.class)) {
      return new IntObject(Math.abs(((Long) argValue).longValue()));

    } else if (arg.instanceOf(RealObject.class)) {
      return new RealObject(Math.abs(((Double) argValue)));

    } else {
      return new IntObject(0L);
    }
  }
}
