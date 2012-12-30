package com.easytournament.basic.model.tablemodel;

import java.util.ArrayList;


import com.easytournament.basic.gameevent.GameEventType;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.jgoodies.common.collect.ArrayListModel;

public class EventImportTableModel extends SelectableTableModel {

  private String[] columnNames = {"",ResourceManager.getText(Text.TYPE), ResourceManager.getText(Text.NAME)};
  private ArrayList<GameEvent> data;
  private ArrayList<Boolean> selection;

  public EventImportTableModel(ArrayList<GameEvent> data,
      ArrayList<Boolean> selection) {
    super();
    this.data = data;
    this.selection = selection;
  }

  public EventImportTableModel(String[] columNames,
      ArrayListModel<GameEvent> data) {
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
    switch (col) {
      case 0:
        return selection.get(row);
      case 1:
        return GameEventType.getName(data.get(row).getType());
      case 2:
        return data.get(row).getName();
    }
    return null;
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
