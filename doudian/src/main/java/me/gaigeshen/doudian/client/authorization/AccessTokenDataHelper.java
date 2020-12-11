package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AccessToken;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 访问令牌抖店请求执行结果数据帮助类
 *
 * @author gaigeshen
 */
public class AccessTokenDataHelper {

  private AccessTokenDataHelper() { }

  /**
   * 判断访问令牌数据是否有效合法，合法的访问令牌数据至少包含失效时长、访问令牌、刷新令牌和店铺编号
   *
   * @param accessTokenData 访问令牌数据，如果传递空对象则认为是无效的访问令牌
   * @return 是否有效合法
   */
  public static boolean isValid(AccessTokenData accessTokenData) {
    return Objects.nonNull(accessTokenData)
            && Objects.nonNull(accessTokenData.expiresIn)
            && (!StringUtils.isAnyBlank(accessTokenData.accessToken, accessTokenData.refreshToken, accessTokenData.shopId));
  }

  /**
   * 将访问令牌数据转换为访问令牌对象，确保访问令牌数据是有效合法的
   *
   * @param accessTokenData 访问令牌数据不能为空，并且是合法有效的
   * @return 访问令牌对象
   */
  public static AccessToken transform(AccessTokenData accessTokenData) {
    return AccessToken.builder()
            .setAccessToken(accessTokenData.accessToken)
            .setRefreshToken(accessTokenData.refreshToken)
            .setScope(accessTokenData.scope)
            .setShopId(accessTokenData.shopId)
            .setShopName(accessTokenData.shopName)
            .setExpiresIn(accessTokenData.expiresIn)
            .setExpiresTimestamp(System.currentTimeMillis() / 1000 + accessTokenData.expiresIn)
            .setUpdateTime(new Date())
            .build();
  }
}
