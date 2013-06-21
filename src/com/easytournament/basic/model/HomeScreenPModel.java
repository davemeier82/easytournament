package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.table.TableModel;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.wizard.WizardDialog;
import com.easytournament.basic.model.tablemodel.HistoryTableModel;
import com.easytournament.basic.model.wizard.TournamentWizardModel;
import com.easytournament.basic.model.wizard.WizardModel;
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
  
  public Action getWizardAction() {
    return new AbstractAction("Wizard") {
      
      @Override
      public void actionPerformed(ActionEvent e) { 
        WizardDialog wizard = new WizardDialog(null, new TournamentWizardModel(), true);
        wizard.setVisible(true);
      }
    };
  }

}
