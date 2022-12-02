package tokyo.meg.script.treewalker.objects.builtin.standardio;

import java.io.*;
import java.util.*;

import tokyo.meg.script.treewalker.objects.*;

public class MegStandardIO extends MegObject {

  final static BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));

  public MegStandardIO() {
    super();
    this.name = "StandardIO";

    this.attributes = new HashMap<>() {
      {
        this.put("readLine", new MegReadLine());
        this.put("write", new MegWrite());
        this.put("writeLine", new MegWriteLine());
      }
    };
  }
}
