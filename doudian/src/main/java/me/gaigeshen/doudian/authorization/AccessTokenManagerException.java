package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌管理器异常
 *
 * @author gaigeshen
 */
public class AccessTokenManagerException extends AccessTokenException {
  public AccessTokenManagerException(String message) {
    super(message);
  }
  public AccessTokenManagerException(Throwable cause) {
    super(cause);
  }
  public AccessTokenManagerException(String message, Throwable cause) {
    super(message, cause);
  }
}
