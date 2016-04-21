package easytournament.tournament.navigationitem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;

import easytournament.basic.action.MainMenuAction;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.navigationitem.NavTreeActions;
import easytournament.basic.navigationitem.NaviNode;
import easytournament.basic.navigationitem.NavigationItem;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.tournament.gui.GroupAssignPanel;


public class GroupAssignItem extends NavigationItem {

  private GroupAssignPanel panel;

  public void activate() {
    super.activate();
    ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
    enable.add(MainMenuAction.SAVE);
    enable.add(MainMenuAction.SAVEAS);
    enable.add(MainMenuAction.CLOSE);
    enable.add(MainMenuAction.EXPORT);
    enable.add(MainMenuAction.IMPORT);
    MainMenuPModel.getInstance().enableItems(enable);
  }

  public boolean deactivate() {
    super.deactivate();
    if(panel != null)
      panel.save();
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.GROUP_ASSIGNMENT), this);
  }

  public JComponent getPanel() {
    panel = new GroupAssignPanel();
    return panel;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (active && panel != null
        && (evt.getPropertyName().equals(NavTreeActions.OPEN.name()) || evt
            .getPropertyName().equals(NavTreeActions.NEW.name()))) {
      panel.init();
      if(panel.getParent() != null)
        panel.getParent().validate();
      panel.repaint();
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    // do nothing
  }
}
