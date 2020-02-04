import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.PipedOutputStream;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class TextConsolePanel extends javax.swing.JPanel implements java.awt.event.KeyListener, java.awt.event.FocusListener, java.awt.event.ActionListener
{
  private static final long serialVersionUID = 105L;
  private JTextArea screen;
  private javax.swing.JScrollPane spane;
  private KeyboardDevice kbd;
  private MonitorDevice monitor;
  private java.io.PipedInputStream kbin;
  private PipedOutputStream kbout;
  
  TextConsolePanel(KeyboardDevice paramKeyboardDevice, MonitorDevice paramMonitorDevice)
  {
    screen = new JTextArea(5, 21);
    screen.setEditable(false);
    screen.addKeyListener(this);
    screen.addFocusListener(this);
    screen.setLineWrap(true);
    screen.setWrapStyleWord(true);
    
    spane = new javax.swing.JScrollPane(screen, 22, 30);
    

    kbd = paramKeyboardDevice;
    kbout = new PipedOutputStream();
    try {
      kbin = new java.io.PipedInputStream(kbout);
    } catch (java.io.IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
    
    paramKeyboardDevice.setInputStream(kbin);
    paramKeyboardDevice.setDefaultInputStream();
    paramKeyboardDevice.setInputMode(KeyboardDevice.INTERACTIVE_MODE);
    paramKeyboardDevice.setDefaultInputMode();
    monitor = paramMonitorDevice;
    paramMonitorDevice.addActionListener(this);
    add(spane);
  }
  
  public void actionPerformed(java.awt.event.ActionEvent paramActionEvent)
  {
    Object localObject1 = paramActionEvent.getSource();
    Object localObject2; if ((localObject1 instanceof Integer)) {
      localObject2 = screen.getDocument();
      try {
        ((Document)localObject2).remove(0, ((Document)localObject2).getLength());
      } catch (javax.swing.text.BadLocationException localBadLocationException) {
        System.out.println(localBadLocationException.getMessage());
      }
    } else {
      localObject2 = (String)paramActionEvent.getSource();
      screen.append((String)localObject2);
    }
  }
  
  public void keyReleased(KeyEvent paramKeyEvent) {}
  
  public void keyPressed(KeyEvent paramKeyEvent) {}
  
  public void keyTyped(KeyEvent paramKeyEvent) {
    int i = paramKeyEvent.getKeyChar();
    try {
      kbout.write(i);
      kbout.flush();
    } catch (java.io.IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
  }
  
  public void focusGained(java.awt.event.FocusEvent paramFocusEvent) {
    screen.setBackground(Color.yellow);
  }
  
  public void focusLost(java.awt.event.FocusEvent paramFocusEvent) {
    screen.setBackground(Color.white);
  }
  
  public void setEnabled(boolean paramBoolean) {
    screen.setEnabled(paramBoolean);
    if (paramBoolean) {
      screen.setBackground(Color.white);
    } else {
      screen.setBackground(Color.lightGray);
    }
  }
}
