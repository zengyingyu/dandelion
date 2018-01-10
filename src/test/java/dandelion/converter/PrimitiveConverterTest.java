package dandelion.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dandelion.Sex;


public class PrimitiveConverterTest {
  private PrimitiveConverter converter = new PrimitiveConverter();
  
  @Test
  public void testConvert() throws ParseException {
    BigDecimal value = (BigDecimal)converter.convert(BigDecimal.class, "139.5");
    Assert.assertTrue(value.compareTo(new BigDecimal("139.5"))==0);
    
    Sex male =(Sex)converter.convert(Sex.class, "MALE");
    Assert.assertTrue(male==Sex.MALE);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    Date date = (Date)converter.convert(Date.class, "2018-1-10");
    Assert.assertTrue(date.compareTo(sdf.parse("2018-1-10"))==0);
     
  }

}
