package tokyo.meg.script.parser.ast;

public interface Node {

  NodeType getType();

  boolean isProcedural();

  boolean isSequential();

  boolean isFormula();

  boolean isExpression();

  boolean isTerm();

  boolean isFactor();

  boolean isValue();

  boolean isEnvironmental();
}
