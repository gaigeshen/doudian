package me.gaigeshen.doudian.authorization.request;

import lombok.Setter;
import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * @author gaigeshen
 */
@Setter
public class AccessTokenResult implements AbstractResult<AccessTokenData> {

  private int errNo;

  private String message;

  private AccessTokenData data;

  @Override
  public int getCode() {
    return errNo;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public AccessTokenData getData() {
    return data;
  }
}
