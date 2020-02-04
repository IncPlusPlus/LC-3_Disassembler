





public class TimerDevice
{
  private static int MANUAL_TIMER = 0;
  





  private static int AUTOMATIC_TIMER = 1;
  



  private static long TIMER_INTERVAL = 500L;
  
  private int mode;
  private boolean enabled = false;
  private long lastTime;
  private long interval;
  private KeyboardDevice kb = null;
  



  public TimerDevice()
  {
    mode = AUTOMATIC_TIMER;
    enabled = true;
  }
  
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean paramBoolean) { enabled = paramBoolean; }
  
  public long getInterval() { return interval; }
  


  public void setTimer()
  {
    mode = AUTOMATIC_TIMER;
    interval = TIMER_INTERVAL;
    lastTime = System.currentTimeMillis();
  }
  



  public void setTimer(long paramLong)
  {
    mode = AUTOMATIC_TIMER;
    interval = paramLong;
    lastTime = System.currentTimeMillis();
  }
  



  public void setTimer(KeyboardDevice paramKeyboardDevice)
  {
    mode = MANUAL_TIMER;
    interval = 1L;
    kb = paramKeyboardDevice;
  }
  



  public void reset()
  {
    mode = AUTOMATIC_TIMER;
    setTimer(TIMER_INTERVAL);
  }
  
  public boolean hasGoneOff() {
    if (!enabled) {
      return false;
    }
    
    if (mode == AUTOMATIC_TIMER) {
      long l = System.currentTimeMillis();
      if (l - lastTime > interval) {
        lastTime = l;
        return true;
      }
      return false;
    }
    
    return kb.hasTimerTick();
  }
}
