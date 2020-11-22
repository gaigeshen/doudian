package me.gaigeshen.doudian.authorization;

import java.util.Objects;

/**
 * 访问令牌更新异常
 *
 * @author gaigeshen
 */
public class AccessTokenUpdateException extends AccessTokenException {

  private AccessToken currentAccessToken;

  public AccessTokenUpdateException(String message) {
    super(message);
  }
  public AccessTokenUpdateException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 设置当前的访问令牌，可以视同为更新失败的时候当前的访问令牌
   *
   * @param currentAccessToken 当前的访问令牌
   * @return 返回当前的异常
   */
  public AccessTokenUpdateException setCurrentAccessToken(AccessToken currentAccessToken) {
    this.currentAccessToken = currentAccessToken;
    return this;
  }

  /**
   * 返回当前的访问令牌
   *
   * @return 当前的访问令牌
   */
  public AccessToken getCurrentAccessToken() {
    return currentAccessToken;
  }

  /**
   * 返回是否存在当前的访问令牌
   *
   * @return 是否存在当前的访问令牌
   */
  public boolean hasCurrentAccessToken() {
    return Objects.nonNull(currentAccessToken);
  }
}
