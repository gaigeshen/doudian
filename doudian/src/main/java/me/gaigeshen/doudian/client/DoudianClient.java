package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.result.ResultData;

import java.io.Closeable;
import java.io.IOException;

/**
 * 抖店远程服务请求客户端
 *
 * @author gaigeshen
 */
public interface DoudianClient extends Closeable {
  /**
   * 返回此客户端使用的应用配置
   *
   * @return 应用配置，不为空
   */
  AppConfig getAppConfig();

  /**
   * 返回此客户端使用的访问令牌存储器
   *
   * @return 访问令牌存储器，不为空
   */
  AccessTokenStore getAccessTokenStore();

  /**
   * 返回此客户端使用的请求执行器
   *
   * @return 请求执行器，不为空
   */
  RequestExecutor getRequestExecutor();

  /**
   * 执行远程服务请求
   *
   * @param paramsSource 抖店请求参数来源不能为空
   * @param method 该抖店请求参数对应的远程服务名称，不能为空
   * @param shopId 店铺编号不能为空
   * @param <D> 成功执行请求之后返回的抖店数据内容的类型
   * @return 抖店数据内容
   * @throws DoudianExecutionException 无法执行远程服务请求
   * @throws DoudianExecutionResultException 成功执行远程服务请求，但是业务结果失败
   * @throws DoudianExecutionMissingAccessTokenException 该店铺没有访问令牌
   */
  <D extends ResultData> D execute(DoudianParamsSource paramsSource, String method, String shopId) throws DoudianExecutionException;

  /**
   * 执行远程服务请求
   *
   * @param params 抖店请求参数不能为空
   * @param shopId 店铺编号不能为空
   * @param <D> 成功执行请求之后返回的抖店数据内容的类型
   * @return 抖店数据内容
   * @throws DoudianExecutionException 无法执行远程服务请求
   * @throws DoudianExecutionResultException 成功执行远程服务请求，但是业务结果失败
   * @throws DoudianExecutionMissingAccessTokenException 该店铺没有访问令牌
   */
  <D extends ResultData> D execute(DoudianParams params, String shopId) throws DoudianExecutionException;

  /**
   * 执行远程服务请求，具体的使用请求执行器的细节由调用者决定，此方法无须重写
   *
   * @param execution 调用者提供此接口的实现，来自定义请求执行器的使用细节
   * @param <D> 成功执行请求之后返回的抖店数据内容的类型
   * @return 抖店数据内容
   * @throws DoudianExecutionException 无法执行远程服务请求
   * @throws DoudianExecutionResultException 成功执行远程服务请求，但是业务结果失败
   */
  default <D extends ResultData> D execute(Execution<D> execution) throws DoudianExecutionException {
    return execution.execute(getRequestExecutor());
  }

  /**
   * 关闭此客户端，调用此方法后，将无法调用执行远程服务请求
   *
   * @throws IOException 关闭的时候发生异常
   */
  @Override
  void close() throws IOException;

  /**
   * 请求执行器使用细节自定义
   *
   * @param <D> 成功执行请求之后返回的抖店数据内容的类型
   */
  interface Execution<D extends ResultData> {
    /**
     * 实现此方法，决定如何使用该请求执行器，并返回抖店数据内容
     *
     * @param executor 请求执行器不为空
     * @return 抖店数据内容
     * @throws DoudianExecutionException 无法执行远程服务请求
     * @throws DoudianExecutionResultException 成功执行远程服务请求，但是业务结果失败
     */
    D execute(RequestExecutor executor) throws DoudianExecutionException;
  }
}
