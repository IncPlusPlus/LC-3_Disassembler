







public class IllegalMemAccessException
  extends ExceptionException
{
  private static final long serialVersionUID = 108L;
  private int addr;
  
  public IllegalMemAccessException(int paramInt)
  {
    addr = paramInt;
  }
  


  public String getExceptionDescription()
  {
    return "IllegalMemAccessException accessing address " + Word.toHex(addr) + "\n" + "(The MPR and PSR do not permit access to this address)";
  }
}
