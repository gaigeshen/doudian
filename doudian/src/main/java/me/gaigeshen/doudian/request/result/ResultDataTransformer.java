package me.gaigeshen.doudian.request.result;

/**
 * Transform {@link ResultData} to another object
 *
 * @author gaigeshen
 */
public interface ResultDataTransformer<D extends ResultData, T> {

  /**
   * Do transform
   *
   * @param resultData The result data
   * @return Target object
   * @throws ResultDataTransformerException If cannot do transform
   */
  T transform(D resultData) throws ResultDataTransformerException;

}
