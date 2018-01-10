package dandelion.reader.excel;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class ExcelParserTest {
  private ExcelParser paser = new ExcelParser();
  
  @Test
  public void testReadInputStreamString() throws IOException {
    ClassPathResource resource = new ClassPathResource("testdata.xlsx");
    List<ExcelSheet> sheets = paser.read(resource.getInputStream(), resource.getFilename());
    resource.getInputStream().close();
    assertEquals(sheets.size(), 3);
    ExcelSheet sheet = sheets.get(0);
    assertEquals(sheet.getName(), "dandelion.Class");
    assertEquals(sheet.getTitle().length, 6);
    assertArrayEquals(sheet.getTitle(), new String[] {"$id","$refid","name","teachers","students","fee"});
    assertArrayEquals(sheet.getData().get(0), new String[] {"1",null, "一班","class1","class1","2001.56"});
  }

}
