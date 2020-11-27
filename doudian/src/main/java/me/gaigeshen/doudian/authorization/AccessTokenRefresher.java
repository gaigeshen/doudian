package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌刷新器
 *
 * @author gaigeshen
 */
public interface AccessTokenRefresher {
  /**
   * 刷新访问令牌
   *
   * @param oldAccessToken 旧的访问令牌不能为空
   * @return 新的访问令牌不能为空
   * @throws AccessTokenRefreshException 刷新访问令牌失败
   */
  default AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    AccessTokenRefreshException exception = new AccessTokenRefreshException("Please override this method to refresh access token");
    exception.setCurrentAccessToken(oldAccessToken).setCanRetry(false);
    throw exception;
  }

}
