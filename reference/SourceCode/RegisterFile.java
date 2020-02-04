public class RegisterFile extends LC3TableModel
{
  private static final long serialVersionUID = 103L;
  private static final int NUM_REGISTERS = 8;
  private static final int NUM_ROWS = 12;
  private static final int PC_ROW = 8;
  private static final int MPR_ROW = 9;
  private static final int PSR_ROW = 10;
  private static final int CC_ROW = 11;
  private final String[] colNames = { "Register", "Value", "Register", "Value" };
  
  private final Machine machine;
  private final Word PC;
  private final Word MPR;
  private final Word PSR;
  private final Word MCR;
  private final Word CC;
  private final Word[] regArr = new Word[8];
  
  private static String[] indNames = { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "PC", "MPR", "PSR", "CC" };
  


  private static int[] indRow = { 0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5 };
  private static int[] indCol = { 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3 };
  



  public RegisterFile(Machine paramMachine)
  {
    machine = paramMachine;
    for (int i = 0; i < 8; i++) {
      regArr[i] = new Word(0);
    }
    PC = new Word();
    MPR = new Word(0);
    MCR = new Word(32768);
    PSR = new Word(2);
    setPrivMode(true);
    



    CC = PSR;
  }
  



  public void reset()
  {
    for (int i = 0; i < 8; i++) {
      regArr[i].reset();
    }
    PC.reset();
    
    setZ();
    setPrivMode(true);
    fireTableDataChanged();
  }
  



  public int getRowCount()
  {
    return 6;
  }
  
  public int getColumnCount() { return colNames.length; }
  
  public String getColumnName(int paramInt) {
    return colNames[paramInt];
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) { return (paramInt2 == 1) || (paramInt2 == 3); }
  






  public Object getValueAt(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      return indNames[paramInt1];
    if (paramInt2 == 1)
      return regArr[paramInt1].toHex();
    if (paramInt2 == 2)
      return indNames[(paramInt1 + 6)];
    if (paramInt2 == 3) {
      if (paramInt1 < 2)
        return regArr[(paramInt1 + 6)].toHex();
      if (paramInt1 == 2)
        return PC.toHex();
      if (paramInt1 == 3)
        return MPR.toHex();
      if (paramInt1 == 4)
        return PSR.toHex();
      if (paramInt1 == 5) {
        return printCC();
      }
    }
    return null;
  }
  






  public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 1) {
      regArr[paramInt1].setValue(Word.parseNum((String)paramObject));
    } else if (paramInt2 == 3) {
      if (paramInt1 < 2) {
        regArr[(paramInt1 + 6)].setValue(Word.parseNum((String)paramObject));
      } else { if (paramInt1 == 5) {
          setNZP((String)paramObject);
          return; }
        if ((paramObject == null) && (paramInt1 == 3))
        {








          fireTableCellUpdated(paramInt1, paramInt2);
          return;
        }
        int i = Word.parseNum((String)paramObject);
        if (paramInt1 == 2)
        {
          machine.setPC(i);
          machine.scrollToPC();
        } else if (paramInt1 == 3) {
          setMPR(i);
        } else if (paramInt1 == 4) {
          setPSR(i);
        }
      }
    }
    fireTableCellUpdated(paramInt1, paramInt2);
  }
  




  public Word getPC() { return PC; }
  
  public void setPC(int paramInt) { PC.setValue(paramInt);
    fireTableCellUpdated(indRow[8], indCol[8]); }
  
  public void incPC(int paramInt) { setPC(PC.getValue() + paramInt); }
  








  public String printRegister(int paramInt)
    throws IndexOutOfBoundsException
  {
    if ((paramInt < 0) || (paramInt >= 8)) {
      throw new IndexOutOfBoundsException("Register index must be from 0 to 7");
    }
    return regArr[paramInt].toHex();
  }
  



  public int getRegister(int paramInt)
    throws IndexOutOfBoundsException
  {
    if ((paramInt < 0) || (paramInt >= 8)) {
      throw new IndexOutOfBoundsException("Register index must be from 0 to 7");
    }
    return regArr[paramInt].getValue();
  }
  
  public void write(int paramInt1, int paramInt2)
  {
    regArr[paramInt1].setValue(paramInt2);
    fireTableCellUpdated(indRow[paramInt1], indCol[paramInt1]);
  }
  





  public boolean getN() { return PSR.getBit(2) == 1; }
  public boolean getZ() { return PSR.getBit(1) == 1; }
  public boolean getP() { return PSR.getBit(0) == 1; }
  public boolean getPrivMode() { return PSR.getBit(15) == 1; }
  




  public String printCC()
  {
    if ((!(getN() ^ getZ() ^ getP())) || ((getN()) && (getZ()) && (getP())))
    {
      return "invalid";
    }
    if (getN())
      return "N";
    if (getZ())
      return "Z";
    if (getP()) {
      return "P";
    }
    return "unset";
  }
  



  public int getPSR()
  {
    return PSR.getValue();
  }
  



  public void setNZP(int paramInt)
  {
    int i = PSR.getValue();
    i &= 0xFFFFFFF8;
    paramInt &= 0xFFFF;
    
    if ((paramInt & 0x8000) != 0) {
      i |= 0x4;
    } else if (paramInt == 0) {
      i |= 0x2;
    } else {
      i |= 0x1;
    }
    setPSR(i);
  }
  




  public void setNZP(String paramString)
  {
    paramString = paramString.toLowerCase().trim();
    if ((!paramString.equals("n")) && (!paramString.equals("z")) && (!paramString.equals("p"))) {
      CommandLinePanel.writeToConsole("Condition codes must be set as one of `n', `z' or `p'");
      return;
    }
    if (paramString.equals("n")) {
      setN();
    } else if (paramString.equals("z")) {
      setZ();
    } else {
      setP();
    }
  }
  
  public void setN() { setNZP(32768); }
  public void setZ() { setNZP(0); }
  public void setP() { setNZP(1); }
  
  public void setPrivMode(boolean paramBoolean) { int i = PSR.getValue();
    if (!paramBoolean) {
      i &= 0x7FFF;
    } else {
      i |= 0x8000;
    }
    setPSR(i);
  }
  



  public void setClockMCR(boolean paramBoolean)
  {
    if (paramBoolean) {
      setMCR(MCR.getValue() | 0x8000);
    } else {
      setMCR(MCR.getValue() & 0x7FFF);
    }
  }
  


  public boolean getClockMCR()
  {
    return (getMCR() & 0x8000) != 0;
  }
  


  public void setMCR(int paramInt)
  {
    MCR.setValue(paramInt);
  }
  


  public int getMCR()
  {
    return MCR.getValue();
  }
  



  public void setPSR(int paramInt)
  {
    PSR.setValue(paramInt);
    fireTableCellUpdated(indRow[10], indCol[10]);
    fireTableCellUpdated(indRow[11], indCol[11]);
  }
  







  public int getMPR()
  {
    return MPR.getValue();
  }
  


  public void setMPR(int paramInt)
  {
    MPR.setValue(paramInt);
    fireTableCellUpdated(indRow[9], indCol[9]);
  }
  







  public String toString()
  {
    String str = "[";
    for (int i = 0; i < 8; i++) {
      str = str + "R" + i + ": " + regArr[i].toHex() + (i != 7 ? "," : "");
    }
    str = str + "]";
    str = str + "\nPC = " + PC.toHex();
    str = str + "\nMPR = " + MPR.toHex();
    str = str + "\nPSR = " + PSR.toHex();
    str = str + "\nCC = " + printCC();
    return str;
  }
}
