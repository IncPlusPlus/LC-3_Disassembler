






public class IllegalInstructionException
  extends ExceptionException
{
  private static final long serialVersionUID = 101L;
  
  public IllegalInstructionException(String paramString)
  {
    super(paramString);
  }
  


  public String getExceptionDescription()
  {
    return "IllegalInstructionException: " + getMessage();
  }
}
