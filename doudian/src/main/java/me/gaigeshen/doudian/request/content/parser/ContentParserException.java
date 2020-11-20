package me.gaigeshen.doudian.request.content.parser;

import me.gaigeshen.doudian.request.content.ContentException;

/**
 * Could not parse content exception
 *
 * @author gaigeshen
 */
public class ContentParserException extends ContentException {
  public ContentParserException(String message) {
    super(message);
  }
  public ContentParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
