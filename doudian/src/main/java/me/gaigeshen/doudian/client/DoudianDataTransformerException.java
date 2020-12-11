package me.gaigeshen.doudian.client;

/**
 * 抖店请求执行结果数据转换器异常
 *
 * @author gaigeshen
 */
public class DoudianDataTransformerException extends Exception {

  private DoudianData data;

  public DoudianDataTransformerException(String message) {
    super(message);
  }
  public DoudianDataTransformerException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianDataTransformerException(Throwable cause) {
    super(cause);
  }

  /**
   * 返回抖店请求执行结果数据
   *
   * @return 抖店请求执行结果数据可能为空
   */
  public DoudianData getData() {
    return data;
  }

  /**
   * 设置抖店请求执行结果数据
   *
   * @param data 抖店请求执行结果数据
   * @return 当前异常对象
   */
  public DoudianDataTransformerException setData(DoudianData data) {
    this.data = data;
    return this;
  }
}
