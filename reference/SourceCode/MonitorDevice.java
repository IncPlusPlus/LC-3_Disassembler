import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.ListIterator;

public class MonitorDevice
{
  private OutputStreamWriter dout;
  private LinkedList mlist;
  
  public MonitorDevice()
  {
    if (!LC3Simulator.GRAPHICAL_MODE) {
      dout = new OutputStreamWriter(System.out);
    } else {
      mlist = new LinkedList();
    }
  }
  
  public MonitorDevice(java.io.OutputStream paramOutputStream) {
    dout = new OutputStreamWriter(paramOutputStream);
  }
  
  public void addActionListener(ActionListener paramActionListener) {
    mlist.add(paramActionListener);
  }
  
  public boolean ready() {
    if (LC3Simulator.GRAPHICAL_MODE) {
      return true;
    }
    try {
      dout.flush();
      return true;
    } catch (IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
    
    return false;
  }
  
  public void reset() {
    if (LC3Simulator.GRAPHICAL_MODE) {
      ListIterator localListIterator = mlist.listIterator();
      while (localListIterator.hasNext()) {
        ActionListener localActionListener = (ActionListener)localListIterator.next();
        localActionListener.actionPerformed(new java.awt.event.ActionEvent(new Integer(1), 0, null));
      }
    }
  }
  
  public void write(char paramChar) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      ListIterator localListIterator = mlist.listIterator();
      while (localListIterator.hasNext()) {
        ActionListener localActionListener = (ActionListener)localListIterator.next();
        localActionListener.actionPerformed(new java.awt.event.ActionEvent(paramChar + "", 0, null));
      }
    }
    try {
      dout.write(paramChar);
      dout.flush();
    } catch (IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
  }
}
