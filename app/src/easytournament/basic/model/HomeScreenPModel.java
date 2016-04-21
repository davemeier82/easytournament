package easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import com.jgoodies.binding.beans.Model;

import easytournament.basic.Organizer;
import easytournament.basic.gui.wizard.WizardDialog;
import easytournament.basic.model.tablemodel.HistoryTableModel;
import easytournament.basic.model.wizard.TournamentWizardModel;
import easytournament.basic.model.wizard.WizardModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

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
    AbstractAction act = new AbstractAction("", ResourceManager.getIcon(Icon.ASSISTANT_ICON_BIG)) {
      
      @Override
      public void actionPerformed(ActionEvent e) { 
        WizardDialog wizard = new WizardDialog(null, new TournamentWizardModel(true), true);
        wizard.setVisible(true);
      }
    };
    act.putValue(Action.SHORT_DESCRIPTION,
        ResourceManager.getText(Text.TOURNAMENT_ASSISTANT));
    return act;
  }

}
