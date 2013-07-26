package com.easytournament.basic.model.wizard;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.ListModel;

import com.easytournament.basic.gui.wizard.TWizardTSelectionPanel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.tournamentwizard.TournamentSelector;
import com.easytournament.basic.tournamentwizard.TournamentType;
import com.easytournament.basic.tournamentwizard.TournamentWizardData;
import com.jgoodies.common.collect.ArrayListModel;

public class TWizardTSelectionModel extends WizardModel {

  public static final String PROPERTY_BRONCEMEDALGAMEENABLED = "bronceMedalGameEnabled";

  public static final String PROPERTY_NGROUPS = "nGroups";

  public static final String PROPERTY_GROUPLIST = "grouplist";

  public static final String PROPERTY_KNOCKOUTSTAGESLIST = "knockoutstageslist";

  public static final String PROPERTY_NKNOCKOUTSTAGES = "nKnockoutStages";

  public static final String PROPERTY_NTEAMS = "nTeams";
  
  private TournamentWizardData tournamentData;
  private ArrayListModel<Integer> knockoutstageslist = new ArrayListModel<Integer>();
  private ArrayListModel<Integer> grouplist = new ArrayListModel<Integer>();
  
  public TWizardTSelectionModel(TournamentWizardData data) {
    this.tournamentData = data;
  }

  @Override
  public List<Action> getButtonActions() {
    ArrayList<Action> actionList = new ArrayList<Action>();
    actionList.add(new AbstractAction(ResourceManager.getText(Text.BACK)) {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(PREVIOUS_MODEL_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction(ResourceManager.getText(Text.APPLY)) {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(OK_PRESSED, 0, 1);        
      }
    });
    actionList.add(new AbstractAction(ResourceManager.getText(Text.CANCEL)) {      
      @Override
      public void actionPerformed(ActionEvent e) {
        TWizardTSelectionModel.this.firePropertyChange(CANCEL_PRESSED, 0, 1);        
      }
    });
    return actionList;
  }

  @Override
  public JPanel getPanel() {
    TWizardTSelectionPanel panel = new TWizardTSelectionPanel(this);
    return panel;
  }

  @Override
  public String getTitel() {
    return ResourceManager.getText(Text.TOURNAMENT_SELECTION);
  }

  @Override
  public WizardModel getNextModel() {
    // TODO Auto-generated method stub
    return null;
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

  /**
   * @return the nNockoutStages
   */
  public int getnKnockoutStages() {
    return this.tournamentData.getnStages();
  }

  /**
   * @param nKnockoutStages the nNockoutStages to set
   */
  public void setnKnockoutStages(int nKnockoutStages) {
    this.tournamentData.setnStages(nKnockoutStages);
    updateGroupsList();
  }

  /**
   * @return the nGroups
   */
  public int getnGroups() {
    return this.tournamentData.getnGroups();
  }

  /**
   * @param nGroups the nGroups to set
   */
  public void setnGroups(int nGroups) {
    this.tournamentData.setnGroups(nGroups);
  }
  
  public ListModel<Integer> getKnockoutstageslist() {
    updateKnockoutList();
    return knockoutstageslist;
  }

  private void updateKnockoutList() {
    int nStages = TournamentSelector.getMaxNumberOfNockoutStages(this.getnTeams());
    knockoutstageslist.clear();
    for(int i = 1; i <= nStages; i++) {
      knockoutstageslist.add(i);
    }
    this.tournamentData.setnStages(knockoutstageslist.get(knockoutstageslist.getSize()-1));
  }
  
  private void updateGroupsList() {
    grouplist.clear();
    grouplist.addAll(TournamentSelector.getNumberOfGroups(this.tournamentData.getnTeams(), this.tournamentData.getnStages()));
    this.tournamentData.setnGroups(grouplist.get(grouplist.getSize()-1));
  }
  
  public ListModel<Integer> getGrouplist() {
    updateGroupsList();
    return grouplist;
  }
  
  public void setBronceMedalGameEnabled(boolean value) {
    this.tournamentData.setAddBronceMedalGame(value);
  }

  public boolean isBronceMedalGameEnabled() {
    return this.tournamentData.isAddBronceMedalGame();
  }
  
  public void setnTeams(int nTeams) {
    this.tournamentData.setnTeams(nTeams);
    updateKnockoutList();
    updateGroupsList();
  }
  
  public int getnTeams() {
    return this.tournamentData.getnTeams();
  }
  
  public TournamentType getTournamentType() {
    return this.tournamentData.getType();
  }
}
