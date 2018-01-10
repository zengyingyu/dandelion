package dandelion.reader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 存在关联关系，并且未解析的数据
 * 
 * @author yy_zeng
 *
 */
public class NonResolveBeanRef {
  private BeanRef bean;
  private Field field;
  private Method method;
  private String fieldValue;
  
  public BeanRef getBean() {
    return bean;
  }
  public void setBean(BeanRef bean) {
    this.bean = bean;
  }
  
  public Method getMethod() {
    return method;
  }
  public void setMethod(Method method) {
    this.method = method;
  }
  public Field getField() {
    return field;
  }
  public void setField(Field field) {
    this.field = field;
  }
  public String getFieldValue() {
    return fieldValue;
  }
  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }
}
