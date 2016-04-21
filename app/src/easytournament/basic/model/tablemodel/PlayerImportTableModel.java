package easytournament.basic.model.tablemodel;

import java.util.ArrayList;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Person;
import easytournament.basic.valueholder.Player;


public class PlayerImportTableModel extends SelectableTableModel {

  private static final long serialVersionUID = 1043103597499173015L;
  private String[] columnNames = {"", ResourceManager.getText(Text.NR),
      ResourceManager.getText(Text.FIRSTNAME),
      ResourceManager.getText(Text.LASTNAME),
      ResourceManager.getText(Text.POSITION)};
  private ArrayList<Player> data;
  private ArrayList<Boolean> selection;

  public PlayerImportTableModel(ArrayList<Player> data,
      ArrayList<Boolean> selection) {
    super();
    this.data = data;
    this.selection = selection;
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    if (data == null)
      return 0;
    return data.size();
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
    switch (col) {
      case 0:
        return selection.get(row);
      case 1:
        return data.get(row).getNumber();
      case 2:
        return data.get(row).getPrename();
      case 3:
        return data.get(row).getName();
      case 4:
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

  @Override
  public void toggleSelected(int row) {
    selection.set(row, !selection.get(row));
    this.fireTableRowsUpdated(row, row);
  }

}
