package com.easytournament.tournament.navigationitem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;

import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.model.MainMenuPModel;
import com.easytournament.basic.navigationitem.NavTreeActions;
import com.easytournament.basic.navigationitem.NaviNode;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.tournament.gui.GroupAssignPanel;


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
    if (active
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
