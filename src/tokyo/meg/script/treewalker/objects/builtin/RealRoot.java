package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;
import java.util.function.*;

import tokyo.meg.script.treewalker.objects.*;

public final class RealRoot extends MegObject {

  public static RealObject getInstance(double value, String suffix) {
    return new RealObject(value, suffix);
  }

  public RealRoot() {
    this.name = "Real";
    this.attributes = this.getDefaultAttributes();
    RealObject.root = this;
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final var ret = new HashMap<String, MegObject>();

    ret.put("parse", new MegObject(this.outer, this::parse));

    ret.put("+", this.arithmeticOperator((a, b) -> a + b));
    ret.put("-", this.arithmeticOperator((a, b) -> a - b));
    ret.put("*", this.arithmeticOperator((a, b) -> a * b));
    ret.put("/", this.arithmeticOperator((a, b) -> a / b));
    ret.put("%", this.arithmeticOperator((a, b) -> a % b));
    ret.put("**", this.arithmeticOperator((a, b) -> Math.pow(a, b)));

    ret.put(">>", this.compareOperator((a, b) -> a > b));
    ret.put(">=", this.compareOperator((a, b) -> a >= b));
    ret.put("<<", this.compareOperator((a, b) -> a < b));
    ret.put("<=", this.compareOperator((a, b) -> a <= b));
    ret.put("==", this.compareOperator((a, b) -> (double) a == b));
    ret.put("<>", this.compareOperator((a, b) -> (double) a != b));

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

  private MegObject parse(MegObject arg) {
    double value = 0.0D;

    try {
      value = Double.parseDouble(arg.toString());
    } catch (Exception e) {
    }

    return new RealObject(value);
  }

  private MegObject arithmeticOperator(
      BinaryOperator<Double> operator) {

    return this.createOperator((a, b) -> {
      final var left = (double) a.getPrimitiveValue();
      double right = 0.0;

      if (b.instanceOf(IntObject.class)) {
        right = (double) (long) b.getPrimitiveValue();

      } else if (b.instanceOf(RealObject.class)) {
        right = (double) b.getPrimitiveValue();
      }

      return new RealObject(operator.apply(left, right));
    });
  }

  private MegObject compareOperator(
      BiFunction<Double, Double, Boolean> operator) {

    return this.createOperator((a, b) -> {
      final var left = (double) a.getPrimitiveValue();
      double right = 0.0;

      if (b.instanceOf(IntObject.class)) {
        right = (double) (long) b.getPrimitiveValue();

      } else if (b.instanceOf(RealObject.class)) {
        right = (double) b.getPrimitiveValue();
      }

      return new IntObject(operator.apply(left, right) ? 1L : 0L);
    });
  }

  private MegObject booleanOperator(
      BiFunction<Boolean, Boolean, Boolean> operator) {

    return this.createOperator((a, b) -> {
      final boolean left = (double) a.getPrimitiveValue() != 0.0D;
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
      final long left = ((Double) a.getPrimitiveValue()).longValue();
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
