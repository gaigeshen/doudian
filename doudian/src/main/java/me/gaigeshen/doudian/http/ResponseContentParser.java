package me.gaigeshen.doudian.http;

/**
 * Parse to object from {@link ResponseContent}
 *
 * @author gaigeshen
 */
public interface ResponseContentParser<R> {

  /**
   * Parse method
   *
   * @param rContent The {@link ResponseContent}
   * @return Parsed object
   * @throws ResponseContentParserException Could not parse to object
   */
  R parse(ResponseContent rContent) throws ResponseContentParserException;

}
