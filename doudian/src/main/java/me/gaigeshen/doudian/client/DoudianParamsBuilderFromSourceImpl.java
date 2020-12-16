package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抖店请求参数构建器实现，用于将对象实例转换成抖店请求参数，取该对象的所有字段，包含所有父类的字段
 *
 * @author gaigeshen
 * @see DoudianParam
 */
public class DoudianParamsBuilderFromSourceImpl implements DoudianParamsBuilder<DoudianParamsSource> {

  private static final DoudianParamsBuilderFromSourceImpl INSTANCE = new DoudianParamsBuilderFromSourceImpl();

  private final Map<Class<?>, Map<String, Field>> sourceFieldsCache = new ConcurrentHashMap<>();

  /**
   * 请调用此方法获取全局共享的抖店请求参数构建器
   *
   * @return 全局共享的抖店请求参数构建器
   */
  public static DoudianParamsBuilderFromSourceImpl getInstance() {
    return INSTANCE;
  }

  /**
   * @see #getInstance()
   */
  private DoudianParamsBuilderFromSourceImpl() { }

  @Override
  public DoudianParams build(String method, DoudianParamsSource source) throws DoudianParamsBuildingException {
    Asserts.notBlank(method, "method");
    Asserts.notNull(source, "source");

    Map<String, Field> allFields = getAllFields(source.getClass());
    DoudianParams params = new DoudianParams(method);
    try {
      for (Map.Entry<String, Field> entry : allFields.entrySet()) {
        Field field = entry.getValue();
        field.setAccessible(true);
        params.addParam(entry.getKey(), field.get(source));
      }
    } catch (IllegalAccessException e) {
      throw new DoudianParamsBuildingException("Could not build params from object " + source, e);
    }
    return params;
  }

  private Map<String, Field> getAllFields(Class<?> sourceClass) {
    return sourceFieldsCache.computeIfAbsent(sourceClass, aClass -> {
      Map<String, Field> fields = new HashMap<>();
      Class<?> currentClass = aClass;
      while (currentClass != null) {
        for (Field field : currentClass.getDeclaredFields()) {
          DoudianParam annotation = field.getAnnotation(DoudianParam.class);
          if (Objects.isNull(annotation)) {
            continue;
          }
          if (StringUtils.isNotBlank(annotation.value())) {
            fields.put(annotation.value(), field);
          } else {
            fields.put(annotation.namingStrategy().getName(field.getName()), field);
          }
        }
        currentClass = currentClass.getSuperclass();
      }
      return fields;
    });
  }
}
