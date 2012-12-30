package com.easytournament.basic.model.tablemodel;

import java.util.ArrayList;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.jgoodies.common.collect.ArrayListModel;

public class TeamImportTableModel extends SelectableTableModel {

  private String[] columnNames = {"", ResourceManager.getText(Text.TEAM)};
  private ArrayList<Team> data;
  private ArrayList<Boolean> selection;

  public TeamImportTableModel(ArrayList<Team> data, ArrayList<Boolean> selection) {
    super();
    this.data = data;
    this.selection = selection;
  }

  public TeamImportTableModel(String[] columNames, ArrayListModel<Team> data) {
    super();
    this.data = data;
    this.columnNames = columNames;
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

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  @Override
  public void toggleSelected(int row) {
    selection.set(row, !selection.get(row));
    this.fireTableRowsUpdated(row, row);
  }

}
