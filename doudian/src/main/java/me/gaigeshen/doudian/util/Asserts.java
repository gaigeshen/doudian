package me.gaigeshen.doudian.util;

import java.util.Collection;
import java.util.Objects;

/**
 * Assert utils
 *
 * @author gaigeshen
 */
public class Asserts {

  private Asserts() { }

  /**
   * Asserts argument not null
   *
   * @param argument Argument
   * @param name The name of argument
   * @param <T> Argument type
   * @return The argument
   */
  public static <T> T notNull(T argument, String name) {
    if (Objects.isNull(argument)) {
      throw new IllegalArgumentException(name + " cannot be null");
    }
    return argument;
  }

  /**
   * Asserts collection argument not empty
   *
   * @param argument Argument
   * @param name The name of argument
   * @param <E> Object type of collection items
   * @param <T> Argument type
   * @return The argument
   */
  public static <E, T extends Collection<E>> T notEmpty(T argument, String name) {
    if (Objects.isNull(argument)) {
      throw new IllegalArgumentException(name + " cannot be null");
    }
    if (argument.isEmpty()) {
      throw new IllegalArgumentException(name + " cannot be empty");
    }
    return argument;
  }

  /**
   * Asserts char sequence argument not blank
   *
   * @param argument Argument
   * @param name The name of argument
   * @param <T>  Argument type
   * @return The argument
   */
  public static <T extends CharSequence> T notBlank(T argument, String name) {
    if (Objects.nonNull(argument)) {
      for (int i = 0; i < argument.length(); i++) {
        if (!Character.isWhitespace(argument.charAt(i))) {
          return argument;
        }
      }
    }
    throw new IllegalArgumentException(name + " cannot be blank");
  }

  /**
   * Asserts int argument not negative
   *
   * @param n Argument
   * @param name The name of argument
   * @return The argument
   */
  public static int notNegative(int n, String name) {
    if (n < 0) {
      throw new IllegalArgumentException(name + " cannot be negative");
    }
    return n;
  }

  /**
   * Asserts long argument not negative
   *
   * @param n Argument
   * @param name The name of argument
   * @return The argument
   */
  public static long notNegative(long n, String name) {
    if (n < 0) {
      throw new IllegalArgumentException(name + " cannot be negative");
    }
    return n;
  }

  /**
   * Asserts int argument positive
   *
   * @param n Argument
   * @param name The name of argument
   * @return The argument
   */
  public static int positive(int n, String name) {
    if (n <= 0) {
      throw new IllegalArgumentException(name + " cannot be negative or zero");
    }
    return n;
  }

  /**
   * Asserts long argument positive
   *
   * @param n Argument
   * @param name The name of argument
   * @return The argument
   */
  public static long positive(long n, String name) {
    if (n <= 0) {
      throw new IllegalArgumentException(name + " cannot be negative or zero");
    }
    return n;
  }
}
