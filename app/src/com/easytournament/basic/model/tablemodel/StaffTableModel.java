package com.easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Staff;
import com.jgoodies.common.collect.ArrayListModel;

public class StaffTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1043103597499173015L;
  private String[] columnNames = {ResourceManager.getText(Text.FIRSTNAME),
      ResourceManager.getText(Text.LASTNAME),
      ResourceManager.getText(Text.FUNCTION)};
  private ArrayListModel<Staff> data;

  public StaffTableModel(ArrayListModel<Staff> data) {
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
        return data.get(row).getPrename();
      case 1:
        return data.get(row).getName();
      case 2:
        return data.get(row).getFunction();
    }
    return "";
  }

  public Staff getPersonAt(int row) {
    return data.get(row);
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public void addRow(Staff p) {
    data.add(p);
    this.fireTableDataChanged();
  }

  public void removeRow(int row) {
    data.remove(row);
    this.fireTableDataChanged();
  }

}
