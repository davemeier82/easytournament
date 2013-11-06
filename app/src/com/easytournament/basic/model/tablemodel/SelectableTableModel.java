package com.easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;

abstract public class SelectableTableModel extends AbstractTableModel {
  public abstract void toggleSelected(int row);
}
