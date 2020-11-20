package me.gaigeshen.doudian.request.result.parser;

import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.result.InputStreamResult;
import me.gaigeshen.doudian.request.result.RawBytesResult;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.StringResult;

/**
 * Result parser, parse {@link ResponseContent} to {@link Result}
 *
 * @author gaigeshen
 */
public interface ResultParser {
  /**
   * Returns charset, if {@link ResponseContent}'s charset is null, then use this value instead
   *
   * @return Charset, like 'utf-8', cannot be null or blank
   */
  String getCharset();

  /**
   * Returns {@code true} if this parser supports {@link ResponseContent} of this type
   *
   * @param responseContentType The type of {@link ResponseContent}
   * @return Boolean value
   */
  boolean supports(String responseContentType);

  /**
   * Parse method, result class can be {@link RawBytesResult}.class, {@link StringResult}.class, {@link InputStreamResult}.class or other result class
   *
   * @param responseContent The {@link ResponseContent} to be parsed, cannot be null
   * @param resultClass Target result class, cannot be null
   * @param <R> Result type
   * @return The result
   * @throws ResultParserException Could not parse
   */
  <R extends Result> R parse(ResponseContent responseContent, Class<R> resultClass) throws ResultParserException;

}
