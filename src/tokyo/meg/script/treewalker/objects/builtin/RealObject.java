package tokyo.meg.script.treewalker.objects.builtin;

import tokyo.meg.script.treewalker.objects.*;

public final class RealObject extends PrimitiveObject<Double> {

  public static RealRoot root = null;

  public RealObject(double value) {
    this(value, null);
  }

  public RealObject(double value, String suffix) {
    super(value);
    this.parent = RealObject.root;
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return this.value == 0.0 ? this : new RealObject(0.0D);
  }

  @Override
  public String toString() {
    return this.getPrimitiveValue().toString();
  }

  @Override
  public String getName() {
    return ((Double) value).toString();
  }
}
