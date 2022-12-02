package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;

public final class ListObject extends PrimitiveObject<List<MegObject>> {

  public static ListRoot root = null;

  public ListObject(List<MegObject> value) {
    super(value);

    this.parent = ListObject.root;
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    if (arg.instanceOf(IntObject.class)) {
      final int size = this.value.size();
      final int index = ((IntObject) arg).value.intValue();

      return this.value.get((size + index % size) % size);

    } else if (arg.instanceOf(ListObject.class)) {
      final int size = this.value.size();
      final var list = new ArrayList<MegObject>(size);

      for (final MegObject e : ((ListObject) arg).value) {
        final int index = ((IntObject) e).value.intValue();
        list.add(this.value.get((size + index % size) % size));
      }

      return new ListObject(list);

    } else {
      return new EmptyObject();
    }
  }

  @Override
  public String toString() {
    return this.getPrimitiveValue().toString();
  }

  @Override
  public String getName() {
    return "[]";
  }
}
