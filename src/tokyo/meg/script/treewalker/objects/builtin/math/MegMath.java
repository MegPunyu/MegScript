package tokyo.meg.script.treewalker.objects.builtin.math;

import java.util.*;

import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class MegMath extends MegObject {

  public MegMath() {
    super();
    this.name = "Math";

    this.attributes = new HashMap<>() {
      {
        this.put("abs", new MegAbs());
        this.put("exp", new MegExp());
        this.put("ceil", new MegCeil());
        this.put("cos", new MegCos());
        this.put("floor", new MegFloor());
        this.put("log", new MegLog());
        this.put("max", new MegMax());
        this.put("min", new MegMin());
        this.put("rand", new MegRand());
        this.put("round", new MegRound());
        this.put("sin", new MegSin());
        this.put("tan", new MegTan());

        this.put("PI", new RealObject(Math.PI, null));
        this.put("E", new RealObject(Math.E, null));
      }
    };
  }
}
