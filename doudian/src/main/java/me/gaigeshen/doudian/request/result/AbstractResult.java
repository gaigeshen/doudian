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
  int DEFAULT_SUCCESS_CODE = 0;

  /**
   * Returns {@code true} if the code equals {@link #DEFAULT_SUCCESS_CODE}
   * @return Returns {@code true} if this result is successful
   */
  default boolean isSuccessResult() {
    return getCode() == DEFAULT_SUCCESS_CODE;
  }

  /**
   * Returns code
   *
   * @return The code
   */
  int getCode();

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
