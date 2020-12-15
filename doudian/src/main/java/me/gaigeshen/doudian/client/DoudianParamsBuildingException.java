package me.gaigeshen.doudian.client;

/**
 * 构建抖店请求参数失败
 *
 * @author gaigeshen
 */
public class DoudianParamsBuildingException extends DoudianExecutionException {
  public DoudianParamsBuildingException(String message) {
    super(message);
  }
  public DoudianParamsBuildingException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianParamsBuildingException(Throwable cause) {
    super(cause);
  }
}
