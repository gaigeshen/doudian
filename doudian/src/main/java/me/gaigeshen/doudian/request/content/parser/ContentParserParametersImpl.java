package me.gaigeshen.doudian.request.content.parser;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.RequestContentImpl;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.ContentException;
import me.gaigeshen.doudian.request.content.Metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Parse {@link Content} to {@link RequestContent}, the type of {@link RequestContent} is {@link RequestContent.Type#PARAMETERS}
 *
 * @author gaigeshen
 */
public class ContentParserParametersImpl extends AbstractContentParser {

  protected static final boolean DEFAULT_SNAKE_FIELD_NAMES = true;

  private boolean snakeFieldNames = DEFAULT_SNAKE_FIELD_NAMES;

  /**
   * Create content parser
   *
   * @param snakeFieldNames {@link RequestContent}'s parameter names will be parsed to snake type
   */
  public ContentParserParametersImpl(boolean snakeFieldNames) {
    this.snakeFieldNames = snakeFieldNames;
  }

  /**
   * Create content parser with snakeFieldNames = {@code true}
   */
  public ContentParserParametersImpl() {

  }

  @Override
  protected RequestContent parse(Content<?> content, String url, String method, String charset) throws ContentParserException {
    Map<String, Object> fieldValues;
    try {
      fieldValues = getFieldValues(content, snakeFieldNames);
    } catch (ContentException e) {
      throw new ContentParserException("Could not get field values from content " + content);
    }
    Map<String, String> fieldStringValues = new HashMap<>();
    for (Map.Entry<String, Object> field : fieldValues.entrySet()) {
      fieldStringValues.put(field.getKey(), String.valueOf(field.getValue()));
    }
    return RequestContentImpl.createParameters(fieldStringValues).setUri(url).setMethod(method).setCharset(charset).build();
  }

  @Override
  public boolean supports(Metadata metadata) {
    return super.supports(metadata) || metadata.getType().equals(Metadata.Type.PARAMETERS);
  }
}
