import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;





public class CommandOutputWindow
  extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static JTextArea textArea;
  
  public CommandOutputWindow(String paramString)
  {
    super(paramString);
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    JScrollPane localJScrollPane = new JScrollPane(textArea, 22, 30);
    

    getContentPane().add(localJScrollPane);
  }
  
  public static void writeToConsole(String paramString)
  {
    textArea.append(paramString + "\n");
  }
  
  public static void clearConsole()
  {
    Document localDocument = textArea.getDocument();
    try {
      localDocument.remove(0, localDocument.getLength());
    } catch (BadLocationException localBadLocationException) {
      ErrorLog.logError(localBadLocationException);
    }
  }
}
