package me.gaigeshen.doudian.request.result;

import me.gaigeshen.doudian.util.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Result holds input stream, response content type can only support binary
 *
 * @author gaigeshen
 */
public class InputStreamResult implements Result {

  private final InputStream inputStream;

  /**
   * Create result with input stream
   *
   * @param inputStream The input stream cannot be null
   */
  public InputStreamResult(InputStream inputStream) {
    this.inputStream = Asserts.notNull(inputStream, "inputStream");
  }

  /**
   * Returns input stream
   *
   * @return The input stream
   */
  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * Write to output stream
   *
   * @param out Output stream
   * @throws IOException Write failed
   */
  public void write(OutputStream out) throws IOException {
    try (InputStream in = getInputStream()) {
      int len;
      byte[] buffer = new byte[4096];
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
    }
  }
}
