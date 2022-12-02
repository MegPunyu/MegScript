package tokyo.meg.script.treewalker.objects.builtin.date;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegTime extends MegObject {

  public MegTime() {
    super();
    this.name = "time";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    return new IntObject(System.currentTimeMillis());
  }
}
