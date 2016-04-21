package easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Refree;

public class RefreeTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1043103597499173015L;
  private String[] columnNames;
  private ArrayListModel<Refree> data;

  public RefreeTableModel(ArrayListModel<Refree> data, boolean assistant) {
    super();
    this.data = data;
    if (assistant) {
      columnNames = new String[] {ResourceManager.getText(Text.FIRSTNAME),
          ResourceManager.getText(Text.LASTNAME),
          ResourceManager.getText(Text.FUNCTION)};
    }
    else {
      columnNames = new String[] {ResourceManager.getText(Text.FIRSTNAME),
          ResourceManager.getText(Text.LASTNAME)};
    }
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    if (data == null)
      return 0;
    return data.getSize();
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
    switch (col) {
      case 0:
        return data.get(row).getPrename();
      case 1:
        return data.get(row).getName();
      case 2:
        return data.get(row).getFunction();
    }
    return null;
  }

  public Refree getPersonAt(int row) {
    return data.get(row);
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public void addRow(Refree p) {
    data.add(p);
    this.fireTableDataChanged();
  }

  public Refree removeRow(int row) {
    Refree r = data.remove(row);
    this.fireTableDataChanged();
    return r;
  }

}
