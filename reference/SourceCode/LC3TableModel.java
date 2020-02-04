import javax.swing.table.AbstractTableModel;

public abstract class LC3TableModel extends AbstractTableModel
{
  public LC3TableModel() {}
  
  public void fireTableCellUpdated(int paramInt1, int paramInt2) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableCellUpdated(paramInt1, paramInt2);
    }
  }
  
  public void fireTableChanged(javax.swing.event.TableModelEvent paramTableModelEvent) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableChanged(paramTableModelEvent);
    }
  }
  
  public void fireTableDataChanged() {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableDataChanged();
    }
  }
  
  public void fireTableRowsUpdated(int paramInt1, int paramInt2) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableRowsUpdated(paramInt1, paramInt2);
    }
  }
  
  public void fireTableRowsInserted(int paramInt1, int paramInt2) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableRowsInserted(paramInt1, paramInt2);
    }
  }
  
  public void fireTableRowsDeleted(int paramInt1, int paramInt2) {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableRowsDeleted(paramInt1, paramInt2);
    }
  }
  
  public void fireTableStructureChanged() {
    if (LC3Simulator.GRAPHICAL_MODE) {
      super.fireTableStructureChanged();
    }
  }
}
