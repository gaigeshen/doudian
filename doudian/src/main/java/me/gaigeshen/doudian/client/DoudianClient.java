package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessToken;

import java.io.Closeable;

/**
 * 抖店开放平台接口，此接口仅包含授权相关的方法
 *
 * @author gaigeshen
 */
public interface DoudianClient extends Closeable {
  /**
   * 返回授权链接地址，授权者访问此地址开始授权过程
   *
   * @param redirectUri 授权回调链接地址，授权者将被重定向到此地址并携带授权码
   * @return 授权链接地址
   */
  String getAuthorizeUrl(String redirectUri);

  /**
   * 通过授权码获取访问令牌
   *
   * @param authorizationCode 授权码不能为空
   * @return 访问令牌不能为空
   */
  AccessToken getAccessToken(String authorizationCode);

  /**
   * 通过旧的访问令牌刷新访问令牌
   *
   * @param oldAccessToken 旧的访问令牌
   * @return 新的访问令牌不能为空
   */
  AccessToken getAccessToken(AccessToken oldAccessToken);
}
