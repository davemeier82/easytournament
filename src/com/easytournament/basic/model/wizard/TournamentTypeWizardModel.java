package com.easytournament.basic.model.wizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import com.easytournament.basic.gui.wizard.TournamentTypeWizardPanel;
import com.easytournament.basic.tournamentwizard.TournamentType;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;

public class TournamentTypeWizardModel extends WizardModel {

  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_TOURNAMENTTYPE = "tournamentType";

  private TournamentWizardData tournamentData;

  /**
   * @return the tournamentType
   */
  public TournamentType getTournamentType() {
    return tournamentData.getType();
  }

  @Override
  public List<Action> getButtonActions() {
    ArrayList<Action> actionList = new ArrayList<Action>();
    actionList.add(new AbstractAction("Weiter") {
      @Override
      public void actionPerformed(ActionEvent e) {
        TournamentTypeWizardModel.this.firePropertyChange(NEXT_MODEL_PRESSED,
            0, 1);
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
    TournamentTypeWizardPanel panel = new TournamentTypeWizardPanel(this);
    return panel;
  }

  @Override
  public String getTitel() {
    return "Tournament Type";
  }

  @Override
  public WizardModel getNextModel() {
    return new TWizardTSelectionModel(this.tournamentData);
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

  /**
   * @param tournamentType
   *          the tournamentType to set
   */
  public void setTournamentType(TournamentType tournamentType) {
    this.tournamentData.setType(tournamentType);
  }

  public TournamentTypeWizardModel(TournamentWizardData data) {
    this.tournamentData = data;
  }

  public void setName(String name) {
    this.tournamentData.setName(name);
  }

  public String getName() {
    return this.tournamentData.getName();
  }

}
