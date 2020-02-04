import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;
import java.util.TreeSet;



































public class CommandLine
{
  private Machine mac;
  private LC3GUI GUI;
  private LinkedList commandQueue;
  private Stack prevHistoryStack;
  private Stack nextHistoryStack;
  public static final String PROMPT = "\n==>";
  private Hashtable commands;
  private TreeSet commandsSet;
  
  public CommandLine(Machine paramMachine)
  {
    mac = paramMachine;
    
    commands = new Hashtable();
    setupCommands();
    

    commandsSet = new TreeSet(new Comparator() {
      public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
        CommandLine.Command localCommand1 = (CommandLine.Command)paramAnonymousObject1;
        CommandLine.Command localCommand2 = (CommandLine.Command)paramAnonymousObject2;
        String str1 = localCommand1.getUsage().split("\\s+")[0];
        String str2 = localCommand2.getUsage().split("\\s+")[0];
        return str1.compareTo(str2);
      }
      
      public boolean equals(Object paramAnonymousObject) { CommandLine.Command localCommand = (CommandLine.Command)paramAnonymousObject;
        
        String str1 = localCommand.getUsage().split("\\s+")[0];
        String str2 = ((CommandLine.Command)this).getUsage().split("\\s+")[0];
        return str1.equals(str2);
      }
    });
    commandsSet.addAll(commands.values());
    
