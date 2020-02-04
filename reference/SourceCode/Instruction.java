import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;








class Instruction
{
  public boolean valid = false;
  

  public int opcode = -1;
  


  public Ops op;
  

  public String label;
  

  public int[] reg = { -1, -1, -1 };
  

  public int offset_immediate = 0;
  

  public String label_ref;
  

  public String stringz;
  

  public boolean n = false;
  

  public boolean z = false;
  

  public boolean p = false;
  public Scanner scan;
  public static final int NO_OP = 0;
  public static final int BR_OP = 0;
  public static final int ADD_OP = 1;
  public static final int LD_OP = 2;
  
  Instruction() { valid = false; }
  

  public static final int ST_OP = 3;
  
  public static final int JSRR_OP = 4;
  
  public static final int JSR_OP = 4;
  
  public static final int AND_OP = 5;
  
  public static final int LDR_OP = 6;
  
  public static final int STR_OP = 7;
  
  public static final int RTI_OP = 8;
  
  public static final int NOT_OP = 9;
  public static final int LDI_OP = 10;
  public static final int STI_OP = 11;
  public static final int RET_OP = 12;
  public static final int JMP_OP = 12;
  public static final int MUL_OP = 13;
  public static final int LEA_OP = 14;
  public static final int TRAP_OP = 15;
  private static final String[] rnames = { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7" };
  


  Instruction(String paramString, int paramInt, boolean paramBoolean)
    throws AsException
  {
    valid = false;
    scan = new Scanner(paramString, paramInt, paramBoolean);
    build();
    scan.scanning = false;
  }
  



  static boolean reg_legal(int paramInt)
  {
    return (paramInt >= 0) && (paramInt <= 7);
  }
  

  void build()
    throws AsException
  {
    String str1 = scan.get_remaining_line();
    

    if (str1.length() == 0)
      return;
    if (str1.charAt(0) == ';') {
      return;
    }
    

    valid = true;
    

    String str2 = scan.get_lexeme_lab(false);
    Ops localOps = Scanner.get_opcode_from_lexeme(str2);
    if ((localOps == null) && (str2.length() != 0)) {
      label = str2;
      boolean bool1 = scan.eat(str2);
      if (!bool1) { scan.interror("Eating label failed.");
      }
    }
    
    str1 = scan.get_remaining_line();
    if (str1.length() == 0)
      return;
    if (str1.charAt(0) == ';') {
      return;
    }
    String str3 = scan.get_lexeme_lab(true);
    localOps = Scanner.get_opcode_from_lexeme(str3);
    if (localOps == null) {
      scan.error("Invalid opcode ('" + str3 + "')");
    }
    op = localOps;
    opcode = opcode;
    boolean bool2 = scan.eat(str3);
    if (!bool2) scan.interror("Eating opcode failed.");
    update_br_fields(localOps);
    

    parse_operands(op);
    

    str1 = scan.get_remaining_line();
    if ((str1 != null) && (str1.length() != 0) && (str1.charAt(0) != ';')) {
      scan.error("Junk at end of line ('" + str1 + "')");
    }
  }
  


  void update_br_fields(Ops paramOps)
  {
    n = false;z = false;p = false;
    switch (op) {
    case 3: 
      n = true;z = true;p = true;
      break;
    case 4: 
      n = true;z = true;
      break;
    case 5: 
      n = true;p = true;
      break;
    case 6: 
      n = true;
      break;
    case 7: 
      z = true;p = true;
      break;
    case 8: 
      z = true;
      break;
    case 9: 
      p = true;
      break;
    case 10: 
      n = true;z = true;p = true;
    }
    
  }
  

  void parse_imm()
    throws AsException
  {
    String str1 = scan.get_remaining_line();
    
    if (str1.length() == 0) {
      scan.error("Immediate expected.");
    }
    
    String str2 = scan.get_lexeme_imm();
    if (str2.length() == 0) {
      scan.error("Invalid immediate; perhaps you need to include a '#', 'x', or 'b' prefix?");
    }
    
    boolean bool = scan.eat(str2);
    if (!bool) scan.interror("Eating immediate failed.");
    offset_immediate = scan.imm_val(str2);
  }
  





  String parse_reg_imm(int paramInt, boolean paramBoolean)
    throws AsException
  {
    String str1 = scan.get_remaining_line();
    
    if (str1.length() == 0) {
      scan.error("Register " + (paramBoolean ? "or immidiate" : "") + "expected at operand " + (paramInt + 1));
    }
    

    String str2 = scan.get_lexeme_reg();
    if (str2.length() == 0) {
      if (paramBoolean) {
        parse_imm();
      } else {
        scan.error("Register expected for operand " + (paramInt + 1));
      }
    } else {
      reg[paramInt] = Scanner.reg_num(str2);
      if (!reg_legal(reg[paramInt])) {
        scan.error("Illegal register ('" + str2 + "')");
      }
      boolean bool = scan.eat(str2);
      if (!bool) scan.interror("Eating register failed.");
    }
    return str1;
  }
  

  void parse_string()
    throws AsException
  {
    String str = scan.get_string();
    if (str == null) { scan.error("Bad string");
    }
    
    boolean bool = scan.eat(str);
    if (!bool) { scan.interror("Eating string failed.");
    }
    
    stringz = scan.get_real_string(str);
  }
  


  void parse_lab_off()
    throws AsException
  {
    String str1 = scan.get_remaining_line();
    String str2 = scan.get_lexeme_lab(false);
    boolean bool; if ((str2.length() == 0) || ((str2.length() != 0) && ((str2.charAt(0) == 'x') || (str2.charAt(0) == 'b'))))
    {

      if (str2.length() != 0) {
        scan.warning("Operand begins with 'x' or 'b'; assuming it's an immediate offset (i.e., not a label)");
      }
      
      str2 = scan.get_lexeme_imm();
      if (str2.length() == 0) {
        scan.error("Bad immediate value.");
      }
      bool = scan.eat(str2);
      if (!bool) scan.interror("Eating immedeate failed.");
      offset_immediate = scan.imm_val(str2);
    } else {
      bool = scan.eat(str2);
      if (!bool) scan.interror("Eating label failed.");
      label_ref = str2;
    }
  }
  






  void parse_operands(Ops paramOps)
    throws AsException
  {
    scan.get_remaining_line();
    boolean bool;
    switch (op) {
    case 0: 
    case 1: 
    case 2: 
    case 21: 
      parse_reg_imm(0, false);
      bool = scan.eat(",");
      if (!bool) scan.error("Comma expected after operand 1");
      parse_reg_imm(1, false);
      if (op != 21) {
        bool = scan.eat(",");
        if (!bool) scan.error("Comma expected after operand 2");
        parse_reg_imm(2, true);
      }
      

      break;
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
      parse_lab_off();
      break;
    

    case 11: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
      parse_reg_imm(0, false);
      bool = scan.eat(",");
      if (!bool) { scan.error("Comma expected between operands");
      }
      
      parse_lab_off();
      break;
    

    case 12: 
    case 13: 
      parse_reg_imm(0, false);
      bool = scan.eat(",");
      if (!bool) scan.error("Comma expected after operand 1");
      parse_reg_imm(1, false);
      bool = scan.eat(",");
      if (!bool) { scan.error("Comma expected after operand 2");
      }
      
      parse_lab_off();
      break;
    
    case 18: 
    case 37: 
      break;
    

    case 19: 
    case 24: 
    case 25: 
      parse_reg_imm(0, false);
      break;
    

    case 20: 
    case 33: 
      parse_lab_off();
      break;
    

    case 36: 
      parse_string();
      break;
    case 22: 
    case 23: 
      reg[0] = 7;
      break;
    
    case 27: 
      offset_immediate = 32;
      break;
    case 28: 
      offset_immediate = 33;
      break;
    case 29: 
      offset_immediate = 34;
      break;
    case 30: 
      offset_immediate = 35;
      break;
    case 31: 
      offset_immediate = 36;
      break;
    case 32: 
      offset_immediate = 37;
      break;
    

    case 26: 
    case 34: 
    case 35: 
      parse_imm();
    }
    
  }
  




  int next_address(int paramInt)
  {
    if (op != null) {
      if ((paramInt == -1) && (op.op != 35)) return -2;
      switch (op.op) {
      case 35: 
        return offset_immediate;
      case 34: 
        return paramInt + offset_immediate;
      case 36: 
        return paramInt + stringz.length() + 1;
      }
      return paramInt + 1;
    }
    return paramInt;
  }
  





  List encode()
    throws AsException
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    
    if (op == null) return null;
    if (op.opcode != -1) {
      i = add_field(i, op.opcode, 15, 12);
    }
    int j;
    switch (op.op) {
    case 0: 
    case 1: 
    case 2: 
    case 21: 
      i = add_field(i, reg[0], 11, 9);
      i = add_field(i, reg[1], 8, 6);
      if (op.op != 21) {
        if (reg[2] == -1) {
          check_range(offset_immediate, -16, 15);
          i = add_field(i, 1, 5, 5);
          i = add_field(i, offset_immediate, 4, 0);
        } else {
          i = add_field(i, reg[2], 2, 0);
        }
      } else {
        i = add_field(i, 63, 5, 0);
      }
      break;
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
      check_range(offset_immediate, 65280, 255);
      i = add_field(i, n ? 1 : 0, 11, 11);
      i = add_field(i, z ? 1 : 0, 10, 10);
      i = add_field(i, p ? 1 : 0, 9, 9);
      i = add_field(i, offset_immediate, 8, 0);
      break;
    case 11: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
      check_range(offset_immediate, 65280, 255);
      i = add_field(i, reg[0], 11, 9);
      i = add_field(i, offset_immediate, 8, 0);
      break;
    case 12: 
    case 13: 
      check_range(offset_immediate, -32, 31);
      i = add_field(i, reg[0], 11, 9);
      i = add_field(i, reg[1], 8, 6);
      i = add_field(i, offset_immediate, 5, 0);
      break;
    case 18: 
      break;
    
    case 37: 
      scan.interror("Trying to generate instruction for '.end'.");
      break;
    
    case 19: 
    case 24: 
      i = add_field(i, reg[0], 8, 6);
      break;
    
    case 25: 
      i = add_field(i, reg[0], 8, 6);
      i = add_field(i, 1, 0, 0);
      break;
    
    case 20: 
      check_range(offset_immediate, 64512, 1023);
      i = add_field(i, 1, 11, 11);
      i = add_field(i, offset_immediate, 10, 0);
      break;
    case 33: 
      check_range(offset_immediate, 32768, 65535);
      i = offset_immediate;
      break;
    
    case 22: 
      i = add_field(i, 7, 8, 6);
      break;
    
    case 23: 
      i = add_field(i, 7, 8, 6);
      i = add_field(i, 1, 0, 0);
      break;
    
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
      check_range(offset_immediate, 0, 255);
      i = add_field(i, offset_immediate, 7, 0);
      break;
    case 35: 
      check_range(offset_immediate, 0, 65535);
      i = offset_immediate;
      break;
    case 34: 
      check_range(offset_immediate, 1, 65535);
      for (j = 0; j < offset_immediate; j++) {
        localArrayList.add(new Integer(0));
      }
      return localArrayList;
    case 36: 
      for (j = 0; j < stringz.length(); j++) {
        localArrayList.add(new Integer(stringz.charAt(j)));
      }
      
      localArrayList.add(new Integer(0));
      return localArrayList;
    }
    
    localArrayList.add(new Integer(i));
    
    return localArrayList;
  }
  

