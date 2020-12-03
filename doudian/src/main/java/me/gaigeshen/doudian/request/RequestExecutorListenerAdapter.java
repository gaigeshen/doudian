package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 * Just an adapter
 *
 * @author gaigeshen
 */
public class RequestExecutorListenerAdapter implements RequestExecutorListener {
  @Override
  public void beforeContentParse(Content<?> content, String accessToken, Object... urlValues)
          throws RequestExecutorListenerException {
  }

  @Override
  public void beforeExecute(RequestContent requestContent, Content<?> content)
          throws RequestExecutorListenerException {
  }

  @Override
  public void afterExecute(RequestContent requestContent, ResponseContent responseContent, Content<?> content, Result result)
          throws RequestExecutorListenerException {
  }
}
