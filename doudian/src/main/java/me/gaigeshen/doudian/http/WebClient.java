package me.gaigeshen.doudian.http;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.ContentResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The web client for execute http request, use apache http-components lib
 *
 * @author gaigeshen
 */
public class WebClient implements Closeable {

  private final CloseableHttpClient client;

  /**
   * Create web client with default configuration
   */
  public WebClient() {
    this(WebClientConfig.getDefault());
  }

  /**
   * Create web client with configuration
   *
   * @param webClientConfig Configuration cannot be null
   */
  public WebClient(WebClientConfig webClientConfig) {
    Asserts.notNull(webClientConfig, "webClientConfig");
    RequestConfig config = RequestConfig.custom()
            .setConnectionRequestTimeout(webClientConfig.getConnectionRequestTimeout())
            .setConnectTimeout(webClientConfig.getConnectTimeout())
            .setSocketTimeout(webClientConfig.getSocketTimeout())
            .build();

    SSLContext sslContext = webClientConfig.getSslContext();
    SSLConnectionSocketFactory sslConnSocFactory = sslContext != null
            ? new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)
            : SSLConnectionSocketFactory.getSocketFactory();

    Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslConnSocFactory)
            .build();

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(sfr);
    connectionManager.setDefaultMaxPerRoute(20); // Set the maximum number of concurrent connections per route, which is 2 by default
    connectionManager.setMaxTotal(200); // Set the maximum number of total open connections
    connectionManager.setValidateAfterInactivity(1000);

    this.client = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setKeepAliveStrategy(new KeepAliveStrategy())
            .evictExpiredConnections()
            .evictIdleConnections(1800, TimeUnit.SECONDS)
            .setDefaultRequestConfig(config)
            .build();
  }

  /**
   * Execute request with request content, {@link RequestContent#getMethod()} can only support 'get' and 'post'
   *
   * @param req Request content
   * @return Response content
   * @throws WebClientException Execute failed
   */
  public ResponseContent execute(RequestContent req) throws WebClientException {
    if (StringUtils.isAnyBlank(req.getUri(), req.getMethod())) {
      throw new InvalidRequestContentException("The uri and method cannot be blank or null, " + req);
    }
    if ("get".equalsIgnoreCase(req.getMethod())) {
      return execute(createHttpGet(req));
    } else if ("post".equalsIgnoreCase(req.getMethod())) {
      return execute(createHttpPost(req));
    }
    throw new InvalidRequestContentException("Can only support 'get' or 'post' method, " + req);
  }

  /**
   * Create http get object with request content
   *
   * @param req Request content
   * @return Http get object
   * @throws InvalidRequestContentException Cannot create http get object
   */
  private HttpGet createHttpGet(RequestContent req) throws InvalidRequestContentException {
    try {
      return new HttpGet(req.getUri());
    } catch (Exception e) {
      throw new InvalidRequestContentException("Invalid uri, " + req, e);
    }
  }

  /**
   * Create http post object with request content
   *
   * @param req Request content
   * @return Http post object
   * @throws InvalidRequestContentException The request content invalid
   */
  private HttpPost createHttpPost(RequestContent req) throws InvalidRequestContentException {
    if (Objects.isNull(req.getType())) {
      throw new InvalidRequestContentException("The type cannot be null, " + req);
    }
    HttpPost post = new HttpPost(req.getUri());
    // Multipart type ?
    if (req.getType().equals(RequestContent.Type.MULTIPART_PARAMETERS)) {
      configMultipartHttpPost(post, req);
      return post;
    }
    // Other type ?
    EntityBuilder builder = EntityBuilder.create().chunked().gzipCompress();
    switch (req.getType()) {
      case TEXT_JSON:
      case TEXT_PLAIN:
        if (StringUtils.isAnyBlank(req.getText(), req.getCharset())) {
          throw new InvalidRequestContentException("The text and charset cannot be blank or null, " + req);
        }
        builder.setContentType(req.getType().parseContentType(req.getCharset()));
        builder.setText(req.getText());
        break;
      case BINARY:
        if (Objects.isNull(req.getBinary())) {
          throw new InvalidRequestContentException("The binary cannot be null, " + req);
        }
        builder.setBinary(req.getBinary());
        break;
      case STREAM:
        if (Objects.isNull(req.getStream())) {
          throw new InvalidRequestContentException("The stream cannot be null, " + req);
        }
        builder.setStream(req.getStream());
        break;
      case PARAMETERS:
        if (StringUtils.isBlank(req.getCharset())) {
          throw new InvalidRequestContentException("The charset cannot be null or blank, " + req);
        }
        if (Objects.isNull(req.getParameters())) {
          throw new InvalidRequestContentException("The parameters cannot be null, " + req);
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : req.getParameters().entrySet()) {
          nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        builder.setContentType(req.getType().parseContentType(req.getCharset()));
        builder.setParameters(nameValuePairs);
        break;
    }
    post.setEntity(builder.build());
    return post;
  }

  /**
   * If the request content type is multipart, call this method to configure the http post object
   *
   * @param post The http post object
   * @param req The request content
   * @throws InvalidRequestContentException If request content invalid
   */
  private void configMultipartHttpPost(HttpPost post, RequestContent req) throws InvalidRequestContentException {
    Map<String, Object> parameters = req.getMultipartParameters();
    if (Objects.isNull(parameters)) {
      throw new InvalidRequestContentException("The multipart parameters cannot be null, " + req);
    }
    MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
      // Can only support byte[], file, input stream and string value
      if (entry.getValue() instanceof byte[]) {
        multipartBuilder.addBinaryBody(entry.getKey(), (byte[]) entry.getValue());
      } else if (entry.getValue() instanceof File) {
        multipartBuilder.addBinaryBody(entry.getKey(), (File) entry.getValue());
      } else if (entry.getValue() instanceof InputStream) {
        multipartBuilder.addBinaryBody(entry.getKey(), (InputStream) entry.getValue());
      } else if (entry.getValue() instanceof String) {
        multipartBuilder.addTextBody(entry.getKey(), (String) entry.getValue());
      }
    }
    post.setEntity(multipartBuilder.build());
  }

  /**
   * Execute with http uri request object
   *
   * @param req The http uri request object
   * @return Response content
   * @throws WebClientException Execute failed
   */
  public ResponseContent execute(HttpUriRequest req) throws WebClientException {
    Content content = execute(req, new ContentResponseHandler());
    return new ResponseContent() {
      @Override
      public byte[] getRawBytes() {
        return content.asBytes();
      }
      @Override
      public String getType() {
        return content.getType().getMimeType();
      }
      @Override
      public Charset getCharset() {
        return content.getType().getCharset();
      }
      @Override
      public String getAsString() {
        return content.asString();
      }
      @Override
      public String getAsString(Charset charset) {
        return content.asString(charset);
      }
      @Override
      public InputStream getAsStream() {
        return content.asStream();
      }
    };
  }

  /**
   * Execute with http uri request object and response handler
   *
   * @param req The http uri request object
   * @param handler The response handler
   * @param <T> Result object
   * @return The result object after executed
   * @throws WebClientException Execute failed
   */
  public <T> T execute(HttpUriRequest req, AbstractResponseHandler<T> handler) throws WebClientException {
    try {
      return client.execute(req, handler);
    } catch (IOException e) {
      throw new WebClientException("Could not execute request, " + req, e);
    }
  }

  @Override
  public void close() throws IOException {
    client.close();
  }

  /**
   * @author gaigeshen
   */
  private static class KeepAliveStrategy implements ConnectionKeepAliveStrategy {
    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
      HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
      while (it.hasNext()) {
        HeaderElement headerElement = it.nextElement();
        String name = headerElement.getName();
        String value = headerElement.getValue();
        if (value != null && name.equalsIgnoreCase("timeout")) {
          try {
            return Long.parseLong(value) * 1000;
          } catch (NumberFormatException ignored) {
          }
        }
      }
      return 65 * 1000;
    }
  }
}
