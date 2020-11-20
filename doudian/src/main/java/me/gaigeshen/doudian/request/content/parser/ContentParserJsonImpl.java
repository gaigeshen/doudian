package me.gaigeshen.doudian.request.content.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.RequestContentImpl;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.util.Asserts;

/**
 * Parse {@link Content} to {@link RequestContent}, the type of {@link RequestContent} is {@link RequestContent.Type#TEXT_JSON}
 *
 * @author gaigeshen
 */
public class ContentParserJsonImpl extends AbstractContentParser {

  private final ObjectMapper jacksonObjectMapper;

  /**
   * Create content parser with jackson {@link ObjectMapper}
   *
   * @param jacksonObjectMapper The jackson {@link ObjectMapper} cannot be null
   */
  public ContentParserJsonImpl(ObjectMapper jacksonObjectMapper) {
    this.jacksonObjectMapper = Asserts.notNull(jacksonObjectMapper, "jacksonObjectMapper");
  }

  /**
   * Create content parser with default jackson {@link ObjectMapper}, create {@link ObjectMapper} internal
   */
  public ContentParserJsonImpl() {
    this(new ObjectMapper());
  }

  @Override
  protected RequestContent parse(Content<?> content, String url, String method, String charset) throws ContentParserException {
    String json;
    try {
      json = jacksonObjectMapper.writeValueAsString(content);
    } catch (JsonProcessingException e) {
      throw new ContentParserException("Could not parse to json from content " + content);
    }
    return RequestContentImpl.createJson(json).setUri(url).setMethod(method).build();
  }

  @Override
  public boolean supports(Metadata metadata) {
    return metadata.getType().equals(Metadata.Type.JSON);
  }
}
