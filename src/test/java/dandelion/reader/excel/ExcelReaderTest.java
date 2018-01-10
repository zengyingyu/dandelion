package dandelion.reader.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import dandelion.ConvertException;
import dandelion.Sex;
import dandelion.Student;
import dandelion.Teacher;

public class ExcelReaderTest {
  @Test
  public void testReadStringClassOfT() throws ParseException, ConvertException {
    ExcelReader reader = new ExcelReader(new ClassPathResource("testdata.xlsx"));
    reader.read();
    reader.getBeans();
    
    Student student = reader.read("1", Student.class);
    assertNotNull(student);
    assertEquals(student.getNo(), "S2359");
    assertEquals(student.getName(), "王汉超");
    assertEquals(student.getSex(), Sex.FEMALE);
    assertEquals(student.getBirthday(), DateUtils.parseDate("2011/10/23", "yyyy/M/d"));
    assertEquals(student.getScore(), 29);
    
    Teacher teacher = student.getTeacher();
    assertNotNull(teacher);
    assertEquals(teacher.getName(), "张国栋");
    
    dandelion.Class klass = student.getKlass();
    assertNotNull(klass);
    assertEquals(klass.getName(), "二班");
    
    List<Teacher> teachers = reader.readRef("class1", Teacher.class);
    assertNotNull(teachers);
    assertEquals(teachers.size(), 1);
    teacher = teachers.get(0);
    assertEquals(teacher.getName(), "刘国良");
  }

}
