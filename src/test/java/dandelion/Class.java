package dandelion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 *班级
 * @author yy_zeng
 *
 */
public class Class {
  private String name;
  private Set<Teacher> teachers;
  private List<Student> students;
  /**
   * 班级经费
   */
  private BigDecimal fee;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Set<Teacher> getTeachers() {
    return teachers;
  }
  public void setTeachers(Set<Teacher> teachers) {
    this.teachers = teachers;
  }
  public List<Student> getStudents() {
    return students;
  }
  public void setStudents(List<Student> students) {
    this.students = students;
  }
  public BigDecimal getFee() {
    return fee;
  }
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }
  
  
}
