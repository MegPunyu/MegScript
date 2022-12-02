package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;

public final class RootObject extends MegObject {

  public RootObject() {
    super();

    this.name = "Root";

    this.attributes = this.getDefaultAttributes();
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final var ret = new HashMap<String, MegObject>();

    ret.put("toStr", new MegObject(this.outer, a -> new StrObject(a.toString())));
    ret.put("instanceOf", this.createOperator((a, b) -> {
      boolean isInstance = false;
      MegObject current = a;
      while (current != null) {
        isInstance = current == b;
        if (!isInstance) {
          current = current.parent;
        } else {
          break;
        }
      }
      return new IntObject(isInstance ? 1L : 0L);
    }));

    ret.put("<", this.createOperator((a, b) -> a.call(b)));
    ret.put(">", this.createOperator((a, b) -> b.call(a)));
    ret.put("==", this.createOperator((a, b) -> new IntObject(a == b ? 1L : 0L)));

    return ret;
  }
}
