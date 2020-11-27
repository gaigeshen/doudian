package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessTokenManager;
import me.gaigeshen.doudian.authorization.AccessTokenManagerException;
import me.gaigeshen.doudian.authorization.AuthorizationFlow;
import me.gaigeshen.doudian.request.RequestExecutor;

import java.io.IOException;

/**
 * 抖店接口客户端，同时也是访问令牌管理器和请求执行器
 *
 * @author gaigeshen
 */
public interface DoudianClient extends AuthorizationFlow, AccessTokenManager, RequestExecutor {
  /**
   * 调用此方法仅仅尝试关闭访问令牌管理器的功能
   *
   * @throws AccessTokenManagerException 关闭访问令牌管理器的时候发生异常
   */
  @Override
  void shutdown() throws AccessTokenManagerException;

  /**
   * 调用此方法仅仅尝试关闭请求执行器的功能
   *
   * @throws IOException 关闭请求执行器的时候发生异常
   */
  @Override
  void close() throws IOException;

  /**
   * 调用此方法将尝试关闭访问令牌管理器的功能以及请求执行器的功能，不抛出任何异常
   */
  void shutdownAndClose();
}
