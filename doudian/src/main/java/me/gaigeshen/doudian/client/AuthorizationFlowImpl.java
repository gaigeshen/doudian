package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AbstractAuthorizationFlow;
import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenManager;
import me.gaigeshen.doudian.authorization.AuthorizationException;
import me.gaigeshen.doudian.util.Asserts;

/**
 * 授权流程处理的实现
 *
 * @author gaigeshen
 */
public class AuthorizationFlowImpl extends AbstractAuthorizationFlow {

  private final AccessTokenManager accessTokenManager;

  private final DoudianClient doudianClient;

  private final String redirectUri;

  /**
   * 创建授权流程处理的实现
   *
   * @param accessTokenManager 访问令牌管理器不能为空
   * @param doudianClient 抖店开放平台接口不能为空
   * @param redirectUri 授权回调链接地址不能为空
   */
  public AuthorizationFlowImpl(AccessTokenManager accessTokenManager, DoudianClient doudianClient, String redirectUri) {
    Asserts.notNull(accessTokenManager, "accessTokenManager");
    Asserts.notNull(doudianClient, "doudianClient");
    Asserts.notNull(redirectUri, "redirectUri");
    this.accessTokenManager = accessTokenManager;
    this.doudianClient = doudianClient;
    this.redirectUri = redirectUri;
  }

  @Override
  protected AccessTokenManager getAccessTokenManager() {
    return accessTokenManager;
  }

  @Override
  protected AccessToken getAccessToken(String authorizationCode) throws AuthorizationException {
    return null;
  }

  @Override
  public String getAuthorizeUrl() {
    return null;
  }
}
