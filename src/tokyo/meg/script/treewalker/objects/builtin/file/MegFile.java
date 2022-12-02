package tokyo.meg.script.treewalker.objects.builtin.file;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;

public class MegFile extends MegObject {

  public MegFile() {
    super();
    this.name = "File";

    this.attributes = new HashMap<>() {
      {
        this.put("append", new MegAppend());
        this.put("del", new MegDel());
        this.put("exists", new MegExists());
        this.put("list", new MegList());
        this.put("mkdir", new MegMkdir());
        this.put("overwrite", new MegOverwrite());
        this.put("read", new MegRead());
        this.put("write", new MegWrite());
      }
    };
  }
}
