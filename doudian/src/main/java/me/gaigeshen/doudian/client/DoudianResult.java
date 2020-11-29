package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * 抖店请求响应结果
 *
 * @author gaigeshen
 */
public class DoudianResult<D> implements AbstractResult<D> {

  private Integer errNo; // 异常码

  private String message; // 消息内容

  private D data; // 数据部分

  @Override
  public final Integer getCode() {
    return errNo;
  }

  @Override
  public final String getMessage() {
    return message;
  }

  @Override
  public final D getData() {
    return data;
  }

  public void setErrNo(Integer errNo) {
    this.errNo = errNo;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setData(D data) {
    this.data = data;
  }
}
