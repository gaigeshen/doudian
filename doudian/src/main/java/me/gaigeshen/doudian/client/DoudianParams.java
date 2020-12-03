package me.gaigeshen.doudian.client;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author gaigeshen
 */
public class DoudianParams implements Iterable<DoudianParams.Param> {

  private final List<Param> params = new ArrayList<>();

  public void addParam(Param param) {
    if (Objects.isNull(param)) {
      throw new IllegalArgumentException("param cannot be null");
    }
    params.add(param);
  }

  public void removeAllParams() {
    params.clear();
  }

  public Param getParam(String name) {
    for (Param param : params) {
      if (param.getName().equals(name)) {
        return param;
      }
    }
    return null;
  }

  public Object getParamValue(String name) {
    Param param = getParam(name);
    if (Objects.isNull(param)) {
      return null;
    }
    return param.getValue();
  }

  @Override
  public Iterator<Param> iterator() {
    return params.iterator();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(params);
  }

  /**
   * @author gaigeshen
   */
  public interface Param {

    String getName();

    Object getValue();
  }

  /**
   * @author gaigeshen
   */
  public static class SimpleParam implements Param {
    private final String name;
    private final Object value;

    public SimpleParam(String name, Object value) {
      if (Objects.isNull(name)) {
        throw new IllegalArgumentException("name cannot be null");
      }
      if (Objects.isNull(value)) {
        throw new IllegalArgumentException("value cannot be null");
      }
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }
    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this);
    }
  }
}
