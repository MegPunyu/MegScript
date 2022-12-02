package tokyo.meg.script.treewalker.objects.builtin.standardio;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegWrite extends MegObject {

  public MegWrite() {
    super();
    this.name = "write";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final var str = (StrObject) arg.findAttribute("toStr").call(arg);
    System.out.print(str.getPrimitiveValue());
    return this;
  }
}
