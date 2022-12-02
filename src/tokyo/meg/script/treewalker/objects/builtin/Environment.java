package tokyo.meg.script.treewalker.objects.builtin;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.date.*;
import tokyo.meg.script.treewalker.objects.builtin.file.*;
import tokyo.meg.script.treewalker.objects.builtin.math.*;
import tokyo.meg.script.treewalker.objects.builtin.standardio.*;
import tokyo.meg.script.treewalker.objects.builtin.std.*;

public final class Environment extends MegObject {

  public Environment() {
    super();

    this.name = "Env";

    final MegObject env = this;
    this.attributes = new HashMap<>() {
      {
        this.put("Env", env);
        this.put("Root", MegObject.root);
        this.put("Real", new RealRoot());
        this.put("Int", new IntRoot());
        this.put("Str", new StrRoot());
        this.put("List", new ListRoot());

        this.put("not", new MegNot());

        this.put("File", new MegFile());
        this.put("Math", new MegMath());
        this.put("Date", new MegDate());

        final MegObject stdio = new MegStandardIO();
        this.put("StandardIO", stdio);

        this.put("input", stdio.attributes.get("readLine"));
        this.put("log", stdio.attributes.get("writeLine"));
        this.put("print", stdio.attributes.get("write"));
      }
    };
  }
}
