






























































































class TempRun
  implements Runnable
{
  LC3GUI ms;
  
  public TempRun(LC3GUI paramLC3GUI)
  {
    ms = paramLC3GUI;
  }
  
  public void run() {
    ms.setUpGUI();
  }
}
