package com.easytournament.basic.model.tablemodel;

import java.util.ArrayList;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Person;
import com.easytournament.basic.valueholder.Refree;


public class RefreeImportTableModel extends SelectableTableModel {

  private static final long serialVersionUID = 1043103597499173015L;
  private String[] columnNames = {"", ResourceManager.getText(Text.FIRSTNAME),
      ResourceManager.getText(Text.LASTNAME)};
  private ArrayList<Refree> data;
  private ArrayList<Boolean> selection;

  public RefreeImportTableModel(ArrayList<Refree> data,
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
      case 2:
        return data.get(row).getPrename();
      case 3:
        return data.get(row).getName();
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
