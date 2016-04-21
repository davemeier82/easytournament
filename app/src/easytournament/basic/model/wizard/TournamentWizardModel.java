package easytournament.basic.model.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.model.MainFramePModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.tournamentwizard.TournamentSelector;
import easytournament.basic.tournamentwizard.TournamentWizardData;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.basic.xml.XMLHandler;
import easytournament.designer.navigationitem.DesignerItem;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.Group;

public class TournamentWizardModel extends WizardModel implements
    PropertyChangeListener {

  private TournamentWizardData data;
  private WizardModel currentModel;
  private boolean newTournament;

  public TournamentWizardModel(boolean newTournament) {
    this.data = new TournamentWizardData(newTournament);
    this.currentModel = new TournamentTypeWizardModel(this.data);
    this.currentModel.addPropertyChangeListener(this);
    this.newTournament = newTournament;
  }

  @Override
  public List<Action> getButtonActions() {
    if (this.currentModel != null) {
      return this.currentModel.getButtonActions();
    }
    return new ArrayList<Action>();
  }

  @Override
  public JPanel getPanel() {
    if (this.currentModel != null) {
      return this.currentModel.getPanel();
    }
    return new JPanel();
  }

  @Override
  public String getTitel() {
    if (this.currentModel != null) {
      return this.currentModel.getTitel();
    }
    return "";
  }

  @Override
  public WizardModel getNextModel() {
    if (this.currentModel != null && this.currentModel.hasNextModel()) {
      return this.currentModel.getNextModel();
    }
    return null;
  }

  @Override
  public WizardModel getPreviousModel() {
    if (this.currentModel != null && this.currentModel.hasPreviousModel()) {
      return this.currentModel.getPreviousModel();
    }
    return null;
  }

  @Override
  public boolean hasNextModel() {
    if (this.currentModel != null) {
      return this.currentModel.hasNextModel();
    }
    return false;
  }

  @Override
  public boolean hasPreviousModel() {
    if (this.currentModel != null) {
      return this.currentModel.hasPreviousModel();
    }
    return false;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == WizardModel.NEXT_MODEL_PRESSED) {
      if (this.currentModel.hasNextModel()) {
        this.currentModel.removePropertyChangeListener(this);
        this.currentModel = this.currentModel.getNextModel();
        this.currentModel.addPropertyChangeListener(this);
        this.firePropertyChange(WizardModel.BUTTONS_CHANGED, null,
            this.getButtonActions());
        this.firePropertyChange(WizardModel.PANEL_CHANGED, null,
            this.getPanel());
        this.firePropertyChange(WizardModel.TITLE_CHANGED, null,
            this.getTitel());
      }
    }
    else if (evt.getPropertyName() == WizardModel.PREVIOUS_MODEL_PRESSED) {
      if (this.currentModel.hasPreviousModel()) {
        this.currentModel.removePropertyChangeListener(this);
        this.currentModel = this.currentModel.getPreviousModel();
        this.currentModel.addPropertyChangeListener(this);
        this.firePropertyChange(WizardModel.BUTTONS_CHANGED, null,
            this.getButtonActions());
        this.firePropertyChange(WizardModel.PANEL_CHANGED, null,
            this.getPanel());
        this.firePropertyChange(WizardModel.TITLE_CHANGED, null,
            this.getTitel());
      }
    }
    else if (evt.getPropertyName() == WizardModel.CANCEL_PRESSED) {
      this.firePropertyChange(WizardModel.DISPOSE, 0, 1);
    }
    else if (evt.getPropertyName() == WizardModel.OK_PRESSED) {
      if (newTournament) {
        MainFramePModel.getInstance().newTournament();
      }
      Tournament tournement = Organizer.getInstance().getCurrentTournament();

      if (newTournament) {
        tournement.setName(this.data.getName());
      }
      else {
        tournement.setUnassignedteams(tournement.getTeams());
        for (Team tm : tournement.getTeams()) {
          tm.clearGroups();
        }
        tournement.getPlan().getGroups().clear();
        tournement.getSchedules().clear();
      }

      Document doc;
      try {
        doc = XMLHandler.openXMLDoc(TournamentSelector.getTournamentFile(
            this.data.getType(), this.data.getnTeams(), this.data.getnGroups(),
            this.data.getnStages(), this.data.isAddBronceMedalGame()));
        DesignerItem.openPlan(doc.getRootElement(), true);
        ArrayListModel<AbstractGroup> groups = tournement.getPlan().getGroups();
        String groupName = ResourceManager.getText(Text.GROUP);
        for (AbstractGroup g : groups) {
          if (g instanceof Group) {
            Group group = (Group)g;
            group.setName(group.getName().replaceFirst("Group", groupName));
          }
          else {
            DuellGroup group = (DuellGroup)g;
            if (group.getName().startsWith("Final")) {
              group.setName(group.getName().replaceFirst("Final",
                  ResourceManager.getText(Text.FINAL)));
            }
            else if (group.getName().startsWith("Semi final")) {
              group.setName(group.getName().replaceFirst("Semi final",
                  ResourceManager.getText(Text.SEMI_FINAL)));
            }
            else if (group.getName().startsWith("Quarter final")) {
              group.setName(group.getName().replaceFirst("Quarter final",
                  ResourceManager.getText(Text.QUARTER_FINAL)));
            }
            else if (group.getName().startsWith("Eighth final")) {
              group.setName(group.getName().replaceFirst("Eighth final",
                  ResourceManager.getText(Text.BEST16)));
            }
            else if (group.getName().startsWith("Game")) {
              group.setName(group.getName().replaceFirst("Game",
                  ResourceManager.getText(Text.BEST32_GAME)));
            }
            else if (group.getName().startsWith("Bronce Medal")) {
              group.setName(group.getName().replaceFirst("Bronce Medal",
                  ResourceManager.getText(Text.BRONCEMEDAL_GAME)));
            }
          }
        }
      }
      catch (Exception e) {
        ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
        return;
      }
      DesignerItem.clearUndoManager();
      this.firePropertyChange(WizardModel.DISPOSE, 0, 1);
    }
  }
}
