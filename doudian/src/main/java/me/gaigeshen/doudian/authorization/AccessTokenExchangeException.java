package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌换取异常
 *
 * @author gaigeshen
 */
public class AccessTokenExchangeException extends AccessTokenException {
  public AccessTokenExchangeException(Throwable cause) {
    super(cause);
  }
  public AccessTokenExchangeException(String message) {
    super(message);
  }
  public AccessTokenExchangeException(String message, Throwable cause) {
    super(message, cause);
  }
}
