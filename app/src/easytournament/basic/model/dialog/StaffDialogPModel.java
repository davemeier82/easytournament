package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Staff;
import easytournament.basic.valueholder.Team;

public class StaffDialogPModel extends Model {
  
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  
  public static final String DISPOSE = "dispose";
  
  protected Staff staff;
  protected Staff originalStaff;
  protected boolean newStaff = false;
  protected Team team;

  
  public StaffDialogPModel(Staff staff) {
    this.originalStaff = staff;
    try {
      this.staff = (Staff)staff.clone();
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("StaffDialogPModel", "constructor", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
  }
  
  public StaffDialogPModel(Team team) {
    this.staff =  new Staff();
    this.newStaff = true;
    this.team = team;
  }


  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(newStaff){
              team.getStaff().add(staff);
            } else {
              originalStaff.setStaff(staff);
            }

            StaffDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            StaffDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public PersonPanelPModel getPersonPanelPModel() {
    return new PersonPanelPModel(this.staff);
  }

  public ValueModel getStaffValueModel(String propertyName) {
    return new PropertyAdapter<Staff>(staff, propertyName);
  }

}
