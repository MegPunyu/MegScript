package tokyo.meg.script.lexer.chars;

import java.util.*;

public final class Chars {

  private static final Set<Character> operators = new HashSet<>();
  private static final Set<Character> reserved = new HashSet<>();

  static {
    for (final char ch : "!%&*+-/<=>^`|~".toCharArray()) {
      Chars.operators.add(ch);
    }

    for (final char ch : "\0\"#$'(),.:;?@[\\]{}".toCharArray()) {
      Chars.reserved.add(ch);
    }
  }

  public static boolean isOperator(char c) {
    return Chars.operators.contains(c);
  }

  public static boolean isReserved(char c) {
    return Chars.reserved.contains(c);
  }
}
