package tokyo.meg.script.treewalker.objects;

public abstract class PrimitiveObject<T> extends MegObject {

  public final T value;

  public PrimitiveObject(T value) {
    super();
    this.value = value;
  }

  public PrimitiveObject(T value, MegObject outer) {
    super();
    this.value = value;
  }

  @Override
  public abstract String toString();

  @Override
  public final boolean isPrimitive() {
    return true;
  }

  @Override
  public final T getPrimitiveValue() {
    return this.value;
  }
}
