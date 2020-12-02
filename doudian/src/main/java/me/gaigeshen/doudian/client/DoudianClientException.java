package me.gaigeshen.doudian.client;

/**
 * 抖店客户端异常
 *
 * @author gaigeshen
 */
public class DoudianClientException extends Exception {
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
