package tokyo.meg.script;

import java.io.*;
import java.nio.file.*;

import tokyo.meg.script.treewalker.*;

public class NutMeg {
  public static void main(String[] args) throws IOException {

    if (args.length == 1) {
      String source = args[0];

      NutMeg.treeWalk(source);
    }
  }

  private static void treeWalk(String source) throws IOException {
    final Path sourcePath = Paths.get(source).toAbsolutePath().normalize();

    new TreeWalker(sourcePath).evaluate();
  }
}