package me.gaigeshen.doudian.authorization;

/**
 * 授权异常
 *
 * @author gaigeshen
 */
public class AuthorizationException extends Exception {
  public AuthorizationException(String message) {
    super(message);
  }

  public AuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthorizationException(Throwable cause) {
    super(cause);
  }
}
