package me.gaigeshen.doudian.authorization;

/**
 * @author gaigeshen
 */
public class AccessTokenManagerException extends AccessTokenException {
  public AccessTokenManagerException(String message) {
    super(message);
  }
  public AccessTokenManagerException(String message, Throwable cause) {
    super(message, cause);
  }
}
