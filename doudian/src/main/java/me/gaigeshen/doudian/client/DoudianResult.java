package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 *
 * @author gaigeshen
 */
public class DoudianResult<D> implements AbstractResult<D> {

  public Integer errNo;

  public String message;

  public D data;

  @Override
  public Integer getCode() {
    return errNo;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public D getData() {
    return data;
  }
}
