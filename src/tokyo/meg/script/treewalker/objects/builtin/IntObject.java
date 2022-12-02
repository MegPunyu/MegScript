package tokyo.meg.script.treewalker.objects.builtin;

import tokyo.meg.script.treewalker.objects.*;

public final class IntObject extends PrimitiveObject<Long> {

  public static IntRoot root = null;

  public IntObject(long value) {
    this(value, null);
  }

  public IntObject(long value, String suffix) {
    super(value);
    this.parent = IntObject.root;
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return this.value == 0L ? this : new IntObject(0L);
  }

  @Override
  public String toString() {
    return this.getPrimitiveValue().toString();
  }

  @Override
  public String getName() {
    return ((Long) value).toString();
  }
}
