package me.gaigeshen.doudian.request.content;

/**
 * Content metadata exception
 *
 * @author gaigeshen
 */
public class ContentMetadataException extends ContentException {
  public ContentMetadataException(String message) {
    super(message);
  }
  public ContentMetadataException(String message, Throwable cause) {
    super(message, cause);
  }
}
