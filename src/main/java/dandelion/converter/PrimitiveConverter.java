package dandelion.converter;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.converters.DateFormatContainer;
import org.dozer.converters.PrimitiveOrWrapperConverter;
import org.dozer.util.CollectionUtils;
import org.dozer.util.MappingUtils;

public class PrimitiveConverter {
  private final PrimitiveOrWrapperConverter converter = new PrimitiveOrWrapperConverter();
  private final DateFormatContainer dfContainer = new DateFormatContainer("yyyy-M-d");

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Object convert(Type type, String value) {
    Class<?> clazz;
    if(type instanceof Class<?>) {
      clazz = (Class<?>)type;
    }else {
      clazz = (Class<?>)((ParameterizedType) type).getRawType();
    }
    if (converter.accepts(clazz)) {
      return converter.convert(value, clazz, dfContainer);
    }else if(CollectionUtils.isArray(clazz)) {
      Class<?> componentType = clazz.getComponentType();
      if (converter.accepts(clazz)) {
        return mapToArray(value.split(","), componentType);
      }      
    }else if(CollectionUtils.isList(clazz)) {
      Class<?> componentType =  (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
      if (converter.accepts(clazz)) {
        return mapToList(value.split(","), componentType);
      }      
    }else if(CollectionUtils.isSet(clazz)) {
      Class<?> componentType = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
      if (converter.accepts(clazz)) {
        return mapToSet(value.split(","), componentType);
      }
    }else if(MappingUtils.isSupportedMap(clazz)) {
      throw new UnsupportedOperationException();
    }else if (MappingUtils.isEnumType(clazz)) {
      return mapEnum((Class<Enum>) clazz, value);
    }
    //自定义对象，或自定义对象的集合或数组
    return null;
  }
  
  public boolean accepts(Type type) {
    Class<?> clazz;
    if(type instanceof Class<?>) {
      clazz = (Class<?>)type;
    }else {
      clazz = (Class<?>)((ParameterizedType) type).getRawType();
    }
    if(converter.accepts(clazz)) {
      return true;
    }else if(MappingUtils.isEnumType(clazz)){
      return true;
    }
    if(CollectionUtils.isArray(clazz)) {
      Class<?> componentType = clazz.getComponentType();
      return converter.accepts(componentType);
    }else if(CollectionUtils.isCollection(clazz)) {
      Class<?> componentType = ((ParameterizedType) type).getActualTypeArguments()[0].getClass();
      return converter.accepts(componentType)||MappingUtils.isEnumType(componentType);
    }
    return false;
  }
  
  private <T extends Enum<T>> T mapEnum(Class<T> destFieldType, String name) {
    return Enum.valueOf(destFieldType, name);
  }
  
  private Object mapToArray(String[] values, Class<?> destEntryType) {
    Object array = Array.newInstance(destEntryType, values.length);
    for(int i=0;i<values.length;i++) {
      Array.set(array, i, converter.convert(values[i], destEntryType, dfContainer));
    }
    return array;
  }
  
  private List<?> mapToList(String[] values, Class<?> destEntryType) {
    List<Object> result = new ArrayList<>(values.length);
    for(String value:values) {
      result.add(converter.convert(value, destEntryType, dfContainer));
    }
    return result;
  }
  
  private Set<?> mapToSet(String[] values, Class<?> destEntryType) {
    Set<Object> result = new HashSet<>(values.length);
    for(String value:values) {
      result.add(converter.convert(value, destEntryType, dfContainer));
    }
    return result;
  }
  
}
