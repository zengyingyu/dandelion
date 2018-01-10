package dandelion;

public class ConvertException extends Exception{

  /**
   * 
   */
  private static final long serialVersionUID = 8563687955122059453L;

  public ConvertException() {}
  
  public ConvertException(String message) {
    super(message);
  }
  
  public ConvertException(String message, Throwable t) {
    super(message, t);
  }
}
