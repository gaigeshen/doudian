package me.gaigeshen.doudian.request.result.parser;

import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.result.InputStreamResult;
import me.gaigeshen.doudian.request.result.RawBytesResult;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.StringResult;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Abstract result parser, this class process result classes below, sub classes process other result classes
 * <ul>
 *   <li>{@link RawBytesResult}.class</li>
 *   <li>{@link StringResult}.class</li>
 *   <li>{@link InputStreamResult}.class</li>
 * </ul>
 *
 * @author gaigeshen
 */
public abstract class AbstractResultParser implements ResultParser {

  protected static final String DEFAULT_CHARSET = "utf-8";

  protected static final String TYPE_TEXT = "text/plain";

  protected static final String TYPE_JSON = "application/json";

  protected static final String TYPE_BINARY = "application/octet-stream";

  /**
   * Returns default charset, {@link AbstractResultParser#DEFAULT_CHARSET}, can be overriden in a subclass, cannot be null or blank
   *
   * @return Default charset
   */
  @Override
  public String getCharset() {
    return DEFAULT_CHARSET;
  }

  // Parse RawBytesResult & StringResult & InputStreamResult
  // Sub classes parse other result classes
  @SuppressWarnings("unchecked")
  @Override
  public final <R extends Result> R parse(ResponseContent responseContent, Class<R> resultClass) throws ResultParserException {
    String type = responseContent.getType();
    // Support all response content
    if (RawBytesResult.class == resultClass) {
      return (R) new RawBytesResult(responseContent.getRawBytes());
    }
    // Support text and json response content
    if (StringResult.class == resultClass) {
      if (!isTextResponseContent(type) && !isJsonResponseContent(type)) {
        throw new ResultParserException("Could not parse to string result, can only support text or json type, but type is " + type);
      }
      Charset charset = responseContent.getCharset();
      if (Objects.isNull(charset)) {
        String charsetName = getCharset();
        if (StringUtils.isBlank(charsetName)) {
          throw new ResultParserException("Please configure charset of this result parser " + this);
        }
        charset = Charset.forName(charsetName);
      }
      return (R) new StringResult(responseContent.getAsString(charset));
    }
    // Support binary response content
    if (InputStreamResult.class == resultClass) {
      if (!isBinaryResponseContent(type)) {
        throw new ResultParserException("Could not parse to input stream result, can only support binary type, but type is " + type);
      }
      return (R) new InputStreamResult(responseContent.getAsStream());
    }
    // Other result classes
    return parseInternal(responseContent, resultClass);
  }

  /**
   * Parse to result from response content
   *
   * @param <R> Result type
   * @param responseContent The response content cannot be null
   * @param resultClass The result class cannot be null, and cannot be {@link RawBytesResult}.class, {@link StringResult}.class or {@link InputStreamResult}.class
   * @return The result
   * @throws ResultParserException Could not parse
   */
  protected abstract <R extends Result> R parseInternal(ResponseContent responseContent, Class<R> resultClass) throws ResultParserException;

  /**
   * Check the {@link ResponseContent}'s type
   *
   * @param responseContentType The {@link ResponseContent}'s type
   * @return Returns {@code true} if the type is {@link AbstractResultParser#TYPE_TEXT}
   */
  protected boolean isTextResponseContent(String responseContentType) {
    return TYPE_TEXT.equals(responseContentType);
  }

  /**
   * Check the {@link ResponseContent}'s type
   *
   * @param responseContentType The {@link ResponseContent}'s type
   * @return Returns {@code true} if the type is {@link AbstractResultParser#TYPE_JSON}
   */
  protected boolean isJsonResponseContent(String responseContentType) {
    return TYPE_JSON.equals(responseContentType);
  }

  /**
   * Check the {@link ResponseContent}'s type
   *
   * @param responseContentType The {@link ResponseContent}'s type
   * @return Returns {@code true} if the type is {@link AbstractResultParser#TYPE_BINARY}
   */
  protected boolean isBinaryResponseContent(String responseContentType) {
    return TYPE_BINARY.equals(responseContentType);
  }

}
