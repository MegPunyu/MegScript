package tokyo.meg.script.treewalker.objects.builtin.file;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegAppend extends MegObject {

  MegAppend() {
    super();
    this.name = "append";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final String contentText = arg.findAttribute("toStr").call(arg).getPrimitiveValue().toString();

    return new MegObject(this.outer, pathString -> new MegObject(this.outer, charset -> {
      final Path path = Paths.get(pathString.getPrimitiveValue().toString());

      try {
        Files.writeString(path, contentText, Charset.forName(charset.getPrimitiveValue().toString()),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return new IntObject(1L);
      } catch (IOException e) {
        return new IntObject(0L);
      }
    }));
  }
}
