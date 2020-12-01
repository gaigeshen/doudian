package me.gaigeshen.doudian.request.content;

import java.lang.annotation.*;

import static me.gaigeshen.doudian.request.content.Metadata.*;

/**
 * Metadata of content, post json content with access token by default
 *
 * @author gaigeshen
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetadataAttributes {

  /**
   * Alias for {@link #url()}, url is not blank, uses the url
   *
   * @return The value for {@link #url()}
   */
  String value() default "";

  /**
   * Returns request url, support string template url, use {@link String#format(String, Object...)} internal
   *
   * @return Request url
   */
  String url() default "";

  /**
   * Returns request method, can only support 'get' and 'post', default is 'post'
   *
   * @return Request method
   */
  String method() default "post";

  /**
   * Returns boolean value, default is {@code true}
   *
   * @return If require access token, returns {@code true}, this access token will be appended to url
   */
  boolean requireAccessToken() default true;

  /**
   * Returns content metadata type, default is {@link Type#JSON},
   * the fields of content will be parsed to string if this type is {@link Type#PARAMETERS},
   * the fields of content can only support byte[], file, input stream and string value if this type is {@link Type#MULTIPART_PARAMETERS},
   *
   * @return Content metadata type
   */
  Type type() default Type.JSON;

}
