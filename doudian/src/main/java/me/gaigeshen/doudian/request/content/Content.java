package me.gaigeshen.doudian.request.content;

import me.gaigeshen.doudian.request.result.Result;

/**
 * Content for request content, you have to define content metadata by use of {@link MetadataAttributes}
 *
 * @author gaigeshen
 */
public interface Content<R extends Result> {
  /**
   * Returns result class
   *
   * @return Request result class from response content
   */
  Class<R> getResultClass();

}
