package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static me.gaigeshen.doudian.config.Constants.*;

/**
 * 访问令牌帮助类
 *
 * @author gaigeshen
 */
public class AccessTokenHelper {

  private AccessTokenHelper() { }

  /**
   * 返回该访问令牌是否已经过期
   *
   * @param accessToken 访问令牌不可为空
   * @return 是否已经过期
   */
  public static boolean isExpired(AccessToken accessToken) {
    if (Objects.isNull(accessToken)) {
      throw new IllegalArgumentException("accessToken cannot be null");
    }
    return accessToken.getExpiresTimestamp() > System.currentTimeMillis() / 1000;
  }

  /**
   * 返回访问令牌是否有效，有效的访问令牌必需包含访问令牌值、刷新令牌值和店铺编号
   *
   * @param accessToken 访问令牌不可为空
   * @return 是否有效
   */
  public static boolean isValid(AccessToken accessToken) {
    if (Objects.isNull(accessToken)) {
      throw new IllegalArgumentException("accessToken cannot be null");
    }
    return !StringUtils.isAnyBlank(accessToken.getAccessToken(), accessToken.getRefreshToken(), accessToken.getShopId());
  }

  /**
   * 获取授权链接
   *
   * @param appKey 应用编号不能为空
   * @param redirectUri 回调链接不能为空
   * @param state 状态可以为空
   * @return 授权链接
   */
  public static String getAuthorizeUrl(String appKey, String redirectUri, String state) {
    Asserts.notBlank(appKey, "appKey");
    Asserts.notBlank(redirectUri, "redirectUri");
    return String.format(AUTHORIZE_TEMPLATE_URL, appKey, redirectUri, StringUtils.defaultString(state));
  }

  /**
   * 获取授权链接，默认的状态为空字符串
   *
   * @param appKey 应用编号不能为空
   * @param redirectUri 回调链接不能为空
   * @return 授权链接
   */
  public static String getAuthorizeUrl(String appKey, String redirectUri) {
    return getAuthorizeUrl(appKey, redirectUri, "");
  }

  /**
   * 获取访问令牌链接，访问此链接正常情况响应访问令牌数据内容
   *
   * @param appKey 应用编号不能为空
   * @param appSecret 应用密钥不能为空
   * @param code 授权码不能为空
   * @return 访问令牌链接
   */
  public static String getAccessTokenUrl(String appKey, String appSecret, String code) {
    Asserts.notBlank(appKey, "appKey");
    Asserts.notBlank(appSecret, "appSecret");
    Asserts.notBlank(code, "code");
    return String.format(ACCESS_TOKEN_TEMPLATE_URL, appKey, appSecret, code);
  }

  /**
   * 获取访问令牌刷新链接，访问此链接正常情况响应访问令牌数据内容
   *
   * @param appKey 应用编号不能为空
   * @param appSecret 应用密钥不能为空
   * @param refreshToken 刷新令牌不能为空
   * @return 访问令牌刷新链接
   */
  public static String getAccessTokenRefreshUrl(String appKey, String appSecret, String refreshToken) {
    Asserts.notBlank(appKey, "appKey");
    Asserts.notBlank(appSecret, "appSecret");
    Asserts.notBlank(refreshToken, "refreshToken");
    return String.format(ACCESS_TOKEN_REFRESH_TEMPLATE_URL, appKey, appSecret, refreshToken);
  }
}
