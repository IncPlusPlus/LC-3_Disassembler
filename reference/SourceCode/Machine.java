import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

public class Machine implements Runnable
{
  public static final int MEM_SIZE = 65535;
  public static final Word MONITOR_READY = new Word(32768);
  public static final Word MONITOR_NOTREADY = new Word(0);
  
  public static final Word KB_AVAILABLE = new Word(32768);
  public static final Word KB_UNAVAILABLE = new Word(0);
  
  public static final Word TIMER_SET = new Word(32768);
  public static final Word TIMER_UNSET = new Word(0);
  
  private Memory memory;
  
  private RegisterFile registers;
  private LC3GUI gui = null;
  

  private LinkedList NotifyOnStop;
  
  private final Hashtable symbolTable = new Hashtable();
  private final Hashtable inverseTable = new Hashtable();
  
  private HashSet nextBreakPoints = new HashSet(10);
  




  public static final int NUM_CONTINUES = 400;
  




  boolean stopImmediately = false;
  
  public int CHECKS_PASSED = 0;
  public int CHECKS_FAILED = 0;
  
  public Machine() {
    memory = new Memory(this);
    registers = new RegisterFile(this);
    NotifyOnStop = new LinkedList();
    setPC(512);
  }
  
  public void setGUI(LC3GUI paramLC3GUI) {
    gui = paramLC3GUI;
  }
  



  public void setStoppedListener(ActionListener paramActionListener)
  {
    NotifyOnStop.add(paramActionListener);
  }
  
  public void scrollToPC() {
    scrollToPC(0);
  }
  
  public void scrollToPC(int paramInt) {
    if (gui != null) {
      gui.scrollToPC(paramInt);
    }
  }
  

  public void reset()
  {
    symbolTable.clear();
    inverseTable.clear();
    
    memory.reset();
    if (gui != null) {
      gui.setTextConsoleEnabled(true);
    }
    registers.reset();
    CHECKS_PASSED = 0;
    CHECKS_FAILED = 0;
    setPC(512);
    scrollToPC();
  }
  


  public Memory getMemory()
  {
    return memory;
  }
  


  public RegisterFile getRegisterFile()
  {
    return registers;
  }
  


  public KeyboardDevice getKeyBoardDevice()
  {
    return memory.kbDevice;
  }
  


  public MonitorDevice getMonitorDevice()
  {
    return memory.monitorDevice;
  }
  



