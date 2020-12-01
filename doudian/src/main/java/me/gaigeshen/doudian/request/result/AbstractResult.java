package me.gaigeshen.doudian.request.result;

/**
 * Abstract result include code, message and data fields
 *
 * @author gaigeshen
 */
public interface AbstractResult<D> extends Result {
  /**
   * Default success code
   */
  Integer DEFAULT_SUCCESS_CODE = 0;

  /**
   * Returns {@code true} if the code equals {@link #DEFAULT_SUCCESS_CODE}
   *
   * @return Returns {@code true} if this result is successful
   */
  default boolean successful() {
    return DEFAULT_SUCCESS_CODE.equals(getCode());
  }

  /**
   * Returns {@code true} if the code not equals {@link #DEFAULT_SUCCESS_CODE}
   *
   * @return Returns {@code true} if this result is failed
   */
  default boolean failed() {
    return !successful();
  }

  /**
   * Returns code
   *
   * @return The code
   */
  Integer getCode();

  /**
   * Returns message
   *
   * @return The message
   */
  String getMessage();

  /**
   * Returns data
   *
   * @return The data
   */
  D getData();

}
