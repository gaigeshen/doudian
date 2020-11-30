package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌未找到异常
 *
 * @author gaigeshen
 */
public class AccessTokenNotFoundException extends AccessTokenException {
  public AccessTokenNotFoundException(String message) {
    super(message);
  }
  public AccessTokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  public AccessTokenNotFoundException(Throwable cause) {
    super(cause);
  }
}
