package tokyo.meg.script.treewalker.objects.builtin.date;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegNow extends MegObject {

  public MegNow() {
    super();
    this.name = "now";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final var now = Calendar.getInstance();
    final var ret = new MegObject(this.outer);

    ret.parent = MegDate.instance;
    ret.attributes = new HashMap<>() {
      {
        this.put("year", new IntObject(now.get(Calendar.YEAR)));
        this.put("month", new IntObject(now.get(Calendar.MONTH)));
        this.put("date", new IntObject(now.get(Calendar.DATE)));
        this.put("day", new IntObject(now.get(Calendar.DAY_OF_WEEK)));
        this.put("hour", new IntObject(now.get(Calendar.HOUR_OF_DAY)));
        this.put("minute", new IntObject(now.get(Calendar.MINUTE)));
        this.put("second", new IntObject(now.get(Calendar.SECOND)));
        this.put("millisecond", new IntObject(now.get(Calendar.MILLISECOND)));
      }
    };

    return ret;
  }
}
