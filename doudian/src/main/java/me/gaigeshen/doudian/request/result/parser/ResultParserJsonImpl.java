package me.gaigeshen.doudian.request.result.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Result parser, response content's type can only support json
 *
 * @author gaigeshen
 */
public class ResultParserJsonImpl extends AbstractResultParser {

  private final ObjectMapper jacksonObjectMapper;

  /**
   * Create result parser with jackson {@link ObjectMapper}
   *
   * @param jacksonObjectMapper The jackson {@link ObjectMapper} cannot be null
   */
  public ResultParserJsonImpl(ObjectMapper jacksonObjectMapper) {
    this.jacksonObjectMapper = Asserts.notNull(jacksonObjectMapper, "jacksonObjectMapper");
  }

  /**
   * Create content parser with default jackson {@link ObjectMapper}, create {@link ObjectMapper} internal
   */
  public ResultParserJsonImpl() {
    this(new ObjectMapper());
  }

  @Override
  public boolean supports(String responseContentType) {
    return isJsonResponseContent(responseContentType);
  }

  @Override
  protected <R extends Result> R parseInternal(ResponseContent responseContent, Class<R> resultClass) throws ResultParserException {
    Charset charset = responseContent.getCharset();
    if (Objects.isNull(charset)) {
      String charsetName = getCharset();
      if (StringUtils.isBlank(charsetName)) {
        throw new ResultParserException("Please configure charset of this result parser " + this);
      }
      charset = Charset.forName(charsetName);
    }
    String jsonString = responseContent.getAsString(charset);
    if (StringUtils.isBlank(jsonString)) {
      throw new ResultParserException("Could not parse to result from blank response content by result parser " + this);
    }
    try {
      return jacksonObjectMapper.readValue(jsonString, resultClass);
    } catch (IOException e) {
      throw new ResultParserException("Could not parse to result from " + jsonString, e);
    }
  }
}
