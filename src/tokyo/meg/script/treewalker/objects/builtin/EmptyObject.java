package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;

public final class EmptyObject extends MegObject {

  public EmptyObject() {
    super();

    this.name = "()";
    this.attributes = this.getDefaultAttributes();
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return this;
  }

  @Override
  public String toString() {
    return "()";
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final var ret = new HashMap<String, MegObject>();

    ret.put("-", this.sub());

    return ret;
  }

  private MegObject sub() {
    return this.createOperator((a, b) -> {
      if (b.instanceOf(IntObject.class)) {
        return new IntObject(-(long) b.getPrimitiveValue());

      } else if (b.instanceOf(RealObject.class)) {
        return new RealObject(-(double) b.getPrimitiveValue());

      } else {
        return new EmptyObject();
      }
    });
  }
}