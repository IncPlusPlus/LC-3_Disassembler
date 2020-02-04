import java.io.PrintStream;

public class LC3Simulator {
  public LC3Simulator() {}
  
  private static String version = "1.0.5";
  
  public static boolean GRAPHICAL_MODE = true;
  
  public static String getVersion() {
    return "LC-3 Simulator Version " + version;
  }
  
  public static void main(String[] paramArrayOfString) {
    String str1 = null;
    if (paramArrayOfString.length > 0) {
      if ("-t".equals(paramArrayOfString[0])) {
        GRAPHICAL_MODE = false;
        if ((paramArrayOfString.length > 2) && ("-s".equals(paramArrayOfString[1]))) {
          str1 = paramArrayOfString[2];
        }
      }
      if ("-h".equals(paramArrayOfString[0])) {
        System.out.println("Usage: java LC3Simulator [-t [-s script]]");
        System.out.println("-t : start in command-line mode");
        System.out.println("-s script : run 'script' as a script file (only available in command-line mode)");
      }
    }
    Machine localMachine = new Machine();
    Object localObject;
    if (GRAPHICAL_MODE) {
      LC3GUI.initLookAndFeel();
      localObject = new LC3GUI(localMachine);
      
      localMachine.setGUI((LC3GUI)localObject);
      javax.swing.SwingUtilities.invokeLater(new TempRun((LC3GUI)localObject));
    } else {
      localObject = new CommandLine(localMachine);
      try
      {
        java.io.BufferedReader localBufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String str3 = null;
        if (str1 != null) {
          ((CommandLine)localObject).scheduleCommand("@script " + str1);
        }
        for (;;) {
          if (!localMachine.isContinueMode()) {
            System.out.print("\n==>");
          }
          



          if (str1 == null) {
            String str2 = localBufferedReader.readLine();
            if (str2 != null) {
              ((CommandLine)localObject).scheduleCommand(str2);
            }
          }
          while ((((CommandLine)localObject).hasMoreCommands()) && ((!localMachine.isContinueMode()) || (((CommandLine)localObject).hasQueuedStop())))
          {
            String str4 = ((CommandLine)localObject).getNextCommand();
            
            if ((str1 != null) && (!str4.startsWith("@"))) {
              str1 = null;
            }
            try {
              str3 = ((CommandLine)localObject).runCommand(str4);
            } catch (ExceptionException localExceptionException) {
              str3 = localExceptionException.getExceptionDescription();
            } catch (NumberFormatException localNumberFormatException) {
              str3 = "NumberFormatException: " + localNumberFormatException.getMessage();
            }
            
            if (str3 == null) {
              System.out.println("Bye!");
              ErrorLog.logClose();
              return;
            }
            System.out.println(str3);
          }
          



          if ((str1 != null) && (!((CommandLine)localObject).hasMoreCommands()))
            str1 = null;
        }
        return;
      } catch (java.io.IOException localIOException) {
        ErrorLog.logError(localIOException);
      }
    }
  }
}
