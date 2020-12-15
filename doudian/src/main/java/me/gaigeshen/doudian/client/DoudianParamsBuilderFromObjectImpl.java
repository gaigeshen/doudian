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
 */
public class DoudianParamsBuilderFromObjectImpl implements DoudianParamsBuilder<Object> {

  private static final DoudianParamsBuilderFromObjectImpl INSTANCE = new DoudianParamsBuilderFromObjectImpl();

  // 对象实例类型和其所有字段的映射关系
  private final Map<Class<?>, Map<String, Field>> sourceFieldsCache = new ConcurrentHashMap<>();

  /**
   * 请调用此方法获取全局共享的抖店请求参数构建器
   *
   * @return 全局共享的抖店请求参数构建器
   */
  public static DoudianParamsBuilderFromObjectImpl getInstance() {
    return INSTANCE;
  }

  /**
   * @see #getInstance()
   */
  private DoudianParamsBuilderFromObjectImpl() { }

  @Override
  public DoudianParams build(String method, Object source) throws DoudianParamsBuildingException {
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

  // 获取对象实例类型的所有字段，返回字段名称和字段的映射关系，字段名称可以由注解指定
  // 返回的映射关系将被缓存避免重复获取，且不为空对象
  private Map<String, Field> getAllFields(Class<?> sourceClass) {
    return sourceFieldsCache.computeIfAbsent(sourceClass, aClass -> {
      Map<String, Field> fields = new HashMap<>();
      Class<?> currentClass = aClass;
      while (currentClass != null) {
        for (Field field : currentClass.getDeclaredFields()) {
          DoudianParam anno = field.getAnnotation(DoudianParam.class);
          fields.put(Objects.nonNull(anno) ? StringUtils.defaultIfBlank(anno.value(), field.getName()) : field.getName(), field);
        }
        currentClass = currentClass.getSuperclass();
      }
      return fields;
    });
  }
}
