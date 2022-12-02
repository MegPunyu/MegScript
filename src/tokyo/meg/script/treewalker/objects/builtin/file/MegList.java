package tokyo.meg.script.treewalker.objects.builtin.file;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegList extends MegObject {

  MegList() {
    super();
    this.name = "list";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final String path = arg.getPrimitiveValue().toString();
    String[] list = new File(path).list();

    if (list == null) {
      list = new String[] {};
    }

    final List<MegObject> ret = new ArrayList<>(List.of(list))
        .stream()
        .map(e -> new StrObject(e))
        .collect(Collectors.toList());

    return new ListObject(ret);
  }
}
