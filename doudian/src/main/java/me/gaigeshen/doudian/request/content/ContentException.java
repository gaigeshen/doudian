package me.gaigeshen.doudian.request.content;

/**
 * Exception about content
 *
 * @author gaigeshen
 */
public class ContentException extends Exception {
  public ContentException(String message) {
    super(message);
  }
  public ContentException(String message, Throwable cause) {
    super(message, cause);
  }
}
