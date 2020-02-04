import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JOptionPane;










public abstract class ExceptionException
  extends Exception
{
  public ExceptionException() {}
  
  public ExceptionException(String paramString)
  {
    super(paramString);
  }
  



  public String getExceptionDescription()
  {
    return "Generic Exception: " + getMessage();
  }
  


  public void showMessageDialog(JFrame paramJFrame)
  {
    JOptionPane.showMessageDialog(paramJFrame, getExceptionDescription());
  }
  


  public void showMessageDialog(Container paramContainer)
  {
    JOptionPane.showMessageDialog(paramContainer, getExceptionDescription());
  }
}
