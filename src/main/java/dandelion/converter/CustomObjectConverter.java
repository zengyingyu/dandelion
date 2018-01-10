package dandelion.converter;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dozer.util.CollectionUtils;

import dandelion.ConvertException;
import dandelion.reader.BeanRef;

public class CustomObjectConverter {
  private Map<String, Map<String, BeanRef>> map;
  public CustomObjectConverter(Map<String, Map<String, BeanRef>> map) {
    this.map = map;
  }
  
  public Object convert(Type type, String value) throws ConvertException {
    Class<?> clazz;
    if(type instanceof Class<?>) {
      clazz = (Class<?>)type;
    }else {
      clazz = (Class<?>)((ParameterizedType) type).getRawType();
    }
    if(CollectionUtils.isArray(clazz)) {
      Class<?> componentType = clazz.getComponentType();
      return mapToArray(value, componentType);   
    }else if(CollectionUtils.isList(clazz)) {
      Class<?> componentType = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
      return mapToList(value, componentType);
    }else if(CollectionUtils.isSet(clazz)) {
      Class<?> componentType = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
      return mapToSet(value, componentType);
    }
    //自定义对象
    return mapCustomObject(clazz, value);
  }
  
  private Object mapCustomObject(Class<?> clazz, String value) throws ConvertException {
    if(StringUtils.isBlank(value)||"NULL".equalsIgnoreCase(value))return null;
    
    Map<String, BeanRef> beanMap = map.get(clazz.getName());
    if(beanMap==null) {
      throw new ConvertException(clazz.getName()+"未配置数据");
    }
    BeanRef bean =  beanMap.get(value);
    if(bean==null) {
      throw new ConvertException(String.format("%s不存在此对象($id=%s)", clazz.getName(), value));
    }
    return bean.getBean();
  }
  
  private List<Object> findCustomObject(Class<?> clazz, String value) throws ConvertException {
    if(StringUtils.isBlank(value)||"NULL".equalsIgnoreCase(value))return Collections.emptyList();
    Map<String, BeanRef> beanMap = map.get(clazz.getName());
    if(beanMap==null) {
      throw new ConvertException(clazz.getName()+"未配置数据");
    }
    return beanMap.values().stream()
        .filter(b->value.equals(b.getRefid()))
        .map(BeanRef::getBean)
        .collect(Collectors.toList());
  }
  
  private Object mapToArray(String value, Class<?> componentType) throws  ConvertException {
    List<Object> list =  findCustomObject(componentType, value);
    return list.toArray((Object[])Array.newInstance(componentType, list.size()));
  }
  
  private List<?> mapToList(String value, Class<?> componentType) throws ConvertException {
    return findCustomObject(componentType, value);
  }
  
  private Set<?> mapToSet(String value, Class<?> componentType) throws ConvertException {
   return new HashSet<>(findCustomObject(componentType, value));
  }
}
