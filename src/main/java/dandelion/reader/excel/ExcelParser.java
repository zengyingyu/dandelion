package dandelion.reader.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {
  private static final String EXCEL2003L =".xls";    //2003- 版本的excel    
  private static final String EXCEL2007U =".xlsx";   //2007+ 版本的excel
  private final  DecimalFormat df = new DecimalFormat("0");  //格式化number String字符    
  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //日期格式化    
  
  public List<ExcelSheet> read(InputStream in,String fileName)throws IOException {    
      List<ExcelSheet> list = new ArrayList<>(); 
      
      Workbook work = null;
      try {
        work = getWorkbook(in,fileName);
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            list.add(read(work.getSheetAt(i)));
        }
      }finally {
        try {
          if(work!=null)
            work.close();
        } catch (IOException e) {}
      }
      return list;
  }
  
  ExcelSheet read(Sheet sheet) {
    ExcelSheet excelSheet = new ExcelSheet();
    excelSheet.setName(sheet.getSheetName());
    
    if(sheet.getPhysicalNumberOfRows()>0) {
      excelSheet.setTitle(read(sheet.getRow(sheet.getFirstRowNum())));
      List<String[]> data = new ArrayList<>(sheet.getLastRowNum()-sheet.getFirstRowNum());
      for (int j = sheet.getFirstRowNum()+1; j <= sheet.getLastRowNum(); j++) {    
        data.add(read(sheet.getRow(j)));
      }
      excelSheet.setData(data);
    }
    return excelSheet;
  }
  
  String[] read(Row row) {
    String[] cells = new String[row.getLastCellNum()-row.getFirstCellNum()+1];
    for (int i=0,y = row.getFirstCellNum(); y <= row.getLastCellNum(); y++,i++) {    
      cells[i]=getCellValue(row.getCell(y));
    }
    return cells;
  }
  
      
  /**  
   * 描述：根据文件后缀，自适应上传文件的版本   
   * @param inStr,fileName  
   * @return  
   * @throws IOException 
   * @throws Exception  
   */    
   Workbook getWorkbook(InputStream inStr,String fileName) throws IOException{
      String fileType = fileName.substring(fileName.lastIndexOf('.'));
      if(EXCEL2003L.equals(fileType)){    
         return new HSSFWorkbook(inStr);  //2003-    
      }else if(EXCEL2007U.equals(fileType)){    
        return new XSSFWorkbook(inStr);  //2007+    
      }else{    
          return null;
      }
  }    
  
  /**  
   * 描述：对表格中数值进行格式化  
   * @param cell  
   * @return  
   */    
  String getCellValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    switch (cell.getCellTypeEnum()) {
      case STRING:
        return cell.getRichStringCellValue().getString();
      case NUMERIC:
        if ("General".equals(cell.getCellStyle().getDataFormatString())) {
          return df.format(cell.getNumericCellValue());
        } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
          return sdf.format(cell.getDateCellValue());
        } else {
          return String.valueOf(cell.getNumericCellValue());
        }
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case BLANK:
      default:
        return cell.getStringCellValue();
    }
  }
}
