package me.gaigeshen.doudian.request;

/**
 * Exception about request executor listener
 *
 * @author gaigeshen
 */
public class RequestExecutorListenerException extends RequestExecutorException {
  public RequestExecutorListenerException(String message) {
    super(message);
  }
  public RequestExecutorListenerException(String message, Throwable cause) {
    super(message, cause);
  }
}
