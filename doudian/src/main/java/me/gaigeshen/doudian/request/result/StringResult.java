package me.gaigeshen.doudian.request.result;

import me.gaigeshen.doudian.util.Asserts;

/**
 * Result holds string from response content, response content type may be text or json
 *
 * @author gaigeshen
 */
public class StringResult implements Result {

  private final String string;

  /**
   * Create result with string
   *
   * @param string The string cannot be null
   */
  public StringResult(String string) {
    this.string = Asserts.notNull(string, "string");
  }

  /**
   * Returns string
   *
   * @return The string
   */
  public String getString() {
    return string;
  }
}
