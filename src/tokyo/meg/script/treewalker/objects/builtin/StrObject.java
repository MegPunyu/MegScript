package tokyo.meg.script.treewalker.objects.builtin;

import tokyo.meg.script.treewalker.objects.*;

public final class StrObject extends PrimitiveObject<String> {

  public static StrRoot root = null;

  public StrObject(String value) {
    super(value);
    this.name = value;
    this.parent = StrObject.root;
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    if (arg.instanceOf(IntObject.class)) {
      final int size = this.value.length();
      final int index = ((IntObject) arg).value.intValue();

      return new StrObject(((Character) this.value.charAt((size + index % size) % size)).toString());

    } else if (arg.instanceOf(ListObject.class)) {
      final int size = this.value.length();
      final var sb = new StringBuilder();

      for (final MegObject e : ((ListObject) arg).value) {
        final int index = ((IntObject) e).value.intValue();
        sb.append(this.value.charAt((size + index % size) % size));
      }

      return new StrObject(sb.toString());

    } else {
      return new StrObject("");
    }
  }

  @Override
  public String toString() {
    return this.value;
  }
}
