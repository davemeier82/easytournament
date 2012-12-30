package com.easytournament.basic.model;

import java.io.File;

import javax.swing.table.TableModel;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.model.tablemodel.HistoryTableModel;
import com.jgoodies.binding.beans.Model;

public class HomeScreenPModel extends Model {

  HistoryTableModel tablemodel;

  public HomeScreenPModel() {
    this.init();
  }

  private void init() {
    tablemodel = new HistoryTableModel(Organizer.getInstance().getHistory());
  }

  public TableModel getTableModel() {
    return tablemodel;
  }

  public void openHistoryFile(int row) {
    MainFramePModel.getInstance().open(
        (File)tablemodel.getValueAt(row, 3));

  }

}
