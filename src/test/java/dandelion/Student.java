package dandelion;

import java.util.Date;
import java.util.List;

public class Student {
  /**
   * 学号
   */
  private String no;
  private String name;
  private Sex sex;
  private Date birthday;
  private int score;
  private Teacher teacher;
  private Class klass;
  private List<String> addresses;
  
  public String getNo() {
    return no;
  }
  public void setNo(String no) {
    this.no = no;
  }
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
  public Teacher getTeacher() {
    return teacher;
  }
  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }
  public Sex getSex() {
    return sex;
  }
  public void setSex(Sex sex) {
    this.sex = sex;
  }
  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }
  public Class getKlass() {
    return klass;
  }
  public void setKlass(Class klass) {
    this.klass = klass;
  }
  public List<String> getAddresses() {
    return addresses;
  }
  public void setAddresses(List<String> addresses) {
    this.addresses = addresses;
  }
  
}
