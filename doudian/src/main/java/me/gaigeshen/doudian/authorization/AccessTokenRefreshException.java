package me.gaigeshen.doudian.authorization;

/**
 * @author gaigeshen
 */
public class AccessTokenRefreshException extends AccessTokenUpdateException {
  public AccessTokenRefreshException(String message) {
    super(message);
  }
  public AccessTokenRefreshException(Throwable cause) {
    super(cause);
  }
  public AccessTokenRefreshException(String message, Throwable cause) {
    super(message, cause);
  }
}
