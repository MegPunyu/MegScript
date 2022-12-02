package tokyo.meg.script.treewalker.objects.builtin.standardio;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegWriteLine extends MegObject {

  public MegWriteLine() {
    super();
    this.name = "writeLine";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final var str = (StrObject) arg.findAttribute("toStr").call(arg);
    System.out.println(str.getPrimitiveValue());
    return this;
  }
}
