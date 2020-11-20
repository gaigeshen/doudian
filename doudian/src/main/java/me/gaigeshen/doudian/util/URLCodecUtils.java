package me.gaigeshen.doudian.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL encode or decode utils
 *
 * @author gaigeshen
 */
public class URLCodecUtils {

  private URLCodecUtils() {}

  /**
   * Encode url with charset
   *
   * @param url The url
   * @param charset The charset
   * @return Encoded url
   */
  public static String encode(String url, String charset) {
    Asserts.notBlank(url, "url");
    Asserts.notBlank(charset, "charset");
    try {
      return URLEncoder.encode(url, charset);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Could not encode " + url + " with charset " + charset, e);
    }
  }

  /**
   * Decode url with charset
   *
   * @param url The url
   * @param charset The charset
   * @return Decoded url
   */
  public static String decode(String url, String charset) {
    Asserts.notBlank(url, "url");
    Asserts.notBlank(charset, "charset");
    try {
      return URLDecoder.decode(url, charset);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Could not decode " + url + " with charset " + charset, e);
    }
  }
}
