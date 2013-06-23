package com.easytournament.basic.model.wizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import com.easytournament.basic.gui.wizard.TournamentTypeWizardPanel;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;

public class TournamentTypeWizardModel extends WizardModel {

  private TournamentWizardData tournamentData;

  public TournamentTypeWizardModel(TournamentWizardData data) {
    this.tournamentData = data;
  }

  @Override
  public List<Action> getButtonActions() {
    ArrayList<Action> actionList = new ArrayList<Action>();
    actionList.add(new AbstractAction("Weiter") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TournamentTypeWizardModel.this.firePropertyChange(NEXT_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction("Abbrechen") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TournamentTypeWizardModel.this.firePropertyChange(CANCEL_PRESSED, 0, 1);        
      }
    });
    return actionList;
  }

  @Override
  public JPanel getPanel() {
    TournamentTypeWizardPanel panel = new TournamentTypeWizardPanel();
    return panel;
  }

  @Override
  public String getTitel() {
    return "Tournament Type";
  }

  @Override
  public WizardModel getNextModel() {
    return new TWizardNTeamsSelectionModel(this.tournamentData);
  }

  @Override
  public WizardModel getPreviousModel() {
    return null;
  }

  @Override
  public boolean hasNextModel() {
    return true;
  }

  @Override
  public boolean hasPreviousModel() {
    return false;
  }

}
