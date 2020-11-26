package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.RequestExecutorException;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

import java.io.Closeable;

/**
 * 抖店接口客户端，不再使用时请调用关闭方法
 *
 * @author gaigeshen
 */
public interface DoudianClient extends Closeable {
  /**
   * 执行接口请求
   *
   * @param content 请求数据
   * @param <R> 请求响应结果类型
   * @return 请求响应结果
   * @throws RequestExecutorException 执行接口请求失败
   */
  <R extends Result> R execute(Content<R> content) throws RequestExecutorException;

  /**
   * 执行接口请求
   *
   * @param content 请求数据
   * @param accessToken 访问令牌可以为空，如果提供了此访问令牌并且该请求数据配置为需要访问令牌，则会将该访问令牌追加到请求链接上
   * @param <R> 请求响应结果类型
   * @return 请求响应结果
   * @throws RequestExecutorException 执行接口请求失败
   */
  <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException;

  /**
   * 执行接口请求
   *
   * @param content 请求数据
   * @param urlValues 请求数据配置的请求链接上的模板参数值
   * @param <R> 请求响应结果类型
   * @return 请求响应结果
   * @throws RequestExecutorException 执行接口请求失败
   */
  <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException;

  /**
   * 执行接口请求
   *
   * @param content 请求数据
   * @param accessToken 访问令牌可以为空，如果提供了此访问令牌并且该请求数据配置为需要访问令牌，则会将该访问令牌追加到请求链接上
   * @param urlValues 请求数据配置的请求链接上的模板参数值
   * @param <R> 请求响应结果类型
   * @return 请求响应结果
   * @throws RequestExecutorException 执行接口请求失败
   */
  <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException;

  /**
   * 执行接口请求，用于自定义封装请求数据和请求响应结果的时候使用
   *
   * @param requestContent 原始请求数据
   * @return 原始请求响应结果
   * @throws RequestExecutorException 执行接口请求失败
   */
  ResponseContent execute(RequestContent requestContent) throws RequestExecutorException;

}
