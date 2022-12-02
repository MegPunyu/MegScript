package tokyo.meg.script.treewalker.objects.builtin.file;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegRead extends MegObject {

  MegRead() {
    super();
    this.name = "read";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final Path path = Paths.get(arg.getPrimitiveValue().toString());

    return new MegObject(this.outer, charset -> {
      String content = "";

      try {
        content = Files.readString(path, Charset.forName(charset.getPrimitiveValue().toString()));
      } catch (IOException e) {
      }

      return new StrObject(content);
    });
  }
}
