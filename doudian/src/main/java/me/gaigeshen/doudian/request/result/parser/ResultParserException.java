package me.gaigeshen.doudian.request.result.parser;

import me.gaigeshen.doudian.request.result.ResultException;

/**
 * Exception about result parser
 *
 * @author gaigeshen
 */
public class ResultParserException extends ResultException {
  public ResultParserException(String message) {
    super(message);
  }
  public ResultParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
