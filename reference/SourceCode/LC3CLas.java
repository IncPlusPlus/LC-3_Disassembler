import java.io.PrintStream;







class LC3CLas
{
  LC3CLas() {}
  
  public static void main(String[] paramArrayOfString)
  {
    LC3as localLC3as = new LC3as();
    String str = "";
    try {
      str = localLC3as.as(paramArrayOfString);
    }
    catch (AsException localAsException) {
      System.err.print(localAsException.getMessage());
      System.exit(1);
    }
    
    System.out.print(str);
  }
}
