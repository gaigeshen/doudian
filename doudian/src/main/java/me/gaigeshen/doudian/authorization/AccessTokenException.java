package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌相关的异常
 *
 * @author gaigeshen
 */
public class AccessTokenException extends Exception {

  public AccessTokenException(String message) {
    super(message);
  }
  public AccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
