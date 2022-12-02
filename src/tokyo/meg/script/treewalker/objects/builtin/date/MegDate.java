package tokyo.meg.script.treewalker.objects.builtin.date;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegDate extends MegObject {
  static MegDate instance = null;

  public MegDate() {
    super();

    MegDate.instance = this;

    this.name = "Date";

    this.attributes = new HashMap<>() {
      {
        this.put("now", new MegNow());
        this.put("time", new MegTime());
        this.put("toStr", new MegObject(MegDate.instance.outer, a -> {
          final var sb = new StringBuilder();

          sb.append(a.attributes.get("year").getPrimitiveValue());
          sb.append("-");

          sb.append(MegDate.pad((long) a.attributes.get("month").getPrimitiveValue() + 1));
          sb.append("-");
          sb.append(MegDate.pad(a.attributes.get("date").getPrimitiveValue()));
          sb.append("T");
          sb.append(MegDate.pad(a.attributes.get("hour").getPrimitiveValue()));
          sb.append(":");
          sb.append(MegDate.pad(a.attributes.get("minute").getPrimitiveValue()));
          sb.append(":");
          sb.append(MegDate.pad(a.attributes.get("second").getPrimitiveValue()));

          return new StrObject(sb.toString());
        }));
      }
    };
  }

  private static String pad(Object s) {
    final String str = s.toString();
    return str.length() == 2 ? str : "0" + str;
  }
}
