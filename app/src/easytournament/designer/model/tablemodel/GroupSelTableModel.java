package easytournament.designer.model.tablemodel;

import java.util.ArrayList;

import easytournament.basic.model.tablemodel.SelectableTableModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.valueholder.AbstractGroup;

public class GroupSelTableModel extends SelectableTableModel {

  private String[] columnNames = {"", ResourceManager.getText(Text.GROUP)};
  private ArrayList<AbstractGroup> data;
  private ArrayList<Boolean> selection;

  public GroupSelTableModel(ArrayList<AbstractGroup> data, ArrayList<Boolean> selection) {
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
    if (col == 0)
      return selection.get(row);
    return data.get(row).getName();
  }
  
  public AbstractGroup getGroupAt(int row) {
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
