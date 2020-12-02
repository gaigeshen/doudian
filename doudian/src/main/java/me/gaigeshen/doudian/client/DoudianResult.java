package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * 抖店请求执行结果
 *
 * @author gaigeshen
 */
public class DoudianResult<D> implements AbstractResult<D> {

  private Integer errNo;

  private String message;

  private D data;

  /**
   * 返回抖店请求执行结果异常码
   *
   * @return 抖店请求执行结果异常码
   */
  @Override
  public Integer getCode() {
    return errNo;
  }

  /**
   * 返回抖店请求执行结果消息
   *
   * @return 抖店请求执行结果消息
   */
  @Override
  public String getMessage() {
    return message;
  }

  /**
   * 返回抖店请求执行结果数据
   *
   * @return 抖店请求执行结果数据
   */
  @Override
  public D getData() {
    return data;
  }

  /**
   * 设置抖店请求执行结果异常码
   *
   * @param errNo 抖店请求执行结果异常码
   */
  public void setErrNo(Integer errNo) {
    this.errNo = errNo;
  }

  /**
   * 设置抖店请求执行结果消息
   *
   * @param message 抖店请求执行结果消息
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 设置抖店请求执行结果数据
   *
   * @param data 抖店请求执行结果数据
   */
  public void setData(D data) {
    this.data = data;
  }
}
