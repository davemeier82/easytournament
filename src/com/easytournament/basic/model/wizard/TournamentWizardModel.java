package com.easytournament.basic.model.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.model.MainFramePModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.tournamentwizard.TournamentSelector;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.basic.xml.XMLHandler;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.DuellGroup;
import com.easytournament.designer.valueholder.Group;
import com.jgoodies.common.collect.ArrayListModel;

public class TournamentWizardModel extends WizardModel implements
    PropertyChangeListener {

  private TournamentWizardData data;
  private WizardModel currentModel;

  public TournamentWizardModel() {
    this.data = new TournamentWizardData();
    this.currentModel = new TournamentTypeWizardModel(this.data);
    this.currentModel.addPropertyChangeListener(this);
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
      MainFramePModel.getInstance().newTournament();
      Tournament tournement = Organizer.getInstance().getCurrentTournament();
      tournement.setName(this.data.getName());

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
            Group group = (Group) g;
            group.setName(groupName);
          }
          else {
            DuellGroup group = (DuellGroup) g;
            if(group.getName().startsWith("Final")){
              group.setName(group.getName().replaceFirst("Final", ResourceManager.getText(Text.FINAL)));
            }
            else if(group.getName().startsWith("Semi final")){
              group.setName(group.getName().replaceFirst("Semi final", ResourceManager.getText(Text.SEMI_FINAL)));
            }
            else if(group.getName().startsWith("Quarter final")){
              group.setName(group.getName().replaceFirst("Quarter final", ResourceManager.getText(Text.QUARTER_FINAL)));
            }
            else if(group.getName().startsWith("Eighth final")){
              group.setName(group.getName().replaceFirst("Eighth final", ResourceManager.getText(Text.BEST16)));
            }
            else if(group.getName().startsWith("Game")){
              group.setName(group.getName().replaceFirst("Game", ResourceManager.getText(Text.BEST32_GAME)));
            }
          }
        }
      }
      catch (Exception e) {
        ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
        ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
        return;
      }
      this.firePropertyChange(WizardModel.DISPOSE, 0, 1);
    }
  }
}
