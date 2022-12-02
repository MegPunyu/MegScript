package tokyo.meg.script.treewalker.objects.builtin.file;

import java.io.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegDel extends MegObject {

  MegDel() {
    super();
    this.name = "del";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final String path = arg.getPrimitiveValue().toString();

    return new IntObject(new File(path).delete() ? 1L : 0L);
  }
}