    commandQueue = new LinkedList();
    prevHistoryStack = new Stack();
    nextHistoryStack = new Stack();
  }
  









  public void scheduleCommand(String paramString)
  {
    if (paramString.equalsIgnoreCase("stop")) {
      commandQueue.addFirst(paramString);
    } else {
      commandQueue.add(paramString);
    }
  }
  










  public void scheduleScriptCommands(ArrayList paramArrayList)
  {
    ListIterator localListIterator = paramArrayList.listIterator(paramArrayList.size());
    while (localListIterator.hasPrevious()) {
      String str = (String)localListIterator.previous();
      commandQueue.addFirst(str);
    }
  }
  

  public boolean hasMoreCommands()
  {
    return commandQueue.size() != 0;
  }
  
  public String getNextCommand()
  {
    return (String)commandQueue.removeFirst();
  }
  




  public boolean hasQueuedStop()
  {
    return ((String)commandQueue.getFirst()).equalsIgnoreCase("stop");
  }
  









  public void addToHistory(String paramString)
  {
    if (prevHistoryStack.empty()) {
      prevHistoryStack.push(paramString);
    }
    else if (!prevHistoryStack.peek().equals(paramString)) {
      prevHistoryStack.push(paramString);
    }
  }
  






  public String getPrevHistory()
  {
    if (prevHistoryStack.empty()) {
      return null;
    }
    String str = (String)prevHistoryStack.pop();
    nextHistoryStack.push(str);
    return str;
  }
  






  public String getNextHistory()
  {
    if (nextHistoryStack.empty()) {
      return null;
    }
    String str = (String)nextHistoryStack.pop();
    prevHistoryStack.push(str);
    return str;
  }
  





  private void resetHistoryStack()
  {
    while (!nextHistoryStack.empty()) {
      prevHistoryStack.push(nextHistoryStack.pop());
    }
  }
  








  private void setupCommands()
  {
    commands.put("help", new CommandLine.Command() {
      public String getUsage() {
        return "h[elp] [command]";
      }
      
      public String getHelp() { return "Print out help for all available commands, or for just a specified command."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt > 2) {
          return getUsage();
        }
        if (paramAnonymousInt == 1) {
          localObject = "";
          Iterator localIterator = commandsSet.iterator();
          while (localIterator.hasNext()) {
            CommandLine.Command localCommand = (CommandLine.Command)localIterator.next();
            




            String str1 = localCommand.getUsage();
            String str2 = str1.split("\\s+")[0];
            localObject = (String)localObject + str2 + " usage: " + str1 + "\n";
          }
          return localObject;
        }
        Object localObject = (CommandLine.Command)commands.get(paramAnonymousArrayOfString[1].toLowerCase());
        if (localObject == null) {
          return paramAnonymousArrayOfString[1] + ": command not found";
        }
        return "usage: " + ((CommandLine.Command)localObject).getUsage() + "\n   " + ((CommandLine.Command)localObject).getHelp();
      }
      

    });
    commands.put("h", commands.get("help"));
    
    commands.put("quit", new CommandLine.Command() {
      public String getUsage() {
        return "quit";
      }
      
      public String getHelp() { return "Quit the simulator."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 1) {
          return getUsage();
        }
        return null;
      }
      
    });
    commands.put("next", new CommandLine.Command() {
      public String getUsage() {
        return "n[ext]";
      }
      
      public String getHelp() { return "Executes the next instruction."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) throws ExceptionException {
        if (paramAnonymousInt != 1) {
          return getUsage();
        }
        mac.executeNext();
        return "";
      }
    });
    commands.put("n", commands.get("next"));
    
    commands.put("step", new CommandLine.Command() {
      public String getUsage() {
        return "s[tep]";
      }
      
      public String getHelp() { return "Steps into the next instruction."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) throws ExceptionException {
        mac.executeStep();
        return "";
      }
    });
    commands.put("s", commands.get("step"));
    
    commands.put("continue", new CommandLine.Command() {
      public String getUsage() {
        return "c[ontinue]";
      }
      
      public String getHelp() { return "Continues running instructions until next breakpoint is hit."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) throws ExceptionException {
        if (GUI == null) {
          System.out.println("use the 'stop' command to interrupt execution");
        } else {
          CommandLinePanel.writeToConsole("use the 'stop' command to interrupt execution");
        }
        mac.executeMany();
        return "";
      }
    });
    commands.put("c", commands.get("continue"));
    
    commands.put("stop", new CommandLine.Command() {
      public String getUsage() {
        return "stop";
      }
      
      public String getHelp() { return "Stops execution temporarily."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        return mac.stopExecution(true);
      }
      
    });
    commands.put("reset", new CommandLine.Command() {
      public String getUsage() {
        return "reset";
      }
      
      public String getHelp() { return "Resets the machine and simulator."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 1) {
          return getUsage();
        }
        



        mac.stopExecution(false);
        mac.reset();
        return "System reset";
      }
      
    });
    commands.put("print", new CommandLine.Command() {
      public String getUsage() {
        return "p[rint]";
      }
      
      public String getHelp() { return "Prints out all registers, PC, MPR and PSR."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 1) {
          return getUsage();
        }
        return mac.printRegisters();
      }
    });
    commands.put("p", commands.get("print"));
    
    commands.put("input", new CommandLine.Command() {
      public String getUsage() {
        return "input <filename>";
      }
      
      public String getHelp() { return "Specifies a file to read the input from instead of keyboard device (simulator must be restarted to restore normal keyboard input)."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 2) {
          return getUsage();
        }
        return mac.setKeyboardInputStream(new File(paramAnonymousArrayOfString[1]));
      }
      

    });
    commands.put("break", new CommandLine.Command() {
      public String getUsage() {
        return "b[reak] [ set | clear ] [ mem_addr | label ]";
      }
      
      public String getHelp() { return "Sets or clears break point at specified memory address or label."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 3) {
          return getUsage();
        }
        if (paramAnonymousArrayOfString[1].toLowerCase().equals("set"))
          return mac.setBreakPoint(paramAnonymousArrayOfString[2]);
        if (paramAnonymousArrayOfString[1].toLowerCase().equalsIgnoreCase("clear")) {
          return mac.clearBreakPoint(paramAnonymousArrayOfString[2]);
        }
        return getUsage();
      }
      

    });
    commands.put("b", commands.get("break"));
    
    commands.put("script", new CommandLine.Command() {
      public String getUsage() {
        return "script <filename>";
      }
      
      public String getHelp() { return "Specifies a file from which to read commands."; }
      








      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt)
      {
        if (paramAnonymousInt != 2) {
          return getUsage();
        }
        File localFile = new File(paramAnonymousArrayOfString[1]);
        try
        {
          BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
          
          ArrayList localArrayList = new ArrayList();
          for (;;) {
            String str = localBufferedReader.readLine();
            if (str == null) {
              break;
            }
            
            localArrayList.add("@" + str);
          }
          
          scheduleScriptCommands(localArrayList);
        } catch (IOException localIOException) {
          return localIOException.getMessage();
        }
        return "";
      }
      

    });
    commands.put("load", new CommandLine.Command() {
      public String getUsage() {
        return "l[oa]d <filename>";
      }
      
      public String getHelp() { return "Loads an object file into the memory."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt != 2) {
          return getUsage();
        }
        return mac.loadObjectFile(new File(paramAnonymousArrayOfString[1]));
      }
      
    });
    commands.put("ld", commands.get("load"));
    
    commands.put("check", new CommandLine.Command()
    {
      public String getUsage() { return "check [ count | reset | PC | reg | PSR | MPR | mem_addr | label | N | Z | P ] [ mem_addr | label ] [ value | label ]"; }
      
      public String getHelp() {
        String str = "Verifies that a particular value resides in a register or in a memory location, or that a condition code is set.\n";
        str = str + "Samples:\n";
        str = str + "'check PC LABEL' checks if the PC points to wherever LABEL points.\n";
        str = str + "'check LABEL VALUE' checks if the value stored in memory at the location pointed to by LABEL is equal to VALUE.\n";
        str = str + "'check VALUE LABEL' checks if the value stored in memory at VALUE is equal to the location pointed to by LABEL (probably not very useful). To find out where a label points, use 'list' instead.\n";
        return str;
      }
      
      private String check(boolean paramAnonymousBoolean, String[] paramAnonymousArrayOfString, String paramAnonymousString) {
        String str1 = "TRUE";
        String str2 = "FALSE";
        String str3 = "(";
        for (int i = 0; i < paramAnonymousArrayOfString.length; i++) {
          str3 = str3 + paramAnonymousArrayOfString[i];
          str3 = str3 + (i == paramAnonymousArrayOfString.length - 1 ? ")" : " ");
        }
        if (paramAnonymousBoolean) {
          mac.CHECKS_PASSED += 1;
          return str1 + " " + str3;
        }
        mac.CHECKS_FAILED += 1;
        return str2 + " " + str3 + " (actual value: " + paramAnonymousString + ")";
      }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if ((paramAnonymousInt < 2) || (paramAnonymousInt > 4)) {
          return getUsage();
        }
        if (paramAnonymousInt == 2) {
          if (paramAnonymousArrayOfString[1].equals("count")) {
            localObject = mac.CHECKS_PASSED == 1 ? "check" : "checks";
            return mac.CHECKS_PASSED + " " + (String)localObject + " passed, " + mac.CHECKS_FAILED + " failed"; }
          if (paramAnonymousArrayOfString[1].equals("reset")) {
            mac.CHECKS_PASSED = 0;
            mac.CHECKS_FAILED = 0;
            return "check counts reset";
          }
          Object localObject = mac.getRegisterFile();
          if (((paramAnonymousArrayOfString[1].toLowerCase().equals("n")) && (((RegisterFile)localObject).getN())) || ((paramAnonymousArrayOfString[1].toLowerCase().equals("z")) && (((RegisterFile)localObject).getZ())) || ((paramAnonymousArrayOfString[1].toLowerCase().equals("p")) && (((RegisterFile)localObject).getP())))
          {

            return check(true, paramAnonymousArrayOfString, "");
          }
          return check(false, paramAnonymousArrayOfString, ((RegisterFile)localObject).printCC());
        }
        


        int i = Word.parseNum(paramAnonymousArrayOfString[(paramAnonymousInt - 1)]);
        if (i == Integer.MAX_VALUE) {
          i = mac.lookupSym(paramAnonymousArrayOfString[(paramAnonymousInt - 1)]);
          if (i == Integer.MAX_VALUE) {
            return "Bad value or label: " + paramAnonymousArrayOfString[(paramAnonymousInt - 1)];
          }
        }
        Boolean localBoolean = checkRegister(paramAnonymousArrayOfString[1], i);
        if (localBoolean != null) {
          return check(localBoolean.booleanValue(), paramAnonymousArrayOfString, getRegister(paramAnonymousArrayOfString[1]));
        }
        

        int j = mac.getAddress(paramAnonymousArrayOfString[1]);
        
        if (j == Integer.MAX_VALUE) {
          return "Bad register, value or label: " + paramAnonymousArrayOfString[1];
        }
        if ((j < 0) || (j >= 65535)) {
          return "Address " + paramAnonymousArrayOfString[1] + " out of bounds";
        }
        int k;
        if (paramAnonymousInt == 3) {
          k = j;
        } else {
          k = mac.getAddress(paramAnonymousArrayOfString[2]);
          if (k == Integer.MAX_VALUE) {
            return "Bad register, value or label: " + paramAnonymousArrayOfString[2];
          }
          if ((k < 0) || (k >= 65535)) {
            return "Address " + paramAnonymousArrayOfString[2] + " out of bounds";
          }
          if (k < j) {
            return "Second address in range (" + paramAnonymousArrayOfString[2] + ") must be >= first (" + paramAnonymousArrayOfString[1] + ")";
          }
        }
        
        Word localWord = null;
        boolean bool = true;
        String str = "";
        for (int m = j; m <= k; m++) {
          localWord = mac.readMemory(m);
          if (localWord != null)
          {
            if (localWord.getValue() != (i & 0xFFFF)) {
              bool = false;
              str = str + (str.length() == 0 ? "" : ", ");
              str = str + Word.toHex(m) + ":" + localWord.toHex();
            }
          } else {
            return "Bad register, value or label: " + paramAnonymousArrayOfString[1];
          }
        }
        return check(bool, paramAnonymousArrayOfString, str);
      }
      
    });
    commands.put("dump", new CommandLine.Command() {
      public String getUsage() {
        return "d[ump] [-c] mem_addr mem_addr dumpfile";
      }
      
      public String getHelp() { return "dumps a range of memory values to a specified file as raw values or (with the -c flag) as 'check' commands"; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if ((paramAnonymousInt < 4) || (paramAnonymousInt > 5)) {
          return getUsage();
        }
        if ((paramAnonymousInt == 5) && (!paramAnonymousArrayOfString[1].equals("-c"))) {
          System.out.println(paramAnonymousArrayOfString[1]);
          return getUsage();
        }
        IOException localIOException1 = mac.getAddress(paramAnonymousArrayOfString[(paramAnonymousInt - 3)]);
        IOException localIOException2 = mac.getAddress(paramAnonymousArrayOfString[(paramAnonymousInt - 2)]);
        if (localIOException1 == Integer.MAX_VALUE) {
          return "Error: Invalid register, address, or label  ('" + paramAnonymousArrayOfString[(paramAnonymousInt - 3)] + "')";
        }
        if ((localIOException1 < 0) || (localIOException1 >= 65535)) {
          return "Address " + paramAnonymousArrayOfString[(paramAnonymousInt - 3)] + " out of bounds";
        }
        if (localIOException2 == Integer.MAX_VALUE) {
          return "Error: Invalid register, address, or label  ('" + paramAnonymousArrayOfString[(paramAnonymousInt - 3)] + "')";
        }
        if ((localIOException2 < 0) || (localIOException2 >= 65535)) {
          return "Address " + paramAnonymousArrayOfString[(paramAnonymousInt - 2)] + " out of bounds";
        }
        if (localIOException2 < localIOException1) {
          return "Second address in range (" + paramAnonymousArrayOfString[(paramAnonymousInt - 2)] + ") must be >= first (" + paramAnonymousArrayOfString[(paramAnonymousInt - 3)] + ")";
        }
        
        Word localWord = null;
        File localFile = new File(paramAnonymousArrayOfString[(paramAnonymousInt - 1)]);
        PrintWriter localPrintWriter;
        try {
          if (!localFile.createNewFile()) {
            return "File " + paramAnonymousArrayOfString[(paramAnonymousInt - 1)] + " already exists. Choose a different filename.";
          }
          localPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(localFile)));
        } catch (IOException localIOException3) {
          ErrorLog.logError(localIOException3);
          return "Error opening file: " + localFile.getName();
        }
        for (localIOException3 = localIOException1; localIOException3 <= localIOException2; localIOException3++) {
          localWord = mac.readMemory(localIOException3);
          if (localWord != null) {
            if (paramAnonymousInt == 5) {
              localPrintWriter.println("check " + Word.toHex(localIOException3) + " " + localWord.toHex());
            } else {
              localPrintWriter.println(localWord.toHex());
            }
          } else {
            return "Bad register, value or label: " + paramAnonymousArrayOfString[(paramAnonymousInt - 3)];
          }
        }
        localPrintWriter.flush();
        localPrintWriter.close();
        return "Memory dumped.";
      }
    });
    commands.put("d", commands.get("dump"));
    

    commands.put("set", new CommandLine.Command() {
      public String getUsage() {
        return "set [ PC | reg | PSR | MPR | mem_addr | label ] [ mem_addr | label ] [ value | N | Z | P ]";
      }
      
      public String getHelp() { return "Sets the value of a register/PC/PSR/label/memory location/memory range or set the condition codes individually."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if ((paramAnonymousInt < 2) || (paramAnonymousInt > 4)) {
          return getUsage();
        }
        if (paramAnonymousInt == 2) {
          String str1 = setConditionCodes(paramAnonymousArrayOfString[1]);
          if (str1 == null) {
            return getUsage();
          }
          return str1;
        }
        int i = Word.parseNum(paramAnonymousArrayOfString[(paramAnonymousInt - 1)]);
        if (i == Integer.MAX_VALUE) {
          i = mac.lookupSym(paramAnonymousArrayOfString[(paramAnonymousInt - 1)]);
        }
        if (i == Integer.MAX_VALUE) {
          return "Error: Invalid value (" + paramAnonymousArrayOfString[(paramAnonymousInt - 1)] + ")";
        }
        if (paramAnonymousInt == 3) {
          String str2 = setRegister(paramAnonymousArrayOfString[1], i);
          if (str2 != null) {
            return str2;
          }
        }
        

        int j = mac.getAddress(paramAnonymousArrayOfString[1]);
        
        if (j == Integer.MAX_VALUE) {
          return "Error: Invalid register, address, or label  ('" + paramAnonymousArrayOfString[1] + "')";
        }
        if ((j < 0) || (j >= 65535)) {
          return "Address " + paramAnonymousArrayOfString[1] + " out of bounds";
        }
        int k;
        if (paramAnonymousInt == 3) {
          k = j;
        } else {
          k = mac.getAddress(paramAnonymousArrayOfString[2]);
          if (k == Integer.MAX_VALUE) {
            return "Error: Invalid register, address, or label  ('" + paramAnonymousArrayOfString[1] + "')";
          }
          if ((k < 0) || (k >= 65535)) {
            return "Address " + paramAnonymousArrayOfString[2] + " out of bounds";
          }
          if (k < j) {
            return "Second address in range (" + paramAnonymousArrayOfString[2] + ") must be >= first (" + paramAnonymousArrayOfString[1] + ")";
          }
        }
        for (int m = j; m <= k; m++) {
          mac.writeMemory(m, i);
        }
        
        if (paramAnonymousInt == 3) {
          return "Memory location " + Word.toHex(j) + " updated to " + paramAnonymousArrayOfString[(paramAnonymousInt - 1)];
        }
        return "Memory locations " + Word.toHex(k) + " to " + Word.toHex(k) + " updated to " + paramAnonymousArrayOfString[(paramAnonymousInt - 1)];

      }
      

    });
    commands.put("list", new CommandLine.Command() {
      public String getUsage() {
        return "l[ist] [ addr1 | label1 [addr2 | label2] ]";
      }
      
      public String getHelp() { return "Lists the contents of memory locations (default address is PC. Specify range by giving 2 arguments)."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (paramAnonymousInt > 3) {
          return getUsage();
        }
        if (paramAnonymousInt == 1) {
          scrollToPC();
          return Word.toHex(mac.getPC()) + " : " + mac.getCurrentInst().toHex() + " : " + Instruction.disassemble(mac.getCurrentInst(), mac.getPC(), mac);
        }
        
        if (paramAnonymousInt == 2) {
          String str1 = getRegister(paramAnonymousArrayOfString[1]);
          if (str1 != null) {
            return paramAnonymousArrayOfString[1] + " : " + str1;
          }
          
          j = mac.getAddress(paramAnonymousArrayOfString[1]);
          if (j == Integer.MAX_VALUE) {
            return "Error: Invalid address or label (" + paramAnonymousArrayOfString[1] + ")";
          }
          if ((LC3Simulator.GRAPHICAL_MODE) && (j < 65024)) {
            GUI.scrollToIndex(j);
          }
          return Word.toHex(j) + " : " + mac.readMemory(j).toHex() + " : " + Instruction.disassemble(mac.readMemory(j), j, mac);
        }
        

        int i = mac.getAddress(paramAnonymousArrayOfString[1]);
        int j = mac.getAddress(paramAnonymousArrayOfString[2]);
        if (i == Integer.MAX_VALUE)
          return "Error: Invalid address or label (" + paramAnonymousArrayOfString[1] + ")";
        if (j == Integer.MAX_VALUE)
          return "Error: Invalid address or label (" + paramAnonymousArrayOfString[2] + ")";
        if (j < i) {
          return "Error: addr2 should be larger than addr1";
        }
        String str2 = "";
        for (int k = i; k <= j; k++) {
          str2 = str2 + Word.toHex(k) + " : " + mac.readMemory(k).toHex() + " : " + Instruction.disassemble(mac.readMemory(k), k, mac);
          

          if (k != j) str2 = str2 + "\n";
        }
        if (LC3Simulator.GRAPHICAL_MODE) {
          GUI.scrollToIndex(i);
        }
        return str2;
      }
      
    });
    commands.put("l", commands.get("list"));
    
    commands.put("as", new CommandLine.Command() {
      public String getUsage() {
        return "as [-warn] <filename>";
      }
      
      public String getHelp() { return "Assembles <filename> showing errors and (optionally) warnings, and leaves a .obj file in the same directory."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if ((paramAnonymousInt < 2) || (paramAnonymousInt > 3)) {
          return getUsage();
        }
        String[] arrayOfString = new String[paramAnonymousInt - 1];
        String str1 = "";
        arrayOfString[0] = paramAnonymousArrayOfString[1];
        str1 = str1 + paramAnonymousArrayOfString[1];
        if (paramAnonymousInt == 3) {
          arrayOfString[1] = paramAnonymousArrayOfString[2];
          str1 = str1 + " " + paramAnonymousArrayOfString[2];
        }
        LC3as localLC3as = new LC3as();
        String str2 = "";
        try {
          str2 = localLC3as.as(arrayOfString);
          if (str2.length() != 0) {
            return str2 + "Warnings encountered during assembly " + "(but assembly completed w/o errors).";
          }
        }
        catch (AsException localAsException) {
          return localAsException.getMessage() + "Errors encountered during assembly.";
        }
        
        return "Assembly of '" + str1 + "' completed without errors or warnings.";

      }
      

    });
    commands.put("clear", new CommandLine.Command() {
      public String getUsage() {
        return "clear";
      }
      
      public String getHelp() { return "Clears the commandline output window. Only available in GUI mode."; }
      
      public String doCommand(String[] paramAnonymousArrayOfString, int paramAnonymousInt) {
        if (LC3Simulator.GRAPHICAL_MODE) {
          GUI.clearCommandLine();
          return "";
        }
        return "Error: clear is only available in GUI mode";
      }
    });
  }
  



  public void setGUI(LC3GUI paramLC3GUI)
  {
    GUI = paramLC3GUI;
  }
  







  public String runCommand(String paramString)
    throws ExceptionException, NumberFormatException
  {
    if (paramString == null) {
      return "";
    }
    
    if (!paramString.startsWith("@")) {
      resetHistoryStack();
      addToHistory(paramString);
    } else {
      paramString = paramString.replaceFirst("^@", "");
    }
    

    Object localObject1 = paramString.split("\\s+");
    int i = localObject1.length;
    if (i == 0) {
      return "";
    }
    String str = localObject1[0].toLowerCase();
    if (str.equals("")) {
      return "";
    }
    

    int j = -1;
    for (int k = 0; k < localObject1.length; k++) {
      if (localObject1[k].startsWith("#")) {
        j = k;
        break;
      }
    }
    if (j == 0) {
      return "";
    }
    
    if (j >= 0) {
      localObject2 = new String[j];
      for (int m = 0; m < j; m++) {
        localObject2[m] = localObject1[m];
      }
      localObject1 = localObject2;
      i = localObject1.length;
    }
    

    Object localObject2 = (CommandLine.Command)commands.get(str);
    if (localObject2 == null) {
      return "Unknown command: " + str;
    }
    return ((CommandLine.Command)localObject2).doCommand((String[])localObject1, i);
  }
  
  public void scrollToPC()
  {
    if (LC3Simulator.GRAPHICAL_MODE) {
      GUI.scrollToPC();
    }
  }
  




  public String setRegister(String paramString, int paramInt)
  {
    String str = "Register " + paramString.toUpperCase() + " updated to value " + Word.toHex(paramInt);
    
    if (paramString.equalsIgnoreCase("pc")) {
      mac.setPC(paramInt);
      scrollToPC();
    } else if (paramString.equalsIgnoreCase("psr")) {
      mac.setPSR(paramInt);
    } else if (paramString.equalsIgnoreCase("mpr")) {
      mac.getMemory();mac.getMemory().write(65042, paramInt);
    } else if (((paramString.startsWith("r")) || (paramString.startsWith("R"))) && (paramString.length() == 2))
    {
      Integer localInteger = new Integer(paramString.substring(1, 2));
      mac.setRegister(localInteger.intValue(), paramInt);
    } else {
      str = null;
    }
    return str;
  }
  



  public String setConditionCodes(String paramString)
  {
    String str = null;
    if (paramString.equalsIgnoreCase("n")) {
      mac.setN();
      str = "PSR N bit set";
    } else if (paramString.equalsIgnoreCase("z")) {
      mac.setZ();
      str = "PSR Z bit set";
    } else if (paramString.equalsIgnoreCase("p")) {
      mac.setP();
      str = "PSR P bit set";
    }
    return str;
  }
  



  public String getRegister(String paramString)
  {
    int i;
    

    if (paramString.equalsIgnoreCase("pc")) {
      i = mac.getPC();
    } else if (paramString.equalsIgnoreCase("psr")) {
      i = mac.getPSR();
    } else if (paramString.equalsIgnoreCase("mpr")) {
      i = mac.getMPR();
    } else if (((paramString.startsWith("r")) || (paramString.startsWith("R"))) && (paramString.length() == 2))
    {
      Integer localInteger = new Integer(paramString.substring(1, 2));
      i = mac.getRegister(localInteger.intValue());
    } else {
      return null;
    }
    return Word.toHex(i);
  }
  






  public Boolean checkRegister(String paramString, int paramInt)
  {
    int i = Word.parseNum(getRegister(paramString));
    if (i == Integer.MAX_VALUE) {
      return null;
    }
    Word localWord = new Word(paramInt);
    return new Boolean(i == localWord.getValue());
  }
  
  private static abstract interface Command
  {
    public abstract String getUsage();
    
    public abstract String getHelp();
    
    public abstract String doCommand(String[] paramArrayOfString, int paramInt)
      throws ExceptionException;
  }
}
