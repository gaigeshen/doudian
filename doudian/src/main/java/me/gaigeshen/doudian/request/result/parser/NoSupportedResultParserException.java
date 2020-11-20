package me.gaigeshen.doudian.request.result.parser;

/**
 * No supported result parser exception
 *
 * @author gaigeshen
 */
public class NoSupportedResultParserException extends ResultParserException {
  public NoSupportedResultParserException(String message) {
    super(message);
  }
  public NoSupportedResultParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
