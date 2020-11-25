package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌存储器异常
 *
 * @author gaigeshen
 */
public class AccessTokenStoreException extends AccessTokenException {
  public AccessTokenStoreException(String message) {
    super(message);
  }
  public AccessTokenStoreException(String message, Throwable cause) {
    super(message, cause);
  }
}
