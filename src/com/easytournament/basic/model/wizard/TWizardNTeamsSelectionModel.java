package com.easytournament.basic.model.wizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import com.easytournament.basic.gui.wizard.TWizardNTeamsSelectionPanel;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;

public class TWizardNTeamsSelectionModel extends WizardModel {

  private TournamentWizardData tournamentData;

  public TWizardNTeamsSelectionModel(TournamentWizardData data) {
    this.tournamentData = data;
  }

  @Override
  public List<Action> getButtonActions() {
    ArrayList<Action> actionList = new ArrayList<Action>();
    actionList.add(new AbstractAction("Zurück") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardNTeamsSelectionModel.this.firePropertyChange(PREVIOUS_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction("Weiter") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardNTeamsSelectionModel.this.firePropertyChange(NEXT_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction("Abbrechen") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardNTeamsSelectionModel.this.firePropertyChange(CANCEL_PRESSED, 0, 1);        
      }
    });
    return actionList;
  }

  @Override
  public JPanel getPanel() {
    TWizardNTeamsSelectionPanel panel = new TWizardNTeamsSelectionPanel();
    return panel;
  }

  @Override
  public String getTitel() {
    return "Team Selection";
  }

  @Override
  public WizardModel getNextModel() {
    return new TWizardTSelectionModel(this.tournamentData);
  }

  @Override
  public WizardModel getPreviousModel() {
    return new TournamentTypeWizardModel(this.tournamentData);
  }

  @Override
  public boolean hasNextModel() {
    return true;
  }

  @Override
  public boolean hasPreviousModel() {
    return true;
  }

}
