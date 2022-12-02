package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;
import java.util.stream.*;

import tokyo.meg.script.treewalker.objects.*;

import java.util.function.*;

public final class ListRoot extends MegObject {

  public static ListObject getInstance(List<MegObject> value) {
    return new ListObject(value);
  }

  public ListRoot() {
    ListObject.root = this;

    this.name = "List";
    this.attributes = this.getDefaultAttributes();
  }

  private Map<String, MegObject> getDefaultAttributes() {
    final var ret = new HashMap<String, MegObject>();

    ret.put("len", new MegObject(this.outer, a -> new IntObject(((ListObject) a).value.size())));

    ret.put("copy", new MegObject(this.outer, a -> {
      return new ListObject(new ArrayList<>(((ListObject) a).value));
    }));

    MegObject setter = this.set();
    ret.put("set", setter);

    ret.put("=", setter);
    ret.put("|>", this.map());
    ret.put(">-", this.filter());
    ret.put(">|", this.reduce());
    ret.put(">-<", this.join());
    ret.put("<++", this.concat(false));
    ret.put("++>", this.concat(true));
    ret.put("<+", this.insert(false));
    ret.put("+>", this.insert(true));
    ret.put("<-", this.remove(false));
    ret.put("->", this.remove(true));
    ret.put("<--", this.split(false));
    ret.put("-->", this.split(true));

    return ret;
  }

  private static int getIndex(MegObject indexObject, MegObject list) {
    final int size = ((ListObject) list).value.size();
    int index = ((IntObject) indexObject).value.intValue();

    return (size + index % size) % size;
  }

  private MegObject set() {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class)) {
        final var list = ((ListObject) left).value;
        final int index = ListRoot.getIndex(right, left);

        return new MegObject(left.outer, a -> {
          list.set(index, a);
          return left;
        });
      }

      return new EmptyObject();
    });
  }

  private MegObject map() {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class)) {
        final var list = ((ListObject) left).value;

        for (int i = 0; i < list.size(); ++i) {
          list.set(i, right.call(list.get(i)));
        }

        return left;
      }

      return new EmptyObject();
    });
  }

  private MegObject filter() {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class)) {
        final var list = ((ListObject) left).value;
        final var newList = new ArrayList<MegObject>(list.size());

        for (final MegObject element : list) {
          final MegObject test = right.call(element);
          if (test.isTruthy()) {
            newList.add(element);
          }
        }

        list.clear();
        list.addAll(newList);

        return left;
      }

      return new EmptyObject();
    });
  }

  private MegObject reduce() {
    return this.createOperator((left, right) -> {
      return new MegObject(left.outer, initialValue -> {
        MegObject result = initialValue;

        for (final MegObject element : ((ListObject) left).value) {
          result = right.call(result).call(element);
        }

        return result;
      });
    });
  }

  private MegObject join() {
    return this.createOperator((left, right) -> {
      final Function<MegObject, String> toString = e -> {
        return ((StrObject) e.findAttribute("toStr").call(e)).value;
      };

      final String str = toString.apply(right);
      return new StrObject(((ListObject) left).value.stream().map(toString).collect(Collectors.joining(str)));
    });
  }

  private MegObject concat(boolean fromLast) {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class) && right.instanceOf(ListObject.class)) {

        if (fromLast) {
          ((ListObject) left).value.addAll(((ListObject) right).value);

        } else {
          ((ListObject) left).value.addAll(0, ((ListObject) right).value);
        }

        return left;
      }

      return new EmptyObject();
    });
  }

  private MegObject remove(boolean fromLast) {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class) && right.instanceOf(IntObject.class)) {

        final List<MegObject> list = ((ListObject) left).value;
        final int size = list.size();

        final int index = ListRoot.getIndex(right, left);

        final var ret = list.remove(fromLast ? size - 1 - index : index);

        return ret;
      }

      return new EmptyObject();
    });
  }

  private MegObject insert(boolean fromLast) {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class)) {

        final List<MegObject> list = ((ListObject) left).value;

        if (fromLast) {
          list.add(right);
        } else {
          list.add(0, right);
        }

        return left;
      }

      return new EmptyObject();
    });
  }

  private MegObject split(boolean fromLast) {
    return this.createOperator((left, right) -> {
      if (left.instanceOf(ListObject.class) && right.instanceOf(IntObject.class)) {

        final List<MegObject> list = ((ListObject) left).value;

        final int size = list.size();
        final int index = ListRoot.getIndex(right, left);

        final List<MegObject> subList = fromLast ? list.subList(size - 1 - index, size) : list.subList(0, index);
        final List<MegObject> ret = new ArrayList<>(subList);

        subList.clear();

        return new ListObject(ret);
      }

      return new EmptyObject();
    });
  }
}
