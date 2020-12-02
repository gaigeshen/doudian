package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenRefreshException;
import me.gaigeshen.doudian.authorization.AccessTokenRefresher;
import me.gaigeshen.doudian.client.DoudianClientException;
import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 访问令牌刷新器实现
 *
 * @author gaigeshen
 */
public class AccessTokenRefresherImpl implements AccessTokenRefresher {

  private final AccessTokenDoudianClient doudianClient;

  /**
   * 创建访问令牌刷新器
   *
   * @param doudianClient 抖店访问令牌客户端不能为空
   */
  public AccessTokenRefresherImpl(AccessTokenDoudianClient doudianClient) {
    Asserts.notNull(doudianClient, "doudianClient");
    this.doudianClient = doudianClient;
  }

  @Override
  public AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    AccessTokenData data;
    try {
      data = doudianClient.refreshAccessToken(oldAccessToken.getRefreshToken());
    } catch (DoudianClientException e) {
      throw new AccessTokenRefreshException("Could not refresh access token with old access token " + oldAccessToken,  e)
              .setCurrentAccessToken(oldAccessToken)
              .setCanRetry(true);
    }
    if (StringUtils.isAnyBlank(data.accessToken, data.refreshToken, data.shopId)
            || Objects.isNull(data.expiresIn)) {
      throw new AccessTokenRefreshException("Could not refresh valid access token with old access token " + oldAccessToken)
              .setCurrentAccessToken(oldAccessToken)
              .setCanRetry(true);
    }
    return AccessToken.builder()
            .setAccessToken(data.accessToken).setRefreshToken(data.refreshToken).setScope(data.scope)
            .setShopId(data.shopId).setShopName(data.shopName)
            .setExpiresIn(data.expiresIn).setExpiresTimestamp(System.currentTimeMillis() / 1000 + data.expiresIn)
            .setUpdateTime(new Date())
            .build();
  }
}
