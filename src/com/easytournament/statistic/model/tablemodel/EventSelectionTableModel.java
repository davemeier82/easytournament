package com.easytournament.statistic.model.tablemodel;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;

public class EventSelectionTableModel extends DefaultTableModel {

  ArrayList<Integer> stringColumns = new ArrayList<Integer>();

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return ResourceManager.getText(Text.GAMEEVENT);
      case 1:
        return ResourceManager.getText(Text.SECONDARY_PLAYER_SHORT);
      case 2:
        return ResourceManager.getText(Text.NUMBER_OF_SHORT);
      case 3:
        return ResourceManager.getText(Text.WRONG_TEAM_SHORT);
      case 4:
        return ResourceManager.getText(Text.LABEL);
      default:
        return null;
    }
  }

  @Override
  public int getColumnCount() {
    return 5;
  }

  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return true;
  }

  @Override
  public Class<?> getColumnClass(int column) {
    if (column == 0)
      return GameEvent.class;
    if (column == 4)
      return String.class;
    return Boolean.class;
  }
}
