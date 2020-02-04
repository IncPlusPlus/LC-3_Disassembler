



class Scanner
{
  public String line = null;
  

  int pos = -1;
  

  int lineno = -1;
  

  public boolean scanning = true;
  

  boolean warn;
  

  static String warnings = "";
  


  Scanner(String paramString, int paramInt, boolean paramBoolean)
    throws AsException
  {
    line = expand_tabs(paramString, 8);
    pos = 0;
    warn = paramBoolean;
    lineno = paramInt;
    get_remaining_line();
  }
  



  String get_remaining_line()
    throws AsException
  {
    check_valid_line();
    String str1 = line.substring(pos);
    String str2 = skipws(str1);
    pos += str1.length() - str2.length();
    
    return str2;
  }
  






  String get_lexeme_pnum(int paramInt1, int paramInt2)
    throws AsException
  {
    check_valid_line();
    
    String str = get_remaining_line().substring(paramInt2);
    

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (paramInt1 == 2) {
        if ((c != '0') && (c != '1')) {
          break;
        }
      } else if (paramInt1 == 10) {
        if (!Character.isDigit(c)) {
          break;
        }
      } else if (paramInt1 == 16) {
        c = Character.toLowerCase(c);
        if ((!Character.isDigit(c)) && (c != 'a') && (c != 'b') && (c != 'c') && (c != 'd') && (c != 'e') && (c != 'f')) {
          break;
        }
      }
      else
      {
        interror("Illegal base (" + paramInt1 + ")");
      }
    }
    return str.substring(0, i);
  }
  






  String get_lexeme_num(int paramInt1, int paramInt2)
    throws AsException
  {
    check_valid_line();
    
    String str1 = get_remaining_line().substring(paramInt2);
    
    if (str1.length() < 1) { return "";
    }
    if (str1.charAt(0) == '-') {
      String str2 = get_lexeme_pnum(paramInt1, paramInt2 + 1);
      if (str2.length() > 0) {
        return "-" + str2;
      }
      return str2;
    }
    
    return get_lexeme_pnum(paramInt1, paramInt2);
  }
  





  String get_lexeme_imm()
    throws AsException
  {
    check_valid_line();
    

    String str2 = get_remaining_line();
    
    if (str2.length() == 0) return str2;
    String str1;
    switch (str2.charAt(0)) {
    case '#': 
      str1 = get_lexeme_num(10, 1);
      if (str1.length() == 0) return str1;
      return "#" + str1;
    case 'x': 
      str1 = get_lexeme_num(16, 1);
      if (str1.length() == 0) return str1;
      return "x" + str1;
    case 'b': 
      str1 = get_lexeme_num(2, 1);
      if (str1.length() == 0) return str1;
      return "b" + str1;
    }
    
    
    if ((Character.isDigit(str2.charAt(0))) || (str2.charAt(0) == '-'))
    {
      return get_lexeme_num(10, 0);
    }
    
    return "";
  }
  


  String get_lexeme_reg()
    throws AsException
  {
    check_valid_line();
    
    String str1 = get_remaining_line();
    
    if (str1.length() < 2) {
      return "";
    }
    int i = str1.charAt(0);
    if ((i != 114) && (i != 82)) {
      return "";
    }
    
    String str2 = get_lexeme_num(10, 1);
    if (str2.length() == 0) { return str2;
    }
    return str1.substring(0, str2.length() + 1);
  }
  

  String get_string()
    throws AsException
  {
    check_valid_line();
    
    String str1 = get_remaining_line();
    String str2 = "";
    if (str1.length() == 0) { return null;
    }
    for (int i = 0; i < str1.length(); i++) {
      str2 = str2 + str1.charAt(i);
      if ((i == 0) && (str1.charAt(i) != '"'))
        return null;
      if ((i != 0) && (str1.charAt(i) == '"')) {
        break;
      }
    }
    return str2;
  }
  





  String get_real_string(String paramString)
  {
    if ((paramString == null) || (paramString.length() < 2))
      return null;
    if ((paramString.charAt(0) != '"') || (paramString.charAt(paramString.length() - 1) != '"'))
    {
      return null;
    }
    String str1 = paramString.substring(1, paramString.length() - 1);
    String str2 = "";
    for (int i = 0; i < str1.length(); i++) {
      if (str1.charAt(i) == '\\') {
        if (i == str1.length() - 1) return null;
        int j = str1.charAt(++i);
        if (j == 110) {
          str2 = str2 + '\n';
        } else {
          return null;
        }
      } else {
        str2 = str2 + str1.charAt(i);
      }
    }
    return str2;
  }
  





  String get_lexeme_lab(boolean paramBoolean)
    throws AsException
  {
    check_valid_line();
    
    String str = get_remaining_line();
    
    if (str.length() == 0) { return str;
    }
    
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      int j = (Character.isLetter(c)) || (c == '_') || ((paramBoolean) && (c == '.')) || ((Character.isDigit(c)) && (i != 0)) ? 1 : 0;
      


      if (j == 0) break;
    }
    return str.substring(0, i);
  }
  


  boolean eat(String paramString)
    throws AsException
  {
    check_valid_line();
    if (paramString == null) return false;
    String str1 = get_remaining_line();
    String str2 = skipws(paramString);
    if (!str1.startsWith(str2)) { return false;
    }
    pos += str2.length();
    return true;
  }
  
  private void check_valid_line() throws AsException
  {
    if (line == null) {
      interror("Invalid Scanner object.");
    } else if (pos == -1) {
      interror("Invalid position in Scanner object.");
    } else if (lineno == -1) {
      interror("Invalid line number in Scanner object.");
    } else if (pos > line.length()) {
      interror("Invalid position in line (" + pos + "/" + line.length() + ").");
    }
  }
  




  public int imm_val(String paramString)
    throws AsException
  {
    String str = skipws(paramString);
    int i = 0;
    int j = 1;
    
    if ((str == null) || (str.length() == 0)) {
      interror("Bad immediate ('" + str + "')");
    }
    
    char c = str.charAt(0);
    if (c == 'x') {
      i = 16;
    } else if (c == '#') {
      i = 10;
    } else if ((Character.isDigit(c)) || (c == '-')) {
      i = 10;
      j = 0;
    } else if (c == 'b') {
      i = 2;
    } else {
      interror("No prefix on immediate ('" + str + "')");
    }
    
    return Integer.parseInt(str.substring(j), i);
  }
  




  static int reg_num(String paramString)
  {
    String str = skipws(paramString);
    

    if ((str == null) || (str.length() < 2) || (Character.toLowerCase(str.charAt(0)) != 'r'))
    {
      return -1;
    }
    

    return Integer.parseInt(str.substring(1));
  }
  




  static Ops get_opcode_from_lexeme(String paramString)
  {
    String str = skipws(paramString);
    if ((str == null) || (str.length() == 0)) { return null;
    }
    
    return Ops.buildOps(str);
  }
  





  private static String expand_tabs(String paramString, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    
    if (paramString == null) return null;
    for (int j = 0; j < paramString.length(); j++) {
      char c = paramString.charAt(j);
      if (c == '\t') {
        int k = paramInt - i % paramInt;
        for (int m = 0; m < k; m++) localStringBuffer.append(' ');
        i += k;
      }
      else {
        localStringBuffer.append(c);
        if (c == '\n') i = 0; else
          i++;
      }
    }
    return localStringBuffer.toString();
  }
  



  static String skipws(String paramString)
  {
    if (paramString == null) { return "";
    }
    for (int i = 0; i < paramString.length(); i++) {
      int j = paramString.charAt(i);
      if ((j != 32) && (j != 9) && (j != 10)) {
        return paramString.substring(i);
      }
    }
    return "";
  }
  

  private String get_current_string()
    throws AsException
  {
    check_valid_line();
    return line.substring(pos);
  }
  




  String print_line()
  {
    String str = "";
    if (line != null) {
      str = str + str + "\n";
      if (scanning) {
        for (int i = 0; i < pos; i++) {
          str = str + " ";
        }
        str = str + "^\n";
      }
    }
    return str;
  }
  
  void warning(String paramString)
  {
    if (warn) {
      warnings = warnings + "WARNING (line " + lineno + "): " + paramString + "\n";
      if (line != null) {
        warnings = warnings + line + "\n";
      }
    }
  }
  
  void error(String paramString) throws AsException
  {
    String str = "ERROR (line " + lineno + "): " + paramString + "\n";
    str = str + print_line();
    throw new AsException(str);
  }
  
  void interror(String paramString)
    throws AsException
  {
    String str = "INTERNAL ERROR: " + paramString + "\n";
    str = str + "Please report to cse240@seas.upenn.edu.\n";
    str = str + "Include the .asm file that produced this error.\n";
    str = str + print_line();
    throw new AsException(str);
  }
}
