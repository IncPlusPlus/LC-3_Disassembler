




public class Ops
{
  static final int ADD = 0;
  static final int AND = 1;
  static final int MUL = 2;
  static final int BRNZP = 3;
  static final int BRNZ = 4;
  static final int BRNP = 5;
  static final int BRN = 6;
  static final int BRZP = 7;
  static final int BRZ = 8;
  static final int BRP = 9;
  static final int BR = 10;
  static final int LEA = 11;
  static final int LDR = 12;
  static final int STR = 13;
  static final int LDI = 14;
  static final int STI = 15;
  static final int LD = 16;
  static final int ST = 17;
  static final int RTI = 18;
  static final int JSRR = 19;
  static final int JSR = 20;
  static final int NOT = 21;
  static final int RET = 22;
  static final int RTT = 23;
  static final int JMP = 24;
  static final int JMPT = 25;
  static final int TRAP = 26;
  static final int GETC = 27;
  static final int OUT = 28;
  static final int PUTS = 29;
  static final int IN = 30;
  static final int PUTSP = 31;
  static final int HALT = 32;
  static final int FILL = 33;
  static final int BLKW = 34;
  static final int ORIG = 35;
  static final int STRINGZ = 36;
  static final int END = 37;
  static final int LAST = 38;
  static final String[] opstrings = { "add", "and", "mul", "brnzp", "brnz", "brnp", "brn", "brzp", "brz", "brp", "br", "lea", "ldr", "str", "ldi", "sti", "ld", "st", "rti", "jsrr", "jsr", "not", "ret", "rtt", "jmp", "jmpt", "trap", "getc", "out", "puts", "in", "putsp", "halt", ".fill", ".blkw", ".orig", ".stringz", ".end" };
  







  static final int[] opcodes = { 1, 5, 13, 0, 0, 0, 0, 0, 0, 0, 0, 14, 6, 7, 10, 11, 2, 3, 8, 4, 4, 9, 12, 12, 12, 12, 15, 15, 15, 15, 15, 15, 15, -1, -1, -1, -1, -1 };
  
  public final String opstring;
  
  public final int opcode;
  
  public final int op;
  

  Ops(int paramInt)
  {
    op = paramInt;
    opstring = opstrings[paramInt];
    opcode = opcodes[paramInt];
  }
  
  static Ops buildOps(String paramString) {
    for (int i = 0; i < 38; i++) {
      if (paramString.equalsIgnoreCase(opstrings[i])) {
        return new Ops(i);
      }
    }
    return null;
  }
}
