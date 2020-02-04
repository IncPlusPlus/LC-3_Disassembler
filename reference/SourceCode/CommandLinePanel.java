import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class CommandLinePanel extends javax.swing.JPanel implements java.awt.event.ActionListener
{
  private static final long serialVersionUID = 104L;
  protected JTextField textField;
  protected static JTextArea textArea;
  private LC3GUI gui;
  private static final String newline = "\n";
  private final CommandLine cmd;
  private final Machine mac;
  
  public void setGUI(LC3GUI paramLC3GUI)
  {
    cmd.setGUI(paramLC3GUI);
    gui = paramLC3GUI;
  }
  
  public CommandLinePanel(Machine paramMachine) {
    super(new java.awt.GridBagLayout());
    
    textField = new JTextField(20);
    textField.addActionListener(this);
    
    textField.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("UP"), "prevHistory");
    textField.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("DOWN"), "nextHistory");
    textField.getActionMap().put("prevHistory", new javax.swing.AbstractAction() {
      private static final long serialVersionUID = 1L;
      
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) { textField.setText(cmd.getPrevHistory());
      }
    });
    textField.getActionMap().put("nextHistory", new javax.swing.AbstractAction() {
      private static final long serialVersionUID = 1L;
      
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) { textField.setText(cmd.getNextHistory());
      }

    });
    mac = paramMachine;
    cmd = new CommandLine(paramMachine);
    textArea = new JTextArea(5, 70);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    javax.swing.JScrollPane localJScrollPane = new javax.swing.JScrollPane(textArea, 22, 30);
    



    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    gridwidth = 0;
    fill = 2;
    add(textField, localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridwidth = 0;
    fill = 1;
    weightx = 1.0D;
    weighty = 1.0D;
    add(localJScrollPane, localGridBagConstraints);
    
    setMinimumSize(new java.awt.Dimension(20, 1));
  }
  
  public void clear()
  {
    Document localDocument = textArea.getDocument();
    try {
      localDocument.remove(0, localDocument.getLength());
    } catch (javax.swing.text.BadLocationException localBadLocationException) {
      ErrorLog.logError(localBadLocationException);
    }
    CommandOutputWindow.clearConsole();
  }
  





  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str;
    




    if (paramActionEvent != null) {
      str = textField.getText();
      cmd.scheduleCommand(str);
    }
    while ((cmd.hasMoreCommands()) && ((!mac.isContinueMode()) || (cmd.hasQueuedStop()))) {
      try
      {
        str = cmd.runCommand(cmd.getNextCommand());
        
        if (str != null) {
          if (str.length() > 0) {
            writeToConsole(str);
          }
        } else {
          gui.confirmExit();
        }
      } catch (ExceptionException localExceptionException) {
        localExceptionException.showMessageDialog(getParent());
      }
    }
    textField.selectAll();
    

    textArea.setCaretPosition(textArea.getDocument().getLength());
  }
  






  public static void writeToConsole(String paramString)
  {
    textArea.append(paramString + "\n");
    CommandOutputWindow.writeToConsole(paramString);
  }
}
