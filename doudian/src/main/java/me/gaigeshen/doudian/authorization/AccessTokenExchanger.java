package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌换取器
 *
 * @author gaigeshen
 */
public interface AccessTokenExchanger {
  /**
   * 通过授权码获取访问令牌
   *
   * @param authorizationCode 授权码
   * @return 访问令牌
   * @throws AccessTokenExchangeException 无法获取访问令牌
   */
  default AccessToken get(String authorizationCode) throws AccessTokenExchangeException {
    throw new AccessTokenExchangeException("Please override this method to get access token");
  }

  /**
   * 刷新访问令牌
   *
   * @param oldAccessToken 旧的访问令牌不能为空
   * @return 新的访问令牌
   * @throws AccessTokenRefreshException 刷新访问令牌失败
   */
  default AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    AccessTokenRefreshException exception = new AccessTokenRefreshException("Please override this method to refresh access token");
    exception.setCurrentAccessToken(oldAccessToken).setCanRetry(false);
    throw exception;
  }

}
