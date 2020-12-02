package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

import java.util.Objects;

/**
 * 抖店客户端请求执行异常
 *
 * @author gaigeshen
 */
public class ExecutionException extends DoudianClientException {

  private Content<?> content;

  private Result result;

  public ExecutionException(String message) {
    super(message);
  }
  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
  public ExecutionException(Throwable cause) {
    super(cause);
  }

  /**
   * 设置请求数据内容
   *
   * @param content 请求数据内容
   * @return 此异常对象
   */
  public ExecutionException setContent(Content<?> content) {
    this.content = content;
    return this;
  }

  /**
   * 设置请求执行结果
   *
   * @param result 请求执行结果
   * @return 此异常对象
   */
  public ExecutionException setResult(Result result) {
    this.result = result;
    return this;
  }

  /**
   * 返回设置的请求数据内容
   *
   * @return 请求数据内容，可能为空
   */
  public Content<?> getContent() {
    return content;
  }

  /**
   * 返回设置的请求执行结果
   *
   * @return 请求执行结果，可能为空
   */
  public Result getResult() {
    return result;
  }

  /**
   * 返回是否已设置请求数据内容
   *
   * @return 是否已设置请求数据内容
   */
  public boolean hasContent() {
    return Objects.nonNull(content);
  }

  /**
   * 返回是否已设置请求执行结果
   *
   * @return 请求执行结果
   */
  public boolean hasResult() {
    return Objects.nonNull(result);
  }
}
