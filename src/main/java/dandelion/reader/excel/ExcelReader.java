package dandelion.reader.excel;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import dandelion.ConvertException;
import dandelion.converter.CustomObjectConverter;
import dandelion.converter.PrimitiveConverter;
import dandelion.reader.BeanReader;
import dandelion.reader.BeanRef;
import dandelion.reader.NonResolveBeanRef;

public class ExcelReader extends BeanReader{
  protected Map<String, Map<String, BeanRef>> beanMap;
  
  public ExcelReader(Resource resource) {
    super(resource);
  }

  private List<NonResolveBeanRef> beans = new ArrayList<>();
  private PrimitiveConverter primitiveConverter = new PrimitiveConverter();
  
  public void read() throws ConvertException {
    try {
      for(Resource resource:resources) {
        read(resource);
      }
      //处理未解析的对象引用
      CustomObjectConverter customerObjectConverter = new CustomObjectConverter(beanMap);
      for(NonResolveBeanRef ref:beans) {
        BeanRef bean = ref.getBean();
        Method method = ref.getMethod();
        method.invoke(bean.getBean(), customerObjectConverter.convert(method.getGenericParameterTypes()[0], ref.getFieldValue()));
      }
    }catch(Exception e) {
      throw new ConvertException(e.getMessage(), e);
    }
  }
  
  private void read(Resource resource) throws ConvertException {
    try(
        InputStream inputStream = resource.getInputStream();
    ) {
      beanMap = new HashMap<>();
      List<ExcelSheet> sheets = new ExcelParser().read(inputStream, resource.getFilename());
      for(ExcelSheet sheet:sheets) {
        beanMap.put(sheet.getName(), convert(sheet));
      }
    }catch(Exception e) {
      throw new ConvertException(e.getMessage(), e);
    }
  }

  Map<String, BeanRef> convert(ExcelSheet sheet) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
    Map<String, BeanRef> map = new HashMap<>();
    Class<?> clazz = Class.forName(sheet.getName());
    for(String[] data:sheet.getData()) {
      BeanRef bean = this.convert(clazz, sheet.getTitle(), data);
      map.put(bean.getId(), bean);
    }
    return map;
  }
  
  BeanRef convert(Class<?> clazz, String[] fieldNames, String[] fieldValues) throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
    BeanRef ref=new BeanRef();
    ref.setId(fieldValues[0]);
    ref.setRefid(fieldValues[1]);
    Object obj = clazz.newInstance();
    ref.setBean(obj);
    for(int i=2;i<fieldNames.length;i++) {
      this.setFieldValue(ref, fieldNames[i], fieldValues[i]);
    }
    return ref;
  }
  
  void setFieldValue(BeanRef ref, String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    if(StringUtils.isBlank(fieldValue))return;
    Class<?> clazz = ref.getBean().getClass();
    Field field = clazz.getDeclaredField(fieldName);
    Method method = clazz.getMethod(getSetterMethodName(fieldName), field.getType());
    Class<?> fieldClass;
    Type type = method.getGenericParameterTypes()[0];
    if(type instanceof Class<?>) {
      fieldClass = (Class<?>)type;
    }else {
      fieldClass = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
    }
     
    if(primitiveConverter.accepts(fieldClass)) {
      method.invoke(ref.getBean(), primitiveConverter.convert(type, fieldValue));
    }else {
      NonResolveBeanRef nonResolveBean = new NonResolveBeanRef();
      nonResolveBean.setBean(ref);
      nonResolveBean.setMethod(method);
      nonResolveBean.setFieldValue(fieldValue);
      beans.add(nonResolveBean);
    }
  }
  
  private String getSetterMethodName(String fieldName) {
    return "set"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T read(String beanId, Class<T> clazz) {
    Map<String, BeanRef> map = beanMap.get(clazz.getName());
    if(map==null)return null;
    BeanRef bean = map.get(beanId);
    if(bean==null)return null;
    return (T)bean.getBean();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> readRef(String refid, Class<T> clazz) {
    Map<String, BeanRef> map = beanMap.get(clazz.getName());
    if(map==null)return Collections.emptyList();
    
    return map.values().stream()
                .filter(b->refid.equals(b.getRefid()))
                .map(b->(T)b.getBean())
                .collect(Collectors.toList());
  }

  @Override
  public Map<String, Map<String, BeanRef>> getBeans() {
    return beanMap;
  }

}
