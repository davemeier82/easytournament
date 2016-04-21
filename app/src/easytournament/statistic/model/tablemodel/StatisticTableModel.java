package easytournament.statistic.model.tablemodel;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class StatisticTableModel extends DefaultTableModel {

  ArrayList<Integer> stringColumns = new ArrayList<Integer>();

  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return false;
  }

  @Override
  public Object getValueAt(int row, int column) {
    if (stringColumns.size() > 0 && stringColumns.contains(column))
      return super.getValueAt(row, column);
    return Integer.valueOf((String)super.getValueAt(row, column));
  }

  @Override
  public Class<?> getColumnClass(int column) {
    if (stringColumns.size() > 0 && stringColumns.contains(column))
      return String.class;
    return Integer.class;
  }

  public ArrayList<Integer> getStringColumns() {
    return stringColumns;
  }

}
