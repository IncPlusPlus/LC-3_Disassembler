import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;



public class ErrorLog
{
  private static PrintWriter log;
  private static String logDelim = "\n-----\n";
  private static boolean logOpen = false;
  



  public ErrorLog() {}
  


  private static void logTimeStamp()
  {
    if (!logOpen) {
      logInit();
    }
    if (log == null) {
      return;
    }
    log.write(new Date(Calendar.getInstance().getTimeInMillis()).toString() + ": ");
  }
  



  public static void logError(String paramString)
  {
    if (!logOpen) {
      logInit();
    }
    if (log == null) {
      return;
    }
    logTimeStamp();
    log.write(paramString);
    log.write(logDelim);
  }
  



  public static void logError(Exception paramException)
  {
    if (!logOpen) {
      logInit();
    }
    if (log == null) {
      return;
    }
    logTimeStamp();
    paramException.printStackTrace(log);
    log.write(logDelim);
  }
  



  private static void logInit()
  {
    if (!logOpen)
    {


      try
      {


        log = new PrintWriter(new FileWriter("lc3sim_errorlog.txt"), true);
      }
      catch (IOException localIOException) {
        log = null;
      }
    }
  }
  
  public static void logClose()
  {
    if (log == null) {
      return;
    }
    log.close();
  }
}
