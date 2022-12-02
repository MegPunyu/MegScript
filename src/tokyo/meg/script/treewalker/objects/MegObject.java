package tokyo.meg.script.treewalker.objects;

import java.util.*;
import java.util.function.*;

public class MegObject {

  public static MegObject root = null;

  public MegObject parent = null;
  public Map<String, MegObject> attributes = null;

  public MegObject inner = null;
  public MegObject outer;
  public UnaryOperator<MegObject> function;
  public String name = null;

  {
    this.parent = MegObject.root;
    this.function = a -> this;
  }

  public MegObject() {
    this(null);
  }

  public MegObject(MegObject outer) {
    this.outer = outer;
  }

  public MegObject(MegObject outer, UnaryOperator<MegObject> function) {
    this.outer = outer;
    this.function = function;
  }

  public MegObject call(MegObject arg) {
    return this.function.apply(arg);
  }

  public MegObject createOperator(BinaryOperator<MegObject> operator) {
    return new MegObject(this.outer, a -> new MegObject(a.outer, b -> operator.apply(a, b)));
  }

  public MegObject findAttribute(String attribute) {
    MegObject result = null;

    if (this.attributes != null) {
      result = this.attributes.get(attribute);
    }

    if (result == null && this.parent != null) {
      return this.parent.findAttribute(attribute);
    }

    return result;
  }

  public MegObject setAttribute(String attribute, MegObject value) {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }

    this.attributes.put(attribute, value);

    return value;
  }

  public MegObject findVariable(String name) {
    MegObject result = this.findAttribute(name);

    if (result == null && this.outer != null) {
      return this.outer.findVariable(name);
    }

    return result;
  }

  public MegObject push() {
    return this.inner = new MegObject(this);
  }

  public MegObject pop() {
    this.outer.inner = null;
    return this.outer;
  }

  public boolean instanceOf(Class<? extends MegObject> type) {
    if (this.getClass().equals(type)) {
      return true;

    } else if (this.parent != null) {
      return this.parent.instanceOf(type);
    }

    return false;
  }

  public Object getPrimitiveValue() {
    if (this.isPrimitive()) {
      return this.getPrimitiveValue();

    } else if (this.parent != null) {
      return this.parent.getPrimitiveValue();
    }

    return "";
  }

  public String toString() {
    return this.toString(0);
  }

  private String toString(int indent) {
    final var sb = new StringBuilder();

    sb.append(this.name == null ? this.getName() : this.name);
    sb.append(" {\n");

    if (this.attributes != null) {
      for (final String attribute : this.attributes.keySet()) {
        sb.append(" ".repeat(indent + 2));
        sb.append(attribute);
        sb.append("\n");
      }
    }

    if (this.parent != null) {
      sb.append(" ".repeat(indent + 2));
      sb.append("# ");
      sb.append(this.parent.toString(indent + 2));
      sb.append("\n");
    }

    sb.append(" ".repeat(indent));
    sb.append("}");

    return sb.toString();
  }

  public boolean isPrimitive() {
    return false;
  }

  public boolean isTruthy() {
    return this.call(this) != this;
  }

  public String getName() {
    return "?";
  }
}
