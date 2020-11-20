package me.gaigeshen.doudian.request.content.parser;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.Metadata;

/**
 * Content parser, parse to {@link RequestContent} object
 *
 * @author gaigeshen
 */
public interface ContentParser {

  /**
   * Returns parameter name of access token, the access token parameter will be append to content {@link Metadata#getUrl()}
   *
   * @return The access token name cannot be null or blank
   */
  String getAccessTokenName();

  /**
   * Returns charset
   *
   * @return Charset, like 'utf-8', cannot be null or blank
   */
  String getCharset();

  /**
   * Returns {@code true} if this parser supports content of this metadata
   *
   * @param metadata Valid metadata, cannot be null
   * @return Boolean value
   */
  boolean supports(Metadata metadata);

  /**
   * Parse to {@link RequestContent} object from {@link Content}
   *
   * @param content The content cannot be null
   * @param metadata Valid metadata of content, cannot be null
   * @param urlValues The url template parameter values of {@link Metadata#getUrl()}
   * @return Parsed object
   * @throws ContentParserException Could not parse
   */
  default RequestContent parse(Content<?> content, Metadata metadata, Object... urlValues) throws ContentParserException {
    return parse(content, metadata, null, urlValues);
  }

  /**
   * Parse to {@link RequestContent} object from {@link Content}
   *
   * @param content The content cannot be null
   * @param metadata Valid metadata of content, cannot be null
   * @param accessToken The access token, may be null or blank
   * @param urlValues The url template parameter values of {@link Metadata#getUrl()}
   * @return Parsed object
   * @throws ContentParserException Could not parse
   */
  RequestContent parse(Content<?> content, Metadata metadata, String accessToken, Object... urlValues) throws ContentParserException;

}
