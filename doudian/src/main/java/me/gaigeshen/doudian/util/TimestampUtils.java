package me.gaigeshen.doudian.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Timestamp utils
 *
 * @author gaigeshen
 */
public class TimestampUtils {
  /**
   * Default format
   */
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private TimestampUtils() { }

  /**
   * Returns current time string use default format
   *
   * @return Current time string
   */
  public static String getCurrentTimestamp() {
    return getCurrentTimestamp(DEFAULT_FORMAT);
  }

  /**
   * Returns current time string
   *
   * @param format Use this format
   * @return Current time string
   */
  public static String getCurrentTimestamp(String format) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
  }
}
