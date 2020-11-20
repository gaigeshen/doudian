package me.gaigeshen.doudian.http;

import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.Map;

/**
 * The request content
 *
 * @author gaigeshen
 */
public interface RequestContent {
  /**
   * Returns request uri, cannot be null or blank
   *
   * @return The request uri
   */
  String getUri();

  /**
   * Returns request method, same as http request method, can only support 'get' and 'post', cannot be null or blank
   *
   * @return Request method
   */
  String getMethod();

  /**
   * Returns request content type, same as http mime type, cannot be null if method is 'post'
   *
   * @return Request content type
   */
  Type getType();

  /**
   * Returns charset, like 'utf-8', cannot be blank or null if type is {@link Type#TEXT_JSON} or {@link Type#TEXT_PLAIN} or {@link Type#PARAMETERS}
   *
   * @return Encoding
   */
  String getCharset();

  /**
   * Returns request content text value, cannot be blank or null if type is {@link Type#TEXT_JSON} or {@link Type#TEXT_PLAIN}
   *
   * @return Content text value
   */
  String getText();

  /**
   * Returns request content binary value, cannot be null if type is {@link Type#BINARY}
   *
   * @return Content binary value
   */
  byte[] getBinary();

  /**
   * Returns request content input stream object, cannot be null if type is {@link Type#STREAM}
   *
   * @return Content input stream object
   */
  InputStream getStream();

  /**
   * Returns request content parameters object, cannot be null if type is {@link Type#PARAMETERS}
   *
   * @return Content parameters obejct
   */
  Map<String, String> getParameters();

  /**
   * Returns request content multipart parameters object, cannot be null if type is {@link Type#MULTIPART_PARAMETERS}
   *
   * @return Content multipart parameters object, value of this object can only support byte[], file, input stream and string value
   */
  Map<String, Object> getMultipartParameters();

  /**
   * Request content type, only support six type
   *
   * @author gaigeshen
   */
  enum Type {
    /**
     * text/plain
     */
    TEXT_PLAIN("text/plain"),
    /**
     * application/json
     */
    TEXT_JSON("application/json"),
    /**
     * application/octet-stream
     */
    BINARY("application/octet-stream"),
    /**
     * application/octet-stream
     */
    STREAM("application/octet-stream"),
    /**
     * application/x-www-form-urlencoded
     */
    PARAMETERS("application/x-www-form-urlencoded"),
    /**
     * multipart/form-data
     */
    MULTIPART_PARAMETERS("multipart/form-data");

    private final String value;

    Type(String name) {
      this.value = name;
    }

    /**
     * Returns name, same as http mime type
     *
     * @return The name
     */
    public String getName() {
      return value;
    }

    /**
     * Parse to content type without charset
     *
     * @return Content type
     */
    public ContentType parseContentType() {
      return ContentType.create(value);
    }

    /**
     * Parse to content type
     *
     * @param charset The charset, like 'utf-8'
     * @return Content type
     */
    public ContentType parseContentType(String charset) {
      return ContentType.create(value, charset);
    }

    @Override
    public String toString() {
      return value;
    }
  }

}
