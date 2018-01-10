package dandelion.reader.excel;

import java.util.List;

public class ExcelSheet {
  private String name;
  private String[] title;
  private List<String[]> data;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String[] getTitle() {
    return title;
  }
  public void setTitle(String[] title) {
    this.title = title;
  }
  public List<String[]> getData() {
    return data;
  }
  public void setData(List<String[]> data) {
    this.data = data;
  }
  
  
}
