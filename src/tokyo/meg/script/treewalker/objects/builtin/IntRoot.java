package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;
import java.util.function.*;

import tokyo.meg.script.treewalker.objects.*;

public final class IntRoot extends MegObject {

  public static IntObject getInstance(long value, String suffix) {
    return new IntObject(value, suffix);
  }

  public IntRoot() {
    this.name = "Int";
    this.attributes = this.getDefaultAttributes();
    IntObject.root = this;
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final var ret = new HashMap<String, MegObject>();

    ret.put("parse", new MegObject(null, a -> IntRoot.parse(a)));

    ret.put("+", this.arithmeticOperator((a, b) -> a + b, (a, b) -> a + b));
    ret.put("-", this.arithmeticOperator((a, b) -> a - b, (a, b) -> a - b));
    ret.put("*", this.arithmeticOperator((a, b) -> a * b, (a, b) -> a * b));
    ret.put("/", this.arithmeticOperator((a, b) -> a / b, (a, b) -> a / b));
    ret.put("%", this.arithmeticOperator((a, b) -> a % b, (a, b) -> a % b));
    ret.put("**", this.arithmeticOperator((a, b) -> ((Double) Math.pow(a, b)).longValue(), (a, b) -> Math.pow(a, b)));

    ret.put(">>", this.compareOperator((a, b) -> a > b, (a, b) -> a > b));
    ret.put(">=", this.compareOperator((a, b) -> a >= b, (a, b) -> a >= b));
    ret.put("<<", this.compareOperator((a, b) -> a < b, (a, b) -> a < b));
    ret.put("<=", this.compareOperator((a, b) -> a <= b, (a, b) -> a <= b));
    ret.put("==", this.compareOperator((a, b) -> (long) a == b, (a, b) -> (double) a == b));
    ret.put("<>", this.compareOperator((a, b) -> (long) a != b, (a, b) -> (double) a != b));

    ret.put("&&", this.booleanOperator((a, b) -> a && b));
    ret.put("||", this.booleanOperator((a, b) -> a || b));
    ret.put("^^", this.booleanOperator((a, b) -> a ^ b));
    ret.put("!&", this.booleanOperator((a, b) -> !(a && b)));
    ret.put("!|", this.booleanOperator((a, b) -> !(a || b)));
    ret.put("!^", this.booleanOperator((a, b) -> !(a ^ b)));

    ret.put("&", this.bitwiseOperator((a, b) -> a & b));
    ret.put("|", this.bitwiseOperator((a, b) -> a | b));
    ret.put("^", this.bitwiseOperator((a, b) -> a ^ b));
    ret.put("~&", this.bitwiseOperator((a, b) -> ~(a & b)));
    ret.put("~|", this.bitwiseOperator((a, b) -> ~(a | b)));
    ret.put("~^", this.bitwiseOperator((a, b) -> ~(a ^ b)));
    ret.put("<-", this.bitwiseOperator((a, b) -> a << b));
    ret.put("->", this.bitwiseOperator((a, b) -> a >> b));

    return ret;
  }

  private static MegObject parse(MegObject arg) {
    long value = 0L;

    try {
      value = Long.parseLong(arg.toString());
    } catch (Exception e) {
    }

    return new IntObject(value, null);

  }

  private MegObject arithmeticOperator(
      BinaryOperator<Long> operatorForLong,
      BinaryOperator<Double> operatorForDouble) {

    return this.createOperator((a, b) -> {
      final var left = (long) a.getPrimitiveValue();

      if (b.instanceOf(IntObject.class)) {
        final var right = (long) b.getPrimitiveValue();

        return new IntObject(operatorForLong.apply(left, right));

      } else if (b.instanceOf(RealObject.class)) {
        final var right = (double) b.getPrimitiveValue();

        return new RealObject(operatorForDouble.apply((double) left, right));

      }

      return new IntObject(operatorForLong.apply(left, 0L));
    });
  }

  private MegObject compareOperator(
      BiFunction<Long, Long, Boolean> operatorForLong,
      BiFunction<Long, Double, Boolean> operatorForDouble) {

    return this.createOperator((a, b) -> {
      final var left = (long) a.getPrimitiveValue();

      if (b.instanceOf(IntObject.class)) {
        final var right = (long) b.getPrimitiveValue();

        return new IntObject(operatorForLong.apply(left, right) ? 1L : 0L);

      } else if (b.instanceOf(RealObject.class)) {
        final var right = (double) b.getPrimitiveValue();

        return new IntObject(operatorForDouble.apply(left, right) ? 1L : 0L);

      }

      return new IntObject(operatorForLong.apply(left, 0L) ? 1L : 0L);
    });
  }

  private MegObject booleanOperator(
      BiFunction<Boolean, Boolean, Boolean> operator) {

    return this.createOperator((a, b) -> {
      final boolean left = (long) a.getPrimitiveValue() != 0L;
      boolean right = false;

      if (b.instanceOf(IntObject.class)) {
        right = (long) b.getPrimitiveValue() != 0L;

      } else if (b.instanceOf(RealObject.class)) {
        right = (double) b.getPrimitiveValue() != 0.0D;

      }

      return new IntObject(operator.apply(left, right) ? 1L : 0L);
    });
  }

  private MegObject bitwiseOperator(
      BiFunction<Long, Long, Long> operator) {

    return this.createOperator((a, b) -> {
      final var left = (long) a.getPrimitiveValue();
      long right = 0L;

      if (b.instanceOf(IntObject.class)) {
        right = (long) b.getPrimitiveValue();

      } else if (b.instanceOf(RealObject.class)) {
        right = ((Double) b.getPrimitiveValue()).longValue();

      }

      return new IntObject(operator.apply(left, right));
    });
  }
}
