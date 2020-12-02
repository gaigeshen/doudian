package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AbstractAuthorizationFlow;
import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenManager;
import me.gaigeshen.doudian.authorization.AuthorizationException;
import me.gaigeshen.doudian.client.DoudianClientException;
import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 授权流程处理的实现
 *
 * @author gaigeshen
 */
public class AuthorizationFlowImpl extends AbstractAuthorizationFlow {

  private final AccessTokenManager accessTokenManager;

  private final AccessTokenDoudianClient doudianClient;

  /**
   * 创建授权流程处理
   *
   * @param accessTokenManager 访问令牌管理器不能为空
   * @param doudianClient 抖店访问令牌客户端不能为空
   */
  public AuthorizationFlowImpl(AccessTokenManager accessTokenManager, AccessTokenDoudianClient doudianClient) {
    Asserts.notNull(accessTokenManager, "accessTokenManager");
    Asserts.notNull(doudianClient, "doudianClient");
    this.accessTokenManager = accessTokenManager;
    this.doudianClient = doudianClient;
  }

  @Override
  protected AccessTokenManager getAccessTokenManager() {
    return accessTokenManager;
  }

  @Override
  protected AccessToken getAccessToken(String authorizationCode) throws AuthorizationException {
    AccessTokenData data;
    try {
      data = doudianClient.getAccessToken(authorizationCode);
    } catch (DoudianClientException e) {
      throw new AuthorizationException("Could not get access token with authorization code " + authorizationCode, e);
    }
    if (StringUtils.isAnyBlank(data.accessToken, data.refreshToken, data.shopId)
            || Objects.isNull(data.expiresIn)) {
      throw new AuthorizationException("Could not get valid access token with authorization code " + authorizationCode);
    }
    return AccessToken.builder()
            .setAccessToken(data.accessToken).setRefreshToken(data.refreshToken).setScope(data.scope)
            .setShopId(data.shopId).setShopName(data.shopName)
            .setExpiresIn(data.expiresIn).setExpiresTimestamp(System.currentTimeMillis() / 1000 + data.expiresIn)
            .setUpdateTime(new Date())
            .build();
  }

  @Override
  public String getAuthorizeUrl() {
    return doudianClient.getAuthorizeUrl();
  }
}
