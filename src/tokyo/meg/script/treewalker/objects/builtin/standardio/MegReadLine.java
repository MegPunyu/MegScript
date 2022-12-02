package tokyo.meg.script.treewalker.objects.builtin.standardio;

import java.io.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public class MegReadLine extends MegObject {

  public MegReadLine() {
    super();
    this.name = "readLine";
    this.function = this::invoke;
  }

  private MegObject invoke(MegObject arg) {
    final var prompt = (StrObject) arg.findAttribute("toStr").call(arg);

    System.out.print(prompt.getPrimitiveValue());

    String str;

    try {
      str = MegStandardIO.stdinReader.readLine();
    } catch (IOException e) {
      str = "";
    }

    return new StrObject(str);
  }
}
