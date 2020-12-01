package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * Result object class contains data
 *
 * @author gaigeshen
 */
public class DoudianResult<D> implements AbstractResult<D> {

  private Integer errNo;

  private String message;

  private D data;

  /**
   * Returns result code, see {@link #setErrNo(Integer)}
   *
   * @return The result code
   */
  @Override
  public Integer getCode() {
    return errNo;
  }

  /**
   * Returns result message
   *
   * @return The result message
   */
  @Override
  public String getMessage() {
    return message;
  }

  /**
   * Returns result data
   *
   * @return The result data
   */
  @Override
  public D getData() {
    return data;
  }

  /**
   * Set result code, field name is errNo
   *
   * @param errNo The result code
   */
  public void setErrNo(Integer errNo) {
    this.errNo = errNo;
  }

  /**
   * Set result message
   *
   * @param message The result message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Set result data
   *
   * @param data The result data
   */
  public void setData(D data) {
    this.data = data;
  }
}
