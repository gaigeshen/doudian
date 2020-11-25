package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌无效
 *
 * @author gaigeshen
 * @see AccessTokenHelper#isValid(AccessToken)
 */
public class InvalidAccessTokenException extends AccessTokenException {
  public InvalidAccessTokenException(String message) {
    super(message);
  }
  public InvalidAccessTokenException(Throwable cause) {
    super(cause);
  }
  public InvalidAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
