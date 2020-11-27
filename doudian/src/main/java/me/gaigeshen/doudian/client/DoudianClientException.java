package me.gaigeshen.doudian.client;

/**
 * @author gaigeshen
 */
public class DoudianClientException extends RuntimeException {
  public DoudianClientException(String message) {
    super(message);
  }
  public DoudianClientException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianClientException(Throwable cause) {
    super(cause);
  }
}
