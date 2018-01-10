package dandelion.reader;

public class BeanRef {
  /**
   * 对象id
   */
  private String id;
  /**
   * 引用此对象的id
   * 1对多
   */
  private String refid;
  
  private Object bean;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRefid() {
    return refid;
  }

  public void setRefid(String refid) {
    this.refid = refid;
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }
  
}
