package dandelion;

import java.util.Date;

public class Teacher {
  private String name;
  private Date birthday;
  private Sex sex;
  /**
   * 级别
   */
  private int level;
  /**
   * 是否班主任
   */
  private boolean adviser;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Date getBirthday() {
    return birthday;
  }
  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }
  public Sex getSex() {
    return sex;
  }
  public void setSex(Sex sex) {
    this.sex = sex;
  }
  public boolean isAdviser() {
    return adviser;
  }
  public void setAdviser(boolean adviser) {
    this.adviser = adviser;
  }
  public int getLevel() {
    return level;
  }
  public void setLevel(int level) {
    this.level = level;
  }
  
  
}
