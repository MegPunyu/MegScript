package tokyo.meg.script.treewalker;

import java.nio.file.*;
import java.util.*;
import java.util.function.*;

import tokyo.meg.script.parser.*;
import tokyo.meg.script.parser.ast.*;
import tokyo.meg.script.parser.ast.expression.*;
import tokyo.meg.script.parser.ast.factor.*;
import tokyo.meg.script.parser.ast.formula.*;
import tokyo.meg.script.parser.ast.procedural.*;
import tokyo.meg.script.parser.ast.sequential.*;
import tokyo.meg.script.parser.ast.term.*;
import tokyo.meg.script.treewalker.objects.*;
import tokyo.meg.script.treewalker.objects.builtin.*;

public final class TreeWalker {

  private final MegObject rootEnvironment;
  private final Brace program;
  private final Map<String, MegObject> modules = new HashMap<>();

  private MegObject currentValue = null;
  private MegObject environment;
  private Path directory;

  public TreeWalker(Path path) {
    MegObject.root = new RootObject();

    this.directory = path.getParent();

    this.environment = new Environment().push();
    this.environment.outer.setAttribute("Main", this.environment);
    this.environment.name = "Main";

    this.rootEnvironment = this.environment;
    this.program = new Parser(path).parse();
  }

  public MegObject evaluate() {
    return this.evaluateNode(this.program.procedural);
  }

  private MegObject evaluateNode(Node node) {
    return this.currentValue = switch (node.getType()) {
      case APOS -> this.evaluateApos((Apos) node);
      case BRACE -> this.evaluateBrace((Brace) node);
      case BRACK -> this.evaluateBrack((Brack) node);
      case BSOL -> this.evaluateBsol((Bsol) node);
      case COLON -> this.evaluateColon((Colon) node);
      case COMMA -> this.evaluateComma((Comma) node);
      case COMMAT -> this.evaluateCommat((Commat) node);
      case DOLLAR -> this.evaluateDollar((Dollar) node);
      case INFIX -> this.evaluateInfix((Infix) node);
      case INT -> IntRoot.getInstance(((Int) node).value, ((Int) node).suffix);
      case NUM -> this.evaluateNum((Num) node);
      case PAR -> this.evaluatePar((Par) node);
      case PERIOD -> this.evaluatePeriod((Period) node);
      case QUEST -> this.evaluateQuest((Quest) node);
      case REAL -> RealRoot.getInstance(((Real) node).value, ((Real) node).suffix);
      case SEMI -> this.evaluateSemi((Semi) node);
      case STR -> StrRoot.getInstance(((Str) node).value);
      case VARIABLE -> this.evaluateVariable((Variable) node);
      default -> new EmptyObject();
    };
  }

  private MegObject evaluateApos(Apos node) {
    final MegObject target = this.evaluateNode(node.term);
    final MegObject value = this.nullToEmpty(target.findAttribute(node.attribute));

    return value.call(target);
  }

  private MegObject evaluateBrace(Brace node) {
    final MegObject struct = this.environment = this.environment.push();

    this.evaluateNode(node.procedural);

    this.environment = this.environment.pop();

    return struct;
  }

  private MegObject evaluateBrack(Brack node) {
    final MegObject func = this.evaluateNode(node.func);
    final MegObject arg = this.evaluateInternally(node.arg);

    return func.call(arg);
  }

  private MegObject evaluateBsol(Bsol node) {
    final String root = node.isRelative ? "" : System.getenv("MEGSCRIPT_HOME") + "/lib/";
    final Path path = this.directory.resolve(root + node.name + ".meg").toAbsolutePath();

    MegObject module = this.modules.get(path.toString());

    if (module == null) {
      final var par = new Parser(path);
      final Brace ast = par.parse();

      final Path currentDirectory = this.directory;
      final MegObject currentEnvironment = this.environment;

      this.directory = path.getParent();
      this.environment = this.rootEnvironment;

      module = this.evaluateNode(ast);

      this.environment = currentEnvironment;
      this.directory = currentDirectory;

      this.modules.put(path.toString(), module);
    }

    if (module.attributes != null) {
      if (this.environment.attributes == null) {
        this.environment.attributes = new HashMap<>();
      }
      this.environment.attributes.putAll(module.attributes);
    }

    if (module.function != null) {
      this.environment.function = module.function;
    }

    return module;
  }

  private MegObject evaluateColon(Colon node) {
    final MegObject outer = this.environment;

    return new MegObject(outer, arg -> this.applyFunction(node, arg, outer));
  }

  private MegObject evaluateComma(Comma node) {
    final var list = new ArrayList<MegObject>(node.formulae.stream().map(this::evaluateNode).toList());

    return ListRoot.getInstance(list);
  }

  private MegObject evaluateCommat(Commat node) {
    final MegObject target = this.evaluateInternally(node.target);
    final MegObject value = this.evaluateInternally(node.formula);

    target.setAttribute(node.attribute, value);

    return value;
  }

  private MegObject evaluateDollar(Dollar node) {
    final MegObject value = this.evaluateInternally(node.formula);

    if (value.name == null) {
      value.name = node.symbol;
    }

    if (node.symbol == null) {
      this.environment.function = arg -> value.call(arg);

    } else {
      this.environment.setAttribute(node.symbol, value);
    }

    return value;
  }

  private MegObject evaluateInfix(Infix node) {
    final MegObject left = this.evaluateNode(node.left);
    final MegObject right = this.evaluateInternally(node.right);
    final MegObject operator = this.nullToEmpty(left.findVariable(node.operator));

    return this.applyOperator(operator, left, right);
  }

  private MegObject evaluateNum(Num node) {
    final MegObject parent = this.evaluateInternally(node.parent);
    final MegObject child = this.evaluateInternally(node.child);

    child.parent = parent;

    return child;
  }

  private MegObject evaluatePar(Par node) {
    return this.evaluateInternally(node.procedural);
  }

  private MegObject evaluatePeriod(Period node) {
    return this.nullToEmpty(this.evaluateNode(node.term).findAttribute(node.attribute));
  }

  private MegObject evaluateQuest(Quest node) {
    final MegObject condition = this.evaluateInternally(node.condition);

    return this.evaluateInternally(condition.isTruthy() ? node.consequent : node.alternative);
  }

  private MegObject evaluateSemi(Semi node) {
    node.sequentials.forEach(this::evaluateNode);

    return this.currentValue;
  }

  private MegObject evaluateVariable(Variable node) {
    return this.nullToEmpty(this.environment.findVariable(node.name));
  }

  private MegObject evaluateInternally(Node node) {
    return this.doInternally(() -> this.evaluateNode(node));
  }

  private MegObject doInternally(Supplier<MegObject> supplier) {
    this.environment = this.environment.push();

    final MegObject value = supplier.get();

    this.environment = this.environment.pop();

    return value;
  }

  private MegObject applyFunction(Colon func, MegObject arg, MegObject environment) {
    final MegObject outer = this.environment;

    this.environment = environment;

    final MegObject returnedValue = this.doInternally(() -> {
      this.environment.setAttribute(func.arg, arg);
      return this.evaluateNode(func.formula);
    });

    this.environment = outer;

    return returnedValue;
  }

  private MegObject applyOperator(MegObject operator, MegObject left, MegObject right) {
    return operator.call(left).call(right);
  }

  private MegObject nullToEmpty(MegObject object) {
    return object == null ? new EmptyObject() : object;
  }
}
