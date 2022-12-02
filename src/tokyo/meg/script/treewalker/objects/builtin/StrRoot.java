package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import tokyo.meg.script.treewalker.objects.*;

public final class StrRoot extends MegObject {

  public static StrObject getInstance(String value) {
    return new StrObject(value);
  }

  public StrRoot() {
    this.name = "Str";
    this.attributes = this.getDefaultAttributes();
    StrObject.root = this;
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final Map<String, MegObject> ret = new HashMap<>();

    ret.put("len", new MegObject(this.outer, a -> new IntObject(((StrObject) a).value.length())));

    ret.put("+", this.add());
    ret.put("*", this.mul());

    ret.put("==", this.compareOperator((a, b) -> a.equals(b)));
    ret.put("<>", this.compareOperator((a, b) -> !a.equals(b)));
    ret.put(">>", this.compareOperator((a, b) -> a.compareTo(b) == 1));
    ret.put("<<", this.compareOperator((a, b) -> a.compareTo(b) == -1));
    ret.put(">=", this.compareOperator((a, b) -> a.equals(b) || a.compareTo(b) == 1));
    ret.put("<=", this.compareOperator((a, b) -> a.equals(b) || a.compareTo(b) == -1));

    ret.put("<->", this.split());
    ret.put("//", this.replace());
    ret.put("-<>", this.indexOf());
    ret.put("<>-", this.lastIndexOf());

    return ret;
  }

  private MegObject add() {
    return this.createOperator((left, right) -> {
      final String str = right.getPrimitiveValue().toString();

      return new StrObject(((StrObject) left).value + str);
    });
  }

  private MegObject mul() {

    return this.createOperator((left, right) -> {
      Long count = 0L;

      if (right.instanceOf(IntObject.class)) {
        count = ((IntObject) right).value;
      }

      return new StrObject(((StrObject) left).value.repeat(count.intValue()));
    });

  }

  private MegObject compareOperator(
      BiFunction<String, String, Boolean> operator) {

    return this.createOperator((a, b) -> {
      final var left = (String) a.getPrimitiveValue();
      final var right = (String) b.getPrimitiveValue();

      return new IntObject(operator.apply(left, right) ? 1L : 0L);
    });
  }

  private MegObject split() {
    return this.createOperator((left, right) -> {
      final String str = left.getPrimitiveValue().toString();
      final String separator = right.getPrimitiveValue().toString();

      final List<StrObject> list = List.of(str.split(separator))
          .stream()
          .map(e -> new StrObject(e))
          .collect(Collectors.toList());

      return new ListObject(new ArrayList<>(list));
    });
  }

  private MegObject replace() {
    return this.createOperator((left, right) -> {

      final String str = left.getPrimitiveValue().toString();
      final String regex = right.getPrimitiveValue().toString();

      return new MegObject(left.outer, replacement -> {
        final String result = str.replaceAll(regex, replacement.getPrimitiveValue().toString());

        return new StrObject(result);
      });
    });
  }

  private MegObject indexOf() {
    return this.createOperator((left, right) -> {
      final String str = left.getPrimitiveValue().toString();
      final String pattern = right.getPrimitiveValue().toString();

      return new IntObject(str.indexOf(pattern));
    });
  }

  private MegObject lastIndexOf() {
    return this.createOperator((left, right) -> {
      final String str = left.getPrimitiveValue().toString();
      final String pattern = right.getPrimitiveValue().toString();

      return new IntObject(str.lastIndexOf(pattern));
    });
  }
}