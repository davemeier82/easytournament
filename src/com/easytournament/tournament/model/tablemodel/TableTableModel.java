package com.easytournament.tournament.model.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.tournament.calc.Values;
import com.easytournament.tournament.valueholder.TableEntry;


public class TableTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;
  private String[] columnNames = {ResourceManager.getText(Text.TEAM),
      ResourceManager.getText(Text.GAME_SHORT),
      ResourceManager.getText(Text.WINS_SHORT),
      ResourceManager.getText(Text.DRAWS_SHORT),
      ResourceManager.getText(Text.DEFEATS_SHORT),
      ResourceManager.getText(Text.POINTS_SHORT), "+", "-", "+/-"};
  private ArrayList<TableEntry> data = new ArrayList<TableEntry>();

  public TableTableModel(ArrayList<TableEntry> data) {
    super();
    this.data = data;
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

    TableEntry tempdata = data.get(row);

    switch (col) {
      case 0:
        return tempdata.getTeam().getName();

      case 1:

        return (Integer)getValueAt(row, 2) + (Integer)getValueAt(row, 3)
            + (Integer)getValueAt(row, 4);

      case 2:

        return tempdata.getValue(Values.HOME_WINS)
            + tempdata.getValue(Values.AWAY_WINS);

      case 3:
        return tempdata.getValue(Values.HOME_DRAWS)
            + tempdata.getValue(Values.AWAY_DRAWS);

      case 4:
        return tempdata.getValue(Values.HOME_DEFEATS)
            + tempdata.getValue(Values.AWAY_DEFEATS);

      case 5:
        return tempdata.getValue(Values.HOME_POINTS)
            + tempdata.getValue(Values.AWAY_POINTS);

      case 6:
        return tempdata.getValue(Values.SCORED_AWAY_GOALS)
            + tempdata.getValue(Values.SCORED_HOME_GOALS);
      case 7:
        return tempdata.getValue(Values.RECEIVED_AWAY_GOALS)
            + tempdata.getValue(Values.RECEIVED_HOME_GOALS);
      case 8:
        return (Integer)getValueAt(row, 6) - (Integer)getValueAt(row, 7);
    }
    return null;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    // not used
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

}
