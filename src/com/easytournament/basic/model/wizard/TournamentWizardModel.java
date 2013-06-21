package com.easytournament.basic.model.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JPanel;

import com.easytournament.basic.valueholder.TournamentWizardData;

public class TournamentWizardModel extends WizardModel implements
    PropertyChangeListener {

  private TournamentWizardData data;
  private WizardModel currentModel;

  public TournamentWizardModel() {
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
        this.firePropertyChange(WizardModel.BUTTONS_CHANGED, null, this.getButtonActions());
        this.firePropertyChange(WizardModel.PANEL_CHANGED, null, this.getPanel());
        this.firePropertyChange(WizardModel.TITLE_CHANGED, null, this.getTitel());
      }
    }
    else if (evt.getPropertyName() == WizardModel.PREVIOUS_MODEL_PRESSED) {
      if (this.currentModel.hasPreviousModel()) {
        this.currentModel.removePropertyChangeListener(this);
        this.currentModel = this.currentModel.getPreviousModel();
        this.currentModel.addPropertyChangeListener(this);
        this.firePropertyChange(WizardModel.BUTTONS_CHANGED, null, this.getButtonActions());
        this.firePropertyChange(WizardModel.PANEL_CHANGED, null, this.getPanel());
        this.firePropertyChange(WizardModel.TITLE_CHANGED, null, this.getTitel());
      }
    }
    else if (evt.getPropertyName() == WizardModel.CANCEL_PRESSED) {
      this.firePropertyChange(WizardModel.DISPOSE, 0, 1);
    }
  }
}
