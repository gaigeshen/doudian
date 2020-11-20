package me.gaigeshen.doudian.request.content;

import me.gaigeshen.doudian.request.result.Result;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Abstract content, there is no need to implement {@link #getResultClass()} method
 *
 * @author gaigeshen
 */
public abstract class AbstractContent<R extends Result> implements Content<R> {

  private final Class<R> resultClass;

  @SuppressWarnings("unchecked")
  protected AbstractContent() {
    Type genericSuperclass = getClass().getGenericSuperclass();
    if (genericSuperclass instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
      if (parameterizedType.getRawType().equals(AbstractContent.class)) {
        resultClass = (Class<R>) parameterizedType.getActualTypeArguments()[0];
      }
    }
    throw new IllegalStateException("Could not initialize content because cannot determine result class:: " + this);
  }

  @Override
  public final Class<R> getResultClass() {
    return resultClass;
  }

  @Override
  public final String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
