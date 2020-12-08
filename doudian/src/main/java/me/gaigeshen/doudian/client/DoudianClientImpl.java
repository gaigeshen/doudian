package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.RequestExecutorException;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

import java.io.IOException;

/**
 * 抽象的抖店客户端
 *
 * @author gaigeshen
 */
public class DoudianClientImpl implements DoudianClient {

  @Override
  public <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
    return null;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
    return null;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
    return null;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    return null;
  }

  @Override
  public ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
    return null;
  }

  @Override
  public void close() throws IOException {

  }
}
