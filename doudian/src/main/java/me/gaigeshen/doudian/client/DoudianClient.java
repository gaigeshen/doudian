package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.WebClientConfig;

/**
 * 抖店客户端，可以用于访问抖店远程服务，依赖请求执行器
 *
 * @author gaigeshen
 */
public interface DoudianClient {
  /**
   * 设置应用配置
   *
   * @param appConfig 应用配置
   */
  void setAppConfig(AppConfig appConfig);

  /**
   * 设置访问令牌存储器
   *
   * @param accessTokenStore 访问令牌存储器
   */
  void setAccessTokenStore(AccessTokenStore accessTokenStore);

  /**
   * 设置请求执行器的配置
   *
   * @param webClientConfig 请求执行器的配置
   */
  void setWebClientConfig(WebClientConfig webClientConfig);

  /**
   * 调用此方法将检查当前所有的配置是否正确，然后做相关的初始化
   *
   * @throws DoudianClientException 有任何异常抛出
   */
  void init() throws DoudianClientException;

  /**
   * 关闭方法，释放所有的在用资源
   *
   * @throws DoudianClientException 有任何异常抛出
   */
  void dispose() throws DoudianClientException;
}
