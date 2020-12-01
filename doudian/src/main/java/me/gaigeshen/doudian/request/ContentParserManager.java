package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.request.content.*;
import me.gaigeshen.doudian.request.content.parser.*;

import java.util.*;

/**
 * Content parsers manager, call parse methods to parse {@link Content} to {@link RequestContent}
 *
 * @author gaigeshen
 */
class ContentParserManager {

  private final List<ContentParser> parsers = new ArrayList<>();

  /**
   * Create {@link ContentParserManager} with {@link ContentParser}s
   *
   * @param parsers The {@link ContentParser}s can be null
   */
  public ContentParserManager(Collection<ContentParser> parsers) {
    if (Objects.nonNull(parsers)) {
      this.parsers.addAll(parsers);
    }
  }

  /**
   * Create {@link ContentParserManager} with {@link ContentParser}s
   *
   * @param parsers The {@link ContentParser}s can be null
   */
  public ContentParserManager(ContentParser... parsers) {
    this(Objects.nonNull(parsers) ? Arrays.asList(parsers) : null);
  }

  /**
   * Create default content parser manager with {@link ContentParserJsonImpl}, {@link ContentParserParametersImpl} and {@link ContentParserMultipartParametersImpl}
   *
   * @return Default content parser manager
   */
  public static ContentParserManager createDefault() {
    return new ContentParserManager(new ContentParserJsonImpl(), new ContentParserParametersImpl(), new ContentParserMultipartParametersImpl());
  }

  /**
   * Returns all {@link ContentParser}s
   *
   * @return {@link ContentParser}s
   */
  public List<ContentParser> getParsers() {
    return Collections.unmodifiableList(parsers);
  }

  /**
   * Do parse from {@link Content} to {@link RequestContent}
   *
   * @param content The content cannot be null
   * @param metadata The content metadata cannot be null
   * @param accessToken The access token may be null or blank
   * @param urlValues The url template parameter values of {@link Metadata#getUrl()}
   * @return Parsed {@link RequestContent}, cannot be null
   * @throws ContentParserException Could not do parse
   * @throws NoSupportedContentParserException If no supported content parser
   */
  public RequestContent parse(Content<?> content, Metadata metadata, String accessToken, Object... urlValues) throws ContentParserException {
    for (ContentParser parser : parsers) {
      if (parser.supports(metadata)) {
        return parser.parse(content, metadata, accessToken, urlValues);
      }
    }
    throw new NoSupportedContentParserException("No supported content parser for this content metadata " + metadata.getType());
  }

  /**
   * Do parse from {@link Content} to {@link RequestContent}
   *
   * @param content The content cannot be null
   * @param metadata The content metadata cannot be null
   * @param urlValues The url template parameter values of {@link Metadata#getUrl()}
   * @return Parsed {@link RequestContent}, cannot be null
   * @throws ContentParserException Could not do parse
   * @throws NoSupportedContentParserException If no supported content parser
   */
  public RequestContent parse(Content<?> content, Metadata metadata, Object... urlValues) throws ContentParserException {
    return parse(content, metadata, null, urlValues);
  }
}
