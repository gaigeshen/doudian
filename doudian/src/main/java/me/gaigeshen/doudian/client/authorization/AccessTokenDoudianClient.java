package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.client.DoudianClientException;

/**
 * 抖店访问令牌客户端
 *
 * @author gaigeshen
 */
public interface AccessTokenDoudianClient {
  /**
   * 获取应用授权地址
   *
   * @return 应用授权地址不能为空
   */
  String getAuthorizeUrl();

  /**
   * 通过授权码获取访问令牌
   *
   * @param authorizationCode 授权码不能为空
   * @return 访问令牌不能为空
   * @throws DoudianClientException 请求执行异常
   */
  AccessTokenData getAccessToken(String authorizationCode) throws DoudianClientException;

  /**
   * 刷新访问令牌
   *
   * @param refreshToken 刷新令牌不能为空
   * @return 访问令牌不能为空
   * @throws DoudianClientException 请求执行异常
   */
  AccessTokenData refreshAccessToken(String refreshToken) throws DoudianClientException;
}
