import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

class LC3as
{
  LC3as() {}
  
  String as(String[] paramArrayOfString) throws AsException
  {
    String str1 = "Usage: LC3as [-warn] file.asm";
    String str2 = null;
    boolean bool = false;
    SymTab localSymTab = new SymTab();
    
    Scanner.warnings = "";
    

    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (paramArrayOfString[i].length() == 0) {
        error("Null arguments are not permitted.\n" + str1);
      } else if (paramArrayOfString[i].charAt(0) == '-') {
        if (paramArrayOfString[i].equalsIgnoreCase("-warn")) {
          bool = true;
        } else {
          error("Unknown flag ('" + paramArrayOfString[i] + "').\n" + str1);
        }
      } else {
        str2 = paramArrayOfString[i];
      }
    }
    if ((str2 == null) || (str2.length() == 0)) {
      error("No .asm file specified.\n" + str1);
    }
    
    String str3 = base_filename(str2);
    

    List localList = parse(str3, bool);
    

    pass_one(localSymTab, localList);
    

    pass_two(localSymTab, localList);
    

    pass_three(localList, str3);
    

    gen_sym(localSymTab, str3);
    
    return Scanner.warnings;
  }
  


  void error(String paramString)
    throws AsException
  {
    throw new AsException("ERROR: " + paramString + "\n");
  }
  


  String base_filename(String paramString)
    throws AsException
  {
    if (!paramString.endsWith(".asm")) {
      error("Input file must have .asm suffix ('" + paramString + "')");
    }
    return paramString.substring(0, paramString.length() - 4);
  }
  


  void pass_one(SymTab paramSymTab, List paramList)
    throws AsException
  {
    int i = -1;
    
    Iterator localIterator = paramList.iterator();
    
    while (localIterator.hasNext()) {
      Instruction localInstruction = (Instruction)localIterator.next();
      if (label != null) {
        if (label.length() > 20) {
          scan.error("Labels can be no longer than 20 characters ('" + label + "').");
        }
        

        if (i > 65535) {
          scan.error("Label cannot be represented in 16 bits (" + i + ")");
        }
        
        if (!paramSymTab.insert(label, i)) {
          scan.error("Duplicate label ('" + label + "')");
        }
      }
      i = localInstruction.next_address(i);
      if (i == -2) {
        scan.error("Instruction not preceeded by a .orig directive.");
      }
    }
  }
  



  void pass_two(SymTab paramSymTab, List paramList)
    throws AsException
  {
    int i = -1;
    Iterator localIterator = paramList.iterator();
    
    while (localIterator.hasNext()) {
      Instruction localInstruction = (Instruction)localIterator.next();
      if (label_ref != null) {
        int j = paramSymTab.lookup(label_ref);
        if (j < 0) {
          scan.error("Symbol not found ('" + label_ref + "')");
        }
        if (op.op == 33) {
          offset_immediate = j;
        } else {
          offset_immediate = (j - (i + 1));
        }
      }
      i = localInstruction.next_address(i);
    }
  }
  




  void pass_three(List paramList, String paramString)
    throws AsException
  {
    String str = paramString + ".obj";
    
    try
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str));
      Iterator localIterator1 = paramList.iterator();
      
      while (localIterator1.hasNext()) {
        Instruction localInstruction = (Instruction)localIterator1.next();
        if ((op != null) && (op.op != 37)) {
          List localList = localInstruction.encode();
          if (localList != null) {
            Iterator localIterator2 = localList.iterator();
            while (localIterator2.hasNext()) {
              write_word_to_file(localBufferedOutputStream, ((Integer)localIterator2.next()).intValue());
            }
          }
        }
      }
      localBufferedOutputStream.close();
    } catch (IOException localIOException) {
      error("Couldn't write file (" + str + ")");
    }
  }
  



  void gen_sym(SymTab paramSymTab, String paramString)
    throws AsException
  {
    String str1 = paramString + ".sym";
    
    Enumeration localEnumeration = paramSymTab.get_labels();
    try
    {
      BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(str1));
      
      localBufferedWriter.write("// Symbol table\n");
      localBufferedWriter.write("// Scope level 0:\n");
      localBufferedWriter.write("//\tSymbol Name       Page Address\n");
      localBufferedWriter.write("//\t----------------  ------------\n");
      
      while (localEnumeration.hasMoreElements()) {
        String str2 = (String)localEnumeration.nextElement();
        localBufferedWriter.write("//\t" + str2);
        for (int i = 0; i < 16 - str2.length(); i++) {
          localBufferedWriter.write(" ");
        }
        i = paramSymTab.lookup(str2);
        String str3 = formatAddress(i);
        localBufferedWriter.write("  " + str3 + "\n");
      }
      localBufferedWriter.newLine();
      
      localBufferedWriter.close();
    } catch (IOException localIOException) {
      error("Couldn't write file (" + str1 + ")");
    }
  }
  




  private String formatAddress(int paramInt)
  {
    String str = "0000" + Integer.toHexString(paramInt).toUpperCase();
    return str.substring(str.length() - 4);
  }
  





  List parse(String paramString, boolean paramBoolean)
    throws AsException
  {
    String str1 = paramString + ".asm";
    

    ArrayList localArrayList = new ArrayList();
    int i = 1;
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new java.io.FileReader(str1));
      String str2; while ((str2 = localBufferedReader.readLine()) != null) {
        Instruction localInstruction = new Instruction(str2, i++, paramBoolean);
        if (valid) {
          localArrayList.add(localInstruction);
        }
      }
      localBufferedReader.close();
    }
    catch (IOException localIOException) {
      error("Couldn't read file (" + str1 + ")");
    }
    
    return localArrayList;
  }
  



  void write_word_to_file(BufferedOutputStream paramBufferedOutputStream, int paramInt)
    throws IOException
  {
    int i = (byte)(paramInt >> 8 & 0xFF);
    int j = (byte)(paramInt & 0xFF);
    paramBufferedOutputStream.write(i);
    paramBufferedOutputStream.write(j);
  }
}