  void check_range(int paramInt1, int paramInt2, int paramInt3)
    throws AsException
  {
    if ((paramInt1 < paramInt2) || (paramInt1 > paramInt3)) {
      scan.error("Immediate operand (#" + paramInt1 + ") out of range (#" + paramInt2 + " to #" + paramInt3 + ")");
    }
  }
  









  static int add_field(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt4 > paramInt3) {
      return add_field(paramInt1, paramInt2, paramInt4, paramInt3);
    }
    
    int i = -1;
    i <<= paramInt3 - paramInt4 + 1;
    i ^= 0xFFFFFFFF;
    i <<= paramInt4;
    
    return i & paramInt2 << paramInt4 | (i ^ 0xFFFFFFFF) & paramInt1;
  }
  


  void print_lab_off()
  {
    if (label_ref != null) {
      System.out.println(label_ref);
    } else {
      System.out.println("#" + offset_immediate);
    }
  }
  

  void print()
  {
    if (label != null) {
      System.out.print(label + "\t");
    } else {
      System.out.print("\t");
    }
    
    if (op == null) { return;
    }
    System.out.print(op.opstring + "\t");
    
    switch (op.op) {
    case 0: 
    case 1: 
    case 2: 
    case 21: 
      System.out.print("R" + reg[0] + ",R" + reg[1]);
      if (op.op != 21) {
        if (reg[2] != -1) {
          System.out.println(",R" + reg[2]);
        } else {
          System.out.println(",#" + offset_immediate);
        }
      }
      break;
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
      print_lab_off();
      break;
    case 11: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
      System.out.print("R" + reg[0] + ",");
      print_lab_off();
      break;
    case 12: 
    case 13: 
      System.out.print("R" + reg[0] + "," + "R" + reg[1] + ",");
      print_lab_off();
      break;
    
    case 18: 
    case 37: 
      System.out.println();
      break;
    
    case 19: 
    case 24: 
    case 25: 
      System.out.print("R" + reg[0]);
      break;
    
    case 20: 
    case 33: 
      print_lab_off();
      break;
    
    case 22: 
    case 23: 
      break;
    
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
      break;
    
    case 26: 
    case 34: 
    case 35: 
      System.out.print("#" + offset_immediate);
    }
    
  }
  




  public static void execute(Word paramWord, Machine paramMachine)
    throws ExceptionException
  {
    int i3 = paramWord.getZext(15, 12);
    int i; int i4 = i = paramWord.getZext(8, 6);
    int j = paramWord.getZext(2, 0);
    int k = paramWord.getZext(11, 9);
    int m = paramWord.getSext(4, 0);
    int i1 = paramWord.getSext(8, 0);
    int i2 = paramWord.getSext(10, 0);
    int i5 = paramWord.getSext(5, 0);
    int i6 = paramWord.getZext(7, 0);
    
    RegisterFile localRegisterFile = paramMachine.getRegisterFile();
    Memory localMemory = paramMachine.getMemory();
    int i7 = paramMachine.getPC() + 1;
    int i8;
    int i9;
    int i10; int i11; switch (i3) {
    case 1: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          i8 = localRegisterFile.getRegister(i) + localRegisterFile.getRegister(j);
          localRegisterFile.write(k, i8);
        } else {
          throw new IllegalInstructionException("ADD: Bits 4 and 3 are not zero");
        }
      } else { i8 = localRegisterFile.getRegister(i) + m;
        localRegisterFile.write(k, i8);
      }
      localRegisterFile.setNZP(i8);
      break;
    
    case 13: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          i8 = localRegisterFile.getRegister(i) * localRegisterFile.getRegister(j);
          localRegisterFile.write(k, i8);
        } else {
          throw new IllegalInstructionException("ADD: Bits 4 and 3 are not zero");
        }
      } else { i8 = localRegisterFile.getRegister(i) * m;
        localRegisterFile.write(k, i8);
      }
      localRegisterFile.setNZP(i8);
      break;
    
    case 5: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          i8 = localRegisterFile.getRegister(i) & localRegisterFile.getRegister(j);
          localRegisterFile.write(k, i8);
        } else {
          throw new IllegalInstructionException("AND: Bits 4 and 3 are not zero");
        }
      } else { i8 = localRegisterFile.getRegister(i) & m;
        localRegisterFile.write(k, i8);
      }
      localRegisterFile.setNZP(i8);
      break;
    
    case 0: 
      i9 = paramWord.getBit(11) == 1 ? 1 : 0;
      i10 = paramWord.getBit(10) == 1 ? 1 : 0;
      i11 = paramWord.getBit(9) == 1 ? 1 : 0;
      

      boolean bool1 = localRegisterFile.getN();
      boolean bool2 = localRegisterFile.getZ();
      boolean bool3 = localRegisterFile.getP();
      if ((i9 == 0) && (i10 == 0) && (i11 == 0))
        throw new IllegalInstructionException("BR: At least one of n/z/p has to be set");
      if (((i9 != 0) && (bool1)) || ((i10 != 0) && (bool2)) || ((i11 != 0) && (bool3))) {
        i7 += i1;
      }
      
      break;
    case 4: 
      i9 = localRegisterFile.getRegister(i4);
      localRegisterFile.write(7, i7);
      if (paramWord.getBit(11) == 0) {
        if (paramWord.getZext(5, 0) != 0) {
          throw new IllegalInstructionException("JSRR: Bits[5-0] should be zero");
        }
        i7 = i9;
      } else {
        i7 += i2;
      }
      break;
    
    case 2: 
      i9 = i7 + i1;
      paramMachine.checkAddr(i9);
      i8 = localMemory.read(i9).getValue();
      localRegisterFile.write(k, i8);
      localRegisterFile.setNZP(i8);
      break;
    
    case 10: 
      i9 = i7 + i1;
      paramMachine.checkAddr(i9);
      i9 = localMemory.read(i9).getValue();
      paramMachine.checkAddr(i9);
      i8 = localMemory.read(i9).getValue();
      localRegisterFile.write(k, i8);
      localRegisterFile.setNZP(i8);
      break;
    
    case 6: 
      i9 = localRegisterFile.getRegister(i4) + i5;
      paramMachine.checkAddr(i9);
      i8 = localMemory.read(i9).getValue();
      localRegisterFile.write(k, i8);
      localRegisterFile.setNZP(i8);
      break;
    
    case 14: 
      i8 = i7 + i1;
      localRegisterFile.write(k, i8);
      localRegisterFile.setNZP(i8);
      break;
    
    case 9: 
      if (paramWord.getZext(5, 0) != 63) {
        throw new IllegalInstructionException("NOT: Bits[5-0] should be all 1s");
      }
      i8 = localRegisterFile.getRegister(i) ^ 0xFFFFFFFF;
      localRegisterFile.write(k, i8);
      localRegisterFile.setNZP(i8);
      
      break;
    
    case 12: 
      if (paramWord.getZext(11, 9) != 0)
        throw new IllegalInstructionException("JMP: Bits[11-9] should be all 0s");
      if ((i4 != 7) && (i5 != 0))
        throw new IllegalInstructionException("JMP: Bits[5-0] should be all 0s");
      if ((i4 == 7) && (i5 != 0) && (i5 != 1)) {
        throw new IllegalInstructionException("RET/RTT: Bits[5-0] are invalid");
      }
      if (paramWord.getZext(0, 0) == 1)
      {
        localRegisterFile.setPrivMode(false);
      }
      i7 = localRegisterFile.getRegister(i);
      
      break;
    
    case 8: 
      if (paramWord.getZext(11, 0) != 0) {
        throw new IllegalInstructionException("RTI: Bits[11-0] should be all 0s");
      }
      if (localRegisterFile.getPrivMode()) {
        i9 = localRegisterFile.getRegister(6);
        i10 = localMemory.read(i9).getValue();
        i7 = i10;
        localRegisterFile.write(6, i9 + 1);
        i11 = localMemory.read(i9).getValue();
        localRegisterFile.setPSR(i11);
      } else {
        throw new IllegalInstructionException("RTI can only be executed in privileged mode");
      }
      

      break;
    case 3: 
      i9 = i7 + i1;
      paramMachine.checkAddr(i9);
      localMemory.write(i9, localRegisterFile.getRegister(k));
      break;
    
    case 11: 
      i9 = i7 + i1;
      paramMachine.checkAddr(i9);
      i9 = localMemory.read(i9).getValue();
      paramMachine.checkAddr(i9);
      localMemory.write(i9, localRegisterFile.getRegister(k));
      break;
    
    case 7: 
      i9 = localRegisterFile.getRegister(i4) + i5;
      paramMachine.checkAddr(i9);
      localMemory.write(i9, localRegisterFile.getRegister(k));
      break;
    
    case 15: 
      if (paramWord.getZext(11, 8) != 0) {
        throw new IllegalInstructionException("TRAP: Bits[11-8] should be all 0s");
      }
      
      localRegisterFile.setPrivMode(true);
      



      localRegisterFile.write(7, i7);
      
      i7 = localMemory.read(i6).getValue();
      
      break;
    
    default: 
      System.out.println("Instruction.execute: got default case");
      throw new IllegalInstructionException("Bad opcode (" + i3 + ")");
    }
    
    
    paramMachine.setPC(i7);
  }
  




  public static String printFill(Word paramWord)
  {
    return ".FILL " + paramWord.toHex();
  }
  






  private static String get_addr(int paramInt1, int paramInt2, Machine paramMachine)
  {
    int i = paramInt1 + paramInt2 + 1;
    String str = paramMachine.lookupSym(i);
    
    if (str != null) {
      return str;
    }
    return Word.toHex(i);
  }
  







  public static String disassemble(Word paramWord, int paramInt, Machine paramMachine)
  {
    int i3 = paramWord.getZext(15, 12);
    int i; int i4 = i = paramWord.getZext(8, 6);
    int j = paramWord.getZext(2, 0);
    int k = paramWord.getZext(11, 9);
    int m = paramWord.getSext(4, 0);
    int i1 = paramWord.getSext(8, 0);
    int i2 = paramWord.getSext(10, 0);
    int i5 = paramWord.getSext(5, 0);
    int i6 = paramWord.getZext(7, 0);
    String str1 = null;
    String str2 = " ";
    String str3 = "#";
    String str4 = ", ";
    int i7;
    switch (i3) {
    case 1: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          str1 = "ADD" + str2 + rnames[k] + str4 + rnames[i] + str4 + rnames[j];
        }
        else
          str1 = printFill(paramWord);
      } else {
        str1 = "ADD" + str2 + rnames[k] + str4 + rnames[i] + str4 + str3 + m;
      }
      break;
    
    case 13: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          str1 = "MUL" + str2 + rnames[k] + str4 + rnames[i] + str4 + rnames[j];
        }
        else
          str1 = printFill(paramWord);
      } else {
        str1 = "MUL" + str2 + rnames[k] + str4 + rnames[i] + str4 + str3 + m;
      }
      break;
    
    case 5: 
      if (paramWord.getBit(5) == 0) {
        if (paramWord.getZext(4, 3) == 0) {
          str1 = "AND" + str2 + rnames[k] + str4 + rnames[i] + str4 + rnames[j];
        }
        else
          str1 = printFill(paramWord);
      } else {
        str1 = "AND" + str2 + rnames[k] + str4 + rnames[i] + str4 + str3 + m;
      }
      break;
    
    case 0: 
      i7 = paramWord.getBit(11) == 1 ? 1 : 0;
      int i8 = paramWord.getBit(10) == 1 ? 1 : 0;
      int i9 = paramWord.getBit(9) == 1 ? 1 : 0;
      
      if ((i7 != 0) && (i8 != 0) && (i9 != 0)) {
        str1 = "BR" + str2 + get_addr(paramInt, i1, paramMachine);
      } else if ((i7 != 0) || (i8 != 0) || (i9 != 0)) {
        str1 = "BR";
        if (i7 != 0)
          str1 = str1 + "n";
        if (i8 != 0)
          str1 = str1 + "z";
        if (i9 != 0)
          str1 = str1 + "p";
        str1 = str1 + str2 + get_addr(paramInt, i1, paramMachine);
      } else {
        str1 = printFill(paramWord); }
      break;
    
    case 12: 
      if ((k == 0) && (i5 == 0)) {
        if (i4 == 7) {
          str1 = "RET";
        } else {
          str1 = "JMP" + str2 + rnames[i4];
        }
      } else if ((k == 0) && (i5 == 1)) {
        str1 = "RTT";
      } else {
        str1 = printFill(paramWord);
      }
      break;
    
    case 4: 
      if (paramWord.getBit(11) == 0) {
        if ((paramWord.getZext(10, 9) == 0) && (i5 == 0)) {
          str1 = "JSRR" + str2 + rnames[i4];
        } else
          str1 = printFill(paramWord);
      } else {
        str1 = "JSR" + str2 + get_addr(paramInt, i2, paramMachine);
      }
      break;
    
    case 2: 
      str1 = "LD" + str2 + rnames[k] + str4 + get_addr(paramInt, i1, paramMachine);
      break;
    
    case 10: 
      str1 = "LDI" + str2 + rnames[k] + str4 + get_addr(paramInt, i1, paramMachine);
      break;
    
    case 6: 
      str1 = "LDR" + str2 + rnames[k] + str4 + rnames[i] + str4 + i5;
      
      break;
    
    case 14: 
      str1 = "LEA" + str2 + rnames[k] + str4 + get_addr(paramInt, i1, paramMachine);
      break;
    
    case 9: 
      i7 = paramWord.getZext(5, 0);
      if (i7 == 63) {
        str1 = "NOT" + str2 + rnames[k] + str4 + rnames[i];
      } else
        str1 = printFill(paramWord);
      break;
    
    case 8: 
      if (i2 == 0) {
        str1 = "RTI";
      } else
        str1 = printFill(paramWord);
      break;
    
    case 3: 
      str1 = "ST" + str2 + rnames[k] + str4 + get_addr(paramInt, i1, paramMachine);
      break;
    
    case 11: 
      str1 = "STI" + str2 + rnames[k] + str4 + get_addr(paramInt, i1, paramMachine);
      break;
    
    case 7: 
      str1 = "STR" + str2 + rnames[k] + str4 + rnames[i4] + str4 + str3 + i5;
      
      break;
    
    case 15: 
      i7 = paramWord.getZext(11, 8);
      if (i7 == 0) {
        str1 = "TRAP" + str2 + str3 + i6;
      } else {
        str1 = printFill(paramWord);
      }
      break;
    }
    return str1;
  }
  




  static boolean nextOver(Word paramWord)
  {
    int i = paramWord.getZext(15, 12);
    
    return (i == 4) || (i == 4) || (i == 15);
  }
}