  public String loadSymbolTable(File paramFile)
  {
    String str1;
    

    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new java.io.FileReader(paramFile));
      int i = 0;
      while (localBufferedReader.ready()) {
        String str2 = localBufferedReader.readLine();
        i++;
        if (i >= 5)
        {
          String[] arrayOfString = str2.split("\\s+");
          if (arrayOfString.length >= 3)
          {
            Integer localInteger = new Integer(Word.parseNum("x" + arrayOfString[2]));
            symbolTable.put(arrayOfString[1].toLowerCase(), localInteger);
            inverseTable.put(localInteger, arrayOfString[1]);
          } } }
      str1 = "Loaded symbol file '" + paramFile.getPath() + "'";
    }
    catch (IOException localIOException) {
      return "Could not load symbol file '" + paramFile.getPath() + "'";
    }
    return str1;
  }
  


  private boolean continueMode = false;
  
  public boolean isContinueMode() {
    return continueMode;
  }
  
  public void setContinueMode() {
    continueMode = true;
  }
  
  public void clearContinueMode() {
    continueMode = false;
  }
  





  public String loadObjectFile(File paramFile)
  {
    byte[] arrayOfByte = new byte[2];
    
    String str2 = paramFile.getPath();
    try {
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      localFileInputStream.read(arrayOfByte);
      

      int i = Word.convertByteArray(arrayOfByte[0], arrayOfByte[1]);
      
      while (localFileInputStream.read(arrayOfByte) == 2) {
        Integer localInteger = new Integer(i);
        if (symbolTable.contains(localInteger)) {
          String str4 = (String)inverseTable.get(localInteger);
          symbolTable.remove(str4.toLowerCase());
          inverseTable.remove(localInteger);
        }
        




        memory.write(i, Word.convertByteArray(arrayOfByte[0], arrayOfByte[1]));
        i++;
      }
      localFileInputStream.close();
      str1 = "Loaded object file '" + str2 + "'";
    }
    catch (IOException localIOException) {
      return "Could not load object file '" + str2 + "'";
    }
    

    String str3 = str2;
    if (str2.endsWith(".obj")) {
      str3 = str2.substring(0, str2.length() - 4);
    }
    str3 = str3 + ".sym";
    String str1 = str1 + "\n" + loadSymbolTable(new File(str3));
    
    return str1;
  }
  




  public Word readMemory(int paramInt)
  {
    return memory.read(paramInt);
  }
  




  public void writeMemory(int paramInt1, int paramInt2)
  {
    memory.write(paramInt1, paramInt2);
  }
  




  public String setBreakPoint(String paramString)
  {
    return memory.setBreakPoint(paramString);
  }
  



  public boolean isBreakPoint(int paramInt)
  {
    return memory.breakPointSet(paramInt);
  }
  




  public String clearBreakPoint(String paramString)
  {
    return memory.clearBreakPoint(paramString);
  }
  



  public String setKeyboardInputStream(File paramFile)
  {
    String str;
    


    try
    {
      memory.kbDevice.setInputStream(new FileInputStream(paramFile));
      memory.kbDevice.setInputMode(KeyboardDevice.SCRIPT_MODE);
      str = "Keyboard input file '" + paramFile.getPath() + "' enabled";
      if (gui != null) {
        gui.setTextConsoleEnabled(false);
      }
    } catch (FileNotFoundException localFileNotFoundException) {
      str = "Could not open keyboard input file '" + paramFile.getPath() + "'";
      if (gui != null) {
        gui.setTextConsoleEnabled(true);
      }
    }
    return str;
  }
  




  public String printRegisters()
  {
    return registers.toString();
  }
  

  public void setZ()
  {
    registers.setZ();
  }
  

  public void setN()
  {
    registers.setN();
  }
  

  public void setP()
  {
    registers.setP();
  }
  



  public int getRegister(int paramInt)
  {
    return registers.getRegister(paramInt);
  }
  



  public void setRegister(int paramInt1, int paramInt2)
  {
    registers.write(paramInt1, paramInt2);
  }
  


  public void setPSR(int paramInt)
  {
    registers.setPSR(paramInt);
  }
  

  public int getPSR()
  {
    return registers.getPSR();
  }
  


  public void setMCR(int paramInt)
  {
    registers.setMCR(paramInt);
  }
  

  public int getMCR()
  {
    return registers.getMCR();
  }
  



  public void setClockMCR(boolean paramBoolean)
  {
    registers.setClockMCR(paramBoolean);
  }
  


  public boolean getClockMCR()
  {
    return registers.getClockMCR();
  }
  


  public void setMPR(int paramInt)
  {
    registers.setMPR(paramInt);
  }
  


  public int getMPR()
  {
    return registers.getMPR();
  }
  



  public void checkAddr(int paramInt)
    throws IllegalMemAccessException
  {
    boolean bool = registers.getPrivMode();
    
    if ((paramInt < 0) || (paramInt >= 65535)) {
      throw new IllegalMemAccessException(paramInt);
    }
    
    if (bool) return;
    int i = paramInt >> 12;
    int j = 1 << i;
    int k = getMPR();
    
    if ((j & k) == 0) {
      throw new IllegalMemAccessException(paramInt);
    }
  }
  





  public Word getCurrentInst()
  {
    return memory.getInst(getPC());
  }
  


  public void setPC(int paramInt)
  {
    int i = registers.getPC().getValue();
    registers.setPC(paramInt);
    memory.fireTableRowsUpdated(i, i);
    memory.fireTableRowsUpdated(paramInt, paramInt);
  }
  


  public void incPC(int paramInt)
  {
    int i = registers.getPC().getValue();
    setPC(i + paramInt);
  }
  


  public int getPC()
  {
    return registers.getPC().getValue();
  }
  







  public void addNextBreakPoint(int paramInt)
  {
    nextBreakPoints.add(new Integer(paramInt));
  }
  




  public boolean nextBreakPointSet(int paramInt)
  {
    return nextBreakPoints.contains(new Integer(paramInt));
  }
  


  public void removeNextBreakPoint(int paramInt)
  {
    nextBreakPoints.remove(new Integer(paramInt));
  }
  

  public void executeStep()
    throws ExceptionException
  {
    checkAddr(getPC());
    setClockMCR(true);
    Instruction.execute(getCurrentInst(), this);
    updateStatusLabel();
    scrollToPC();
  }
  
  public void executeNext()
    throws ExceptionException
  {
    if (Instruction.nextOver(readMemory(getPC()))) {
      addNextBreakPoint((getPC() + 1) % 65535);
      executeMany();
    } else {
      executeStep();
    }
  }
  




  public synchronized String stopExecution(boolean paramBoolean)
  {
    return stopExecution(0, paramBoolean);
  }
  






  public synchronized String stopExecution(int paramInt, boolean paramBoolean)
  {
    stopImmediately = true;
    clearContinueMode();
    updateStatusLabel();
    scrollToPC(paramInt);
    memory.fireTableDataChanged();
    
    if (paramBoolean) {
      ListIterator localListIterator = NotifyOnStop.listIterator(0);
      while (localListIterator.hasNext()) {
        ActionListener localActionListener = (ActionListener)localListIterator.next();
        localActionListener.actionPerformed(null);
      }
    }
    return "Stopped at " + Word.toHex(getPC());
  }
  



  public void executePumpedContinues()
    throws ExceptionException
  {
    int i = 400;
    setClockMCR(true);
    if (gui != null) gui.setStatusLabelRunning();
    while ((!stopImmediately) && (i > 0)) {
      try {
        checkAddr(getPC());
        Instruction.execute(getCurrentInst(), this);
      } catch (ExceptionException localExceptionException) {
        stopExecution(true);
        throw localExceptionException;
      }
      if (memory.breakPointSet(getPC())) {
        String str = "Hit breakpoint at " + Word.toHex(getPC());
        if (gui == null) {
          System.out.println(str);
        } else {
          CommandLinePanel.writeToConsole(str);
        }
        stopExecution(true);
      }
      if (nextBreakPointSet(getPC())) {
        stopExecution(true);
        removeNextBreakPoint(getPC());
      }
      i--;
    }
    

    if (isContinueMode()) {
      javax.swing.SwingUtilities.invokeLater(this);
    }
  }
  






  public synchronized void executeMany()
    throws ExceptionException
  {
    setContinueMode();
    stopImmediately = false;
    try {
      executePumpedContinues();
    } catch (ExceptionException localExceptionException) {
      stopExecution(true);
      throw localExceptionException;
    }
  }
  





  public String lookupSym(int paramInt)
  {
    return (String)inverseTable.get(new Integer(paramInt));
  }
  




  public int lookupSym(String paramString)
  {
    Object localObject = symbolTable.get(paramString.toLowerCase());
    if (localObject != null) {
      return ((Integer)localObject).intValue();
    }
    return Integer.MAX_VALUE;
  }
  






  public int getAddress(String paramString)
  {
    int i = Word.parseNum(paramString);
    if (i == Integer.MAX_VALUE) {
      i = lookupSym(paramString);
    }
    return i;
  }
  


  public void run()
  {
    try
    {
      executePumpedContinues();
    } catch (ExceptionException localExceptionException) {
      if (gui != null) {
        localExceptionException.showMessageDialog(gui.getFrame());
      } else {
        System.out.println(localExceptionException.getMessage());
      }
    }
  }
  

  public void updateStatusLabel()
  {
    if (gui != null) {
      if (!getClockMCR()) {
        gui.setStatusLabelHalted();
      } else if (isContinueMode()) {
        gui.setStatusLabelRunning();
      } else {
        gui.setStatusLabelSuspended();
      }
    }
  }
}
