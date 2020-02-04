import java.util.Hashtable;

class SymTab {
  Hashtable table;
  
  SymTab() {
    table = new Hashtable();
  }
  
  boolean insert(String paramString, int paramInt) {
    if (lookup(paramString) != -1) {
      return false;
    }
    table.put(paramString, new Integer(paramInt));
    return true;
  }
  
  int lookup(String paramString) {
    Integer localInteger = (Integer)table.get(paramString);
    if (localInteger == null) {
      return -1;
    }
    return localInteger.intValue();
  }
  
  java.util.Enumeration get_labels() {
    return table.keys();
  }
}
