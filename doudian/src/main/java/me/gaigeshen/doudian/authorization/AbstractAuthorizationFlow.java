package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 抽象的授权流程处理，通过授权码获取到访问令牌之后，将该访问令牌添加到访问令牌管理器，子类关注如何通过授权码获取访问令牌
 *
 * @author gaigeshen
 */
public abstract class AbstractAuthorizationFlow implements AuthorizationFlow {

  private final String authorizeUrl;

  private final AccessTokenManager accessTokenManager;

  /**
   * 创建此类对象
   *
   * @param authorizeUrl 完整的授权链接
   * @param accessTokenManager 访问令牌管理器，只用于添加获取到的访问令牌
   */
  protected AbstractAuthorizationFlow(String authorizeUrl, AccessTokenManager accessTokenManager) {
    Asserts.notBlank(authorizeUrl, "authorizeUrl");
    Asserts.notNull(accessTokenManager, "accessTokenManager");
    this.authorizeUrl = authorizeUrl;
    this.accessTokenManager = accessTokenManager;
  }

  @Override
  public final String getAuthorizeUrl() {
    return authorizeUrl;
  }

  @Override
  public final void processAuthorizationCode(String authorizationCode) throws AuthorizationException {
    if (StringUtils.isBlank(authorizationCode)) {
      throw new AuthorizationException("Could not process blank or null authorization code");
    }
    AccessToken accessToken = getAccessToken(authorizationCode);
    if (Objects.isNull(accessToken)) {
      throw new AuthorizationException("Could not get access token with authorization code " + authorizationCode);
    }
    try {
      accessTokenManager.addNewAccessToken(accessToken);
    } catch (AccessTokenManagerException | InvalidAccessTokenException e) {
      throw new AuthorizationException("Could not process authorization code " + authorizationCode, e);
    }
  }

  /**
   * 实现此方法用于通过授权码获取访问令牌
   *
   * @param authorizationCode 授权码不能为空
   * @return 访问令牌不能为空
   * @throws AuthorizationException 获取访问令牌失败
   */
  protected abstract AccessToken getAccessToken(String authorizationCode) throws AuthorizationException;
}
