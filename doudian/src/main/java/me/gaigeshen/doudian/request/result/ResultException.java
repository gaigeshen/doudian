package me.gaigeshen.doudian.request.result;

/**
 * Exception about result
 *
 * @author gaigeshen
 */
public class ResultException extends Exception {
  public ResultException(String message) {
    super(message);
  }
  public ResultException(String message, Throwable cause) {
    super(message, cause);
  }
}
