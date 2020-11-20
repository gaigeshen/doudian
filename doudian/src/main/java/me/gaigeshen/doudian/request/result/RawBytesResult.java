package me.gaigeshen.doudian.request.result;

import me.gaigeshen.doudian.util.Asserts;

/**
 * Result holds raw bytes from response content, all response content types supported
 *
 * @author gaigeshen
 */
public class RawBytesResult implements Result {

  private final byte[] rawBytes;

  /**
   * Create result with raw bytes
   *
   * @param rawBytes The raw bytes cannot be null
   */
  public RawBytesResult(byte[] rawBytes) {
    this.rawBytes = Asserts.notNull(rawBytes, "rawBytes");
  }

  /**
   * Returns raw bytes
   *
   * @return The raw bytes
   */
  public byte[] getRawBytes() {
    return rawBytes;
  }
}
