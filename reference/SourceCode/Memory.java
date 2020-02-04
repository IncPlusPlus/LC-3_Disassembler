

public class Memory
  extends LC3TableModel
{
  private static final long serialVersionUID = 102L;
  private Word[] memArr = new Word[65535];
  private String[] colNames = { "BP", "Address", "Value", "Instruction" };
  
  private boolean[] breakPoints = new boolean[65535];
  public KeyboardDevice kbDevice = new KeyboardDevice();
  public MonitorDevice monitorDevice = new MonitorDevice();
  public TimerDevice timerDevice = new TimerDevice();
  
  public static final int BEGIN_DEVICE_REGISTERS = 65024;
  
  public static final int KBSR = 65024;
  
  public static final int KBDR = 65026;
  
  public static final int DSR = 65028;
  
  public static final int DDR = 65030;
  
  public static final int TMR = 65032;
  
  public static final int TMI = 65034;
  
  public static final int DISABLE_TIMER = 0;
  
  public static final int MANUAL_TIMER_MODE = 1;
  public static final int MPR = 65042;
  public static final int MCR = 65534;
  public static final int breakPointColumn = 0;
  public static final int addressColumn = 1;
  public static final int valueColumn = 2;
  public static final int insnColumn = 3;
  private final Machine machine;
  
  public Memory(Machine paramMachine)
  {
    machine = paramMachine;
    for (int i = 0; i < 65535; i++) {
      memArr[i] = new Word();
      breakPoints[i] = false;
    }
    timerDevice.setTimer();
  }
  


  public void reset()
  {
    for (int i = 0; i < 65535; i++) {
      memArr[i].reset();
    }
    kbDevice.reset();
    monitorDevice.reset();
    timerDevice.reset();
    clearAllBreakPoints();
    
    fireTableRowsUpdated(0, 65534);
  }
  
  public int getRowCount() { return memArr.length; }
  public int getColumnCount() { return colNames.length; }
  public String getColumnName(int paramInt) { return colNames[paramInt]; }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return ((paramInt2 == 2) || (paramInt2 == 0)) && (paramInt1 < 65024);
  }
  



  public boolean symbolExists(String paramString)
  {
    return machine.lookupSym(paramString) != Integer.MAX_VALUE;
  }
  
  public int getSymbolAddress(String paramString) { return machine.lookupSym(paramString); }
  








  public boolean breakPointSet(int paramInt)
  {
    return breakPoints[paramInt];
  }
  
  public String setBreakPoint(String paramString)
  {
    int i = machine.getAddress(paramString);
    String str; if (i != Integer.MAX_VALUE) {
      str = setBreakPoint(i);
      if (symbolExists(paramString)) {
        str = str + " ('" + paramString + "')";
      }
    } else {
      str = "Error: Invalid address or label ('" + paramString + "')";
    }
    return str;
  }
  
  public String setBreakPoint(int paramInt) { if ((paramInt < 0) || (paramInt >= 65535)) {
      return "Error: Invalid address or label";
    }
    breakPoints[paramInt] = true;
    fireTableRowsUpdated(paramInt, paramInt);
    return "Breakpoint set at " + Word.toHex(paramInt);
  }
  
  public String clearBreakPoint(String paramString)
  {
    int i = machine.getAddress(paramString);
    String str; if (i != Integer.MAX_VALUE) {
      str = clearBreakPoint(i);
      if (symbolExists(paramString)) {
        str = str + " ('" + paramString + "')";
      }
    } else {
      str = "Error: Invalid address or label ('" + paramString + "')";
    }
    return str;
  }
  
  public String clearBreakPoint(int paramInt) { if ((paramInt < 0) || (paramInt >= 65535)) {
      return "Error: Invalid address or label";
    }
    breakPoints[paramInt] = false;
    fireTableRowsUpdated(paramInt, paramInt);
    return "Breakpoint cleared at " + Word.toHex(paramInt);
  }
  
  public void clearAllBreakPoints() {
    for (int i = 0; i < 65535; i++) {
      breakPoints[i] = false;
    }
  }
  









  public Object getValueAt(int paramInt1, int paramInt2)
  {
    Object localObject = null;
    switch (paramInt2)
    {



    case 0: 
      localObject = new Boolean(breakPointSet(paramInt1));
      break;
    
    case 1: 
      localObject = Word.toHex(paramInt1);
      String str = machine.lookupSym(paramInt1);
      if (str != null) {
        localObject = localObject + " " + str;
      }
      
      break;
    case 2: 
      if (paramInt1 < 65024) {
        localObject = memArr[paramInt1].toHex();
      } else {
        localObject = "???";
      }
      break;
    
    case 3: 
      if (paramInt1 < 65024) {
        localObject = Instruction.disassemble(memArr[paramInt1], paramInt1, machine);
      } else {
        localObject = "Use 'list' to query";
      }
      break;
    }
    
    return localObject;
  }
  





  public Word getInst(int paramInt)
  {
    return memArr[paramInt];
  }
  














  public Word read(int paramInt)
  {
    Word localWord = null;
    switch (paramInt) {
    case 65024: 
      if (kbDevice.available()) {
        localWord = Machine.KB_AVAILABLE;
      } else {
        localWord = Machine.KB_UNAVAILABLE;
      }
      







      break;
    case 65026: 
      localWord = kbDevice.read();
      break;
    case 65028: 
      if (monitorDevice.ready()) {
        localWord = Machine.MONITOR_READY;
      } else {
        localWord = Machine.MONITOR_NOTREADY;
      }
      break;
    case 65032: 
      if (timerDevice.hasGoneOff()) {
        localWord = Machine.TIMER_SET;
      } else {
        localWord = Machine.TIMER_UNSET;
      }
      break;
    case 65034: 
      localWord = new Word((int)timerDevice.getInterval());
      break;
    case 65042: 
      localWord = new Word(machine.getMPR());
      break;
    case 65534: 
      localWord = new Word(machine.getMCR());
      break;
    default: 
      if ((paramInt < 0) || (paramInt >= 65535)) {
        return null;
      }
      localWord = memArr[paramInt];
    }
    return localWord;
  }
  









  public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 2) {
      write(paramInt1, Word.parseNum((String)paramObject));
    }
    if (paramInt2 == 0) {
      if (((Boolean)paramObject).booleanValue()) {
        CommandLinePanel.writeToConsole(setBreakPoint(paramInt1));
      } else {
        CommandLinePanel.writeToConsole(clearBreakPoint(paramInt1));
      }
    }
    fireTableRowsUpdated(paramInt1, paramInt1);
  }
  




  public void write(int paramInt1, int paramInt2)
  {
    switch (paramInt1) {
    case 65030: 
      monitorDevice.write((char)paramInt2);
      fireTableCellUpdated(paramInt1, 3);
      break;
    case 65034: 
      timerDevice.setTimer(paramInt2);
      if (paramInt2 == 0) {
        timerDevice.setEnabled(false);
      } else {
        timerDevice.setEnabled(true);
        if (paramInt2 == 1) {
          timerDevice.setTimer(kbDevice);
        }
      }
      break;
    case 65042: 
      machine.setMPR(paramInt2);
      break;
    case 65534: 
      machine.setMCR(paramInt2);
      if ((paramInt2 & 0x8000) == 0) {
        machine.stopExecution(1, true);
      } else {
        machine.updateStatusLabel();
      }
      break;
    }
    memArr[paramInt1].setValue(paramInt2);
    fireTableRowsUpdated(paramInt1, paramInt1);
  }
}
