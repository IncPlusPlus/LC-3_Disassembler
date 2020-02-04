import java.awt.Color;




public class Word
{
  private int value;
  
  public Word(int paramInt) { value = (paramInt & 0xFFFF); }
  public Word() { value = 0; }
  
  public void reset() {
    value = 0;
  }
  
  public String toHex() {
    return toHex(value);
  }
  





  public static String toHex(int paramInt)
  {
    if ((paramInt < 32768) || (paramInt > 65535)) {
      return null;
    }
    
    String str = "x";
    int[] arrayOfInt = { 15, 240, 3840, 61440 };
    char[] arrayOfChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    for (int i = 3; i >= 0; i--) {
      int j = paramInt & arrayOfInt[i];
      j >>= 4 * i;
      str = str + arrayOfChar[j];
    }
    return str;
  }
  
  public String toString() {
    return "" + value;
  }
  
  public void setValue(int paramInt) {
    value = (paramInt & 0xFFFF);
  }
  
  public int getValue() {
    return value;
  }
  


  public static int parseNum(String paramString)
  {
    int i;
    

    try
    {
      if (paramString.indexOf('x') == 0) {
        i = Integer.parseInt(paramString.replace('x', '0'), 16);
      } else {
        i = Integer.parseInt(paramString);
      }
    } catch (NumberFormatException localNumberFormatException) {
      i = Integer.MAX_VALUE;
    } catch (NullPointerException localNullPointerException) {
      i = Integer.MAX_VALUE;
    }
    return i;
  }
  
  public int convertToRGB() {
    return new Color(getZext(14, 10) * 8, getZext(9, 5) * 8, getZext(4, 0) * 8).getRGB();
  }
  
  public int getZext(int paramInt1, int paramInt2)
  {
    int i = value;
    


    if (paramInt2 > paramInt1) {
      return getZext(paramInt2, paramInt1);
    }
    
    i = (-1 << paramInt1 + 1 ^ 0xFFFFFFFF) & i;
    i >>= paramInt2;
    return i;
  }
  
  public int getSext(int paramInt1, int paramInt2) {
    int i = value;
    
    if (paramInt2 > paramInt1) {
      return getSext(paramInt2, paramInt1);
    }
    
    int j = i & 1 << paramInt1;
    if (j != 0) {
      i = -1 << paramInt1 | i;
    } else {
      i = (-1 << paramInt1 + 1 ^ 0xFFFFFFFF) & i;
    }
    
    i >>= paramInt2;
    return i;
  }
  
  public int getBit(int paramInt) {
    return getZext(paramInt, paramInt);
  }
  






  public static int convertByteArray(byte paramByte1, byte paramByte2)
  {
    int i = 0;
    byte b = 255;
    i += (b & paramByte1);
    i *= 256;
    i += (b & paramByte2);
    return i;
  }
}
