package tokyo.meg.script.treewalker.objects.builtin.math;

import tokyo.meg.script.treewalker.objects.*;

public final class MegMax extends MegObject {

  MegMax() {
    super();
    this.name = "max";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject left) {
    return new MegObject(null, right -> {
      final double leftValue = (Double) left.getPrimitiveValue();
      final double rightValue = (Double) right.getPrimitiveValue();

      return leftValue >= rightValue ? left : right;
    });
  }
}
