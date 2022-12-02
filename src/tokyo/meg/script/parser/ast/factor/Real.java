package tokyo.meg.script.parser.ast.factor;

import tokyo.meg.script.parser.ast.*;

public final class Real extends Factor {

  public final double value;
  public final String suffix;

  public Real(String value) {
    final String[] parts = value.split("(?<=^\\d+)(?=\\D.*$)");
    final String[] fracParts = parts[1].split("(?<=^.\\d*)(?=\\D.*$)");

    this.value = Double.parseDouble(parts[0] + fracParts[0]);
    this.suffix = fracParts.length == 1 ? null : fracParts[1];
  }

  public NodeType getType() {
    return NodeType.REAL;
  }
 
  @Override
  public boolean isValue() {
    return true;
  }
}
