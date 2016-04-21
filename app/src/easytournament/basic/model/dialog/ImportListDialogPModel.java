package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.table.TableModel;

import com.jgoodies.binding.beans.Model;

import easytournament.basic.model.tablemodel.SelectableTableModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

public class ImportListDialogPModel extends Model {

  private static final long serialVersionUID = -2018160535498881654L;
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;

  public static final String DISPOSE = "dispose";

  protected SelectableTableModel tableModel;
  protected boolean answer = false;

  public ImportListDialogPModel(SelectableTableModel tm) {
    this.tableModel = tm;
  }

  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            answer = true;
            ImportListDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            answer = false;
            ImportListDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public TableModel getTableModel() {
    return tableModel;
  }

  public void toggleSelected(int row) {
    tableModel.toggleSelected(row);
  }

  public boolean getAnswer() {
    return answer;
  }
}
