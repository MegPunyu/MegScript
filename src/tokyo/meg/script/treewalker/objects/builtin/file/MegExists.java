package tokyo.meg.script.treewalker.objects.builtin.file;

import java.nio.file.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegExists extends MegObject {

  MegExists() {
    super();
    this.name = "exists";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Path path = Paths.get(arg.getPrimitiveValue().toString());

    return new IntObject(Files.exists(path) ? 1L : 0L);
  }
}
