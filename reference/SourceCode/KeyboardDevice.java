import java.io.BufferedReader;
import java.io.IOException;

public class KeyboardDevice
{
  private BufferedReader kbin;
  private BufferedReader defkbin;
  private static int CBUFSIZE = 128;
  private static char TIMER_TICK = '.';
  

  public static int SCRIPT_MODE = 0;
  



  public static int INTERACTIVE_MODE = 1;
  
  private int current = 0;
  
  private int mode;
  
  private int defmode;
  

  public KeyboardDevice()
  {
    kbin = new BufferedReader(new java.io.InputStreamReader(System.in));
    mode = INTERACTIVE_MODE;
  }
  



  public void setDefaultInputStream()
  {
    defkbin = kbin;
  }
  
  public void setDefaultInputMode() {
    defmode = mode;
  }
  



  public void setInputStream(java.io.InputStream paramInputStream)
  {
    kbin = new BufferedReader(new java.io.InputStreamReader(paramInputStream));
  }
  




  public void setInputMode(int paramInt)
  {
    mode = paramInt;
  }
  



  public void reset()
  {
    kbin = defkbin;
    mode = defmode;
    current = 0;
  }
  


  public boolean available()
  {
    try
    {
      if (kbin.ready()) {
        kbin.mark(1);
        if (kbin.read() == TIMER_TICK) {
          kbin.reset();
          return false;
        }
        kbin.reset();
        return true;
      }
    }
    catch (IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
    return false;
  }
  





  public Word read()
  {
    char[] arrayOfChar = new char[CBUFSIZE];
    try {
      if (available())
      {


        if (mode == INTERACTIVE_MODE) {
          int i = kbin.read(arrayOfChar, 0, CBUFSIZE);
          current = arrayOfChar[(i - 1)];
        } else {
          current = kbin.read();
        }
      }
    }
    catch (IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
    return new Word(current);
  }
  





  public boolean hasTimerTick()
  {
    try
    {
      kbin.mark(1);
      if (kbin.ready()) {
        if (kbin.read() == TIMER_TICK) {
          return true;
        }
        kbin.reset();
        return false;
      }
    }
    catch (IOException localIOException) {
      ErrorLog.logError(localIOException);
    }
    return false;
  }
}
