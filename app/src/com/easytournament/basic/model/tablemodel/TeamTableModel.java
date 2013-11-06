package com.easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.jgoodies.common.collect.ArrayListModel;

public class TeamTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -4419435795695572019L;
  private String[] columnNames = {ResourceManager.getText(Text.TEAM)};
  private ArrayListModel<Team> data;

  public TeamTableModel(ArrayListModel<Team> data) {
    super();
    this.data = data;
  }
  
  public TeamTableModel(String[] columNames, ArrayListModel<Team> data) {
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
    return data.getSize();
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
    return data.get(row).getName();
  }

  public Team getTeamAt(int row) {
    return data.get(row);
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public void addRow(Team team) {
    data.add(team);
    this.fireTableDataChanged();
  }
  
  public void addRow(int index, Team team) {
    data.add(index, team);
    this.fireTableDataChanged();
  }
  
  public void removeRow(int row) {
    data.remove(row);
    this.fireTableDataChanged();
  }
  
  public void removeElement(Team team){
    data.remove(team);
    this.fireTableDataChanged();
  }

}
