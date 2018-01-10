package dandelion.reader;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import dandelion.ConvertException;

public abstract class BeanReader {
  protected Resource resource;
  
  public BeanReader(Resource resource) {
    this.resource = resource;
  }
  
  public abstract void read() throws ConvertException ;
  
  public abstract Map<String, Map<String, BeanRef>> getBeans();
  
  public abstract <T> T read(String beanId, Class<T> clazz);
  
  public abstract <T> List<T> readRef(String refid, Class<T> clazz);
}
