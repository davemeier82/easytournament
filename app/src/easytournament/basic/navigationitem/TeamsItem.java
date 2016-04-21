package easytournament.basic.navigationitem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.jdom.Element;

import easytournament.basic.action.MainMenuAction;
import easytournament.basic.gui.TeamOverviewPanel;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.model.TeamOverviewPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Staff;
import easytournament.basic.valueholder.Team;
import easytournament.basic.xml.TeamsXMLHandler;


public class TeamsItem extends NavigationItem{

  private TeamOverviewPanel panel;
  
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
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.TEAMS), this);
  }

  public JComponent getPanel() {
    if(panel == null){
      panel = new TeamOverviewPanel(new TeamOverviewPModel());
    }
    return new JScrollPane(panel);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.SAVE.name())) {
      TeamsXMLHandler.save((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      TeamsXMLHandler.open((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.NEW.name())) {
      Team.CURRENT_MAX_ID = 0;
      Player.CURRENT_MAX_ID = 0;
      Staff.CURRENT_MAX_ID = 0;
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    //do nothing
  }
}
