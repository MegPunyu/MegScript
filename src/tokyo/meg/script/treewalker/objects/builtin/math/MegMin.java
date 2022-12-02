package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;

public final class MegMin extends MegObject {

  MegMin() {
    super();
    this.name = "min";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject left) {
    return new MegObject(null, right -> {
      final double leftValue = (Double) left.getPrimitiveValue();
      final double rightValue = (Double) right.getPrimitiveValue();

      return leftValue <= rightValue ? left : right;
    });
  }
}
