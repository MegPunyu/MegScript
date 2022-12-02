package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Int extends Factor {

  public final long value;
  public final String suffix;

  public Int(String value) {
    String[] parts = value.split("(?<=^\\d+)(?=\\D.*$)");

    this.value = Long.parseLong(parts[0]);
    this.suffix = parts.length == 1 ? null : parts[1];
  }

  public NodeType getType() {
    return NodeType.INT;
  }

  @Override
  public boolean isValue() {
    return true;
  }
}
