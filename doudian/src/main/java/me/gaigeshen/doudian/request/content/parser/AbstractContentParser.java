package me.gaigeshen.doudian.request.content.parser;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.RequestContentImpl;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.ContentException;
import me.gaigeshen.doudian.request.content.ContentHelper;
import me.gaigeshen.doudian.request.content.Metadata;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Abstract content parser, very helpful class
 *
 * @author gaigeshen
 */
public abstract class AbstractContentParser implements ContentParser {

  protected static final String DEFAULT_ACCESS_TOKEN_NAME = "access_token";

  protected static final String DEFAULT_CHARSET = "utf-8";

  /**
   * Returns default access token name, {@link AbstractContentParser#DEFAULT_ACCESS_TOKEN_NAME}, can be overriden in a subclass, cannot be null or blank
   *
   * @return Default access token name
   */
  @Override
  public String getAccessTokenName() {
    return DEFAULT_ACCESS_TOKEN_NAME;
  }

  /**
   * Returns default charset, {@link AbstractContentParser#DEFAULT_CHARSET}, can be overriden in a subclass, cannot be null or blank
   *
   * @return Default charset
   */
  @Override
  public String getCharset() {
    return DEFAULT_CHARSET;
  }

  /**
   * Please override this method like this: {@code return super.supports(metadata) || metadata.getType().equals(Metadata.Type.PARAMETERS)}
   *
   * @param metadata Valid metadata, cannot be null
   * @return Returns {@code true} if the metadata's type is {@link Metadata.Type#NONE} or other type
   */
  @Override
  public boolean supports(Metadata metadata) {
    return Metadata.Type.NONE.equals(metadata.getType());
  }

  @Override
  public final RequestContent parse(Content<?> content, Metadata metadata, String accessToken, Object... urlValues) throws ContentParserException {
    // Assemble url and parse content use this charset
    String charset = getCharset();
    if (StringUtils.isBlank(charset)) {
      throw new ContentParserException("Please configure charset of this content parser " + this);
    }
    // Access token name cannot be blank or null
    String accessTokenName = getAccessTokenName();
    if (StringUtils.isBlank(accessTokenName)) {
      throw new ContentParserException("Please configure access token name of this content parser " + this);
    }
    // Append access token and replace url template parameter values
    String url = assembleUrl(metadata, accessTokenName, accessToken, charset, urlValues);
    // If no content
    if (Metadata.Type.NONE.equals(metadata.getType())) {
      return RequestContentImpl.builder().setUri(url).setMethod(metadata.getMethod()).build();
    }
    // Do parse by sub class
    return parse(content, url, metadata.getMethod(), charset);
  }

  /**
   * Parse to {@link RequestContent} object from {@link Content}, with url, method and charset
   *
   * @param content The content cannot be null
   * @param url The url cannot be null or blank
   * @param method The method cannot be null or blank
   * @param charset The charset cannot be null or blank
   * @return The parsed {@link RequestContent}
   * @throws ContentParserException Could not parse to {@link RequestContent}
   */
  protected abstract RequestContent parse(Content<?> content, String url, String method, String charset) throws ContentParserException;

  /**
   * Returns url with access token and url template parameter values
   *
   * @param metadata Valid content metadata, cannot be null
   * @param accessTokenName The access token name, cannot be null or blank
   * @param accessToken The access token, may be null or blank
   * @param charset The charset, cannot be null or blank
   * @param urlValues Url template parameter values
   * @return The url
   */
  private String assembleUrl(Metadata metadata, String accessTokenName, String accessToken, String charset, Object... urlValues) {
    String fullUrl = metadata.getUrl();
    if (ArrayUtils.isNotEmpty(urlValues)) {
      fullUrl = String.format(fullUrl, urlValues);
    }
    if (!metadata.isRequireAccessToken() || StringUtils.isBlank(accessToken)) {
      return fullUrl;
    }
    List<NameValuePair> parameters = new ArrayList<>();
    parameters.add(new BasicNameValuePair(accessTokenName, accessToken));
    String[] splitedUrl = fullUrl.split("\\?");
    if (splitedUrl.length > 1) {
      parameters.addAll(URLEncodedUtils.parse(splitedUrl[1], Charset.forName(charset)));
    }
    return new StringJoiner("?").add(splitedUrl[0]).add(URLEncodedUtils.format(parameters, charset)).toString();
  }

  /**
   * Helpful method, returns all field values of content, include all parent class fields, exclude null value fields
   *
   * @param content The content cannot be null
   * @param snakeFieldNames This parameter can be set to true for returns snake field name
   * @return All field values of this content
   * @throws ContentException Could not get fields of this content
   */
  protected Map<String, Object> getFieldValues(Content<?> content, boolean snakeFieldNames) throws ContentException {
    return ContentHelper.getFieldValues(content, snakeFieldNames);
  }
}
