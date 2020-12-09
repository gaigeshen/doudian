package me.gaigeshen.doudian.http;

/**
 * The exception about {@link ResponseContentParser}
 *
 * @author gaigeshen
 */
public class ResponseContentParserException extends Exception {

  private ResponseContent responseContent;

  public ResponseContentParserException(String message) {
    super(message);
  }
  public ResponseContentParserException(String message, Throwable cause) {
    super(message, cause);
  }
  public ResponseContentParserException(Throwable cause) {
    super(cause);
  }

  public ResponseContentParserException setResponseContent(ResponseContent responseContent) {
    this.responseContent = responseContent;
    return this;
  }

  public ResponseContent getResponseContent() {
    return responseContent;
  }
}
