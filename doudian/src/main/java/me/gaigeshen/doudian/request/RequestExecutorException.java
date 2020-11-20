package me.gaigeshen.doudian.request;

/**
 * Request executor exception
 *
 * @author gaigeshen
 */
public class RequestExecutorException extends Exception {
  public RequestExecutorException(String message) {
    super(message);
  }
  public RequestExecutorException(String message, Throwable cause) {
    super(message, cause);
  }
}
