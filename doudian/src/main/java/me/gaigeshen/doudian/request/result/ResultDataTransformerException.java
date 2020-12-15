package me.gaigeshen.doudian.request.result;

/**
 * Could not transform {@link ResultData}
 *
 * @author gaigeshen
 */
public class ResultDataTransformerException extends ResultException {

  private ResultData resultData;

  private Class<?> targetClass;

  public ResultDataTransformerException(String message) {
    super(message);
  }
  public ResultDataTransformerException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Returns result data, maybe null
   *
   * @return Result data
   */
  public ResultData getResultData() {
    return resultData;
  }

  /**
   * Set result data
   *
   * @param resultData Result data
   * @return This exception object
   */
  public ResultDataTransformerException setResultData(ResultData resultData) {
    this.resultData = resultData;
    return this;
  }

  /**
   * Returns transform target class, maybe null
   *
   * @return Target class
   */
  public Class<?> getTargetClass() {
    return targetClass;
  }

  /**
   * Set transform target class
   *
   * @param targetClass Target class
   * @return This exception object
   */
  public ResultDataTransformerException setTargetClass(Class<?> targetClass) {
    this.targetClass = targetClass;
    return this;
  }
}
