package easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Person;
import easytournament.basic.valueholder.Player;

public class PlayerTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1043103597499173015L;
  private String[] columnNames = {ResourceManager.getText(Text.NR),
      ResourceManager.getText(Text.FIRSTNAME),
      ResourceManager.getText(Text.LASTNAME),
      ResourceManager.getText(Text.POSITION)};
  private ArrayListModel<Player> data;

  public PlayerTableModel(ArrayListModel<Player> data) {
    super();
    this.data = data;
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
        return data.get(row).getNumber();
      case 1:
        return data.get(row).getPrename();
      case 2:
        return data.get(row).getName();
      case 3:
        return data.get(row).getPosition();
    }
    return "";
  }

  public Person getPersonAt(int row) {
    return data.get(row);
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public void addRow(Player p) {
    data.add(p);
    this.fireTableDataChanged();
  }

  public void removeRow(int row) {
    data.remove(row);
    this.fireTableDataChanged();
  }

}
