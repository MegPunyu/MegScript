package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegSin extends MegObject {

  MegSin() {
    super();
    this.name = "sin";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Object argValue = arg.getPrimitiveValue();

    if (arg.instanceOf(IntObject.class)) {
      return new RealObject(Math.sin(((Long) argValue).doubleValue()));

    } else if (arg.instanceOf(RealObject.class)) {
      return new RealObject(Math.sin(((Double) argValue)));

    } else {
      return new RealObject(0.0D);
    }
  }
}
