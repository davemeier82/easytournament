package com.easytournament.basic.model.wizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import com.easytournament.basic.gui.wizard.TWizardTSelectionPanel;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;

public class TWizardTSelectionModel extends WizardModel {

  private TournamentWizardData tournamentData;

  public TWizardTSelectionModel(TournamentWizardData data) {
    this.tournamentData = data;
  }

  @Override
  public List<Action> getButtonActions() {
    ArrayList<Action> actionList = new ArrayList<Action>();
    actionList.add(new AbstractAction("Zurück") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(PREVIOUS_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction("Weiter") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(NEXT_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction("Abbrechen") {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(CANCEL_PRESSED, 0, 1);        
      }
    });
    return actionList;
  }

  @Override
  public JPanel getPanel() {
    TWizardTSelectionPanel panel = new TWizardTSelectionPanel();
    return panel;
  }

  @Override
  public String getTitel() {
    return "Tournament Selection";
  }

  @Override
  public WizardModel getNextModel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public WizardModel getPreviousModel() {
    return new TWizardNTeamsSelectionModel(this.tournamentData);
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
