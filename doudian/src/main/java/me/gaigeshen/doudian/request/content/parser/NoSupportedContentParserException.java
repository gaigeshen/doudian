package me.gaigeshen.doudian.request.content.parser;

/**
 * No supported content parser exception
 *
 * @author gaigeshen
 */
public class NoSupportedContentParserException extends ContentParserException {
  public NoSupportedContentParserException(String message) {
    super(message);
  }
  public NoSupportedContentParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
