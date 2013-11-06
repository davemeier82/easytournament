package com.easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Sport;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class SportDialogPModel extends Model {

  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_SETTINGS_IMPORT = "settingsImport";
  public static final String PROPERTY_RULES_IMPORT = "ruleImport";
  public static final String PROPERTY_EVENTS_IMPORT = "eventsImport";
  public static final String DISPOSE = "dispose";
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;

  protected Sport settingsImport;
  protected Sport ruleImport;
  protected Sport eventsImport;

  protected Sport sport;
  protected Sport current = null;
  
  protected boolean newSport;

  protected ArrayListModel<Sport> sports;

  public SportDialogPModel() {
    this.sport = new Sport();
    newSport = true;
    this.init();
  }

  public SportDialogPModel(Sport s) {
    this.sport = s;
    newSport = false;
    this.init();
  }

  protected void init() {
    this.sports = new ArrayListModel<Sport>(Organizer.getInstance().getSports()
        .values());
    Sport s = new Sport();
    s.setName("-");
    if(newSport) {
      settingsImport = s;
      ruleImport = s;
      eventsImport = s;
    } else {
      try {
        current = (Sport) sport.clone();
        current.setName(ResourceManager.getText(Text.KEEP_CURRENT));
        current.setTextResourceBundle(null);
        sports.add(current);
        settingsImport = current;
        ruleImport = current;
        eventsImport = current;
      }
      catch (CloneNotSupportedException e) {
        ErrorLogger.getLogger().throwing("SportDialogPModel", "init", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
      
    }
    this.sports.add(0, s);
  }

  public List<Sport> getList() {
    return sports;
  }

  public Sport getSettingsImport() {
    return settingsImport;
  }

  public void setSettingsImport(Sport settingsImport) {
    this.settingsImport = settingsImport;
  }

  public Sport getRuleImport() {
    return ruleImport;
  }

  public void setRuleImport(Sport ruleImport) {
    this.ruleImport = ruleImport;
  }

  public Sport getEventsImport() {
    return eventsImport;
  }

  public void setEventsImport(Sport eventsImport) {
    this.eventsImport = eventsImport;
  }

  public boolean isNameEditable() {
    return sport.getTextResourceBundle() == null;
  }

  public String getName() {
    return sport.getName();
  }

  public void setName(String name) {
    sport.setName(name);
  }

  public Action getAction(int action) {
    switch (action) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            
            if(!settingsImport.getName().equals("-") && settingsImport!=current)
              sport.setSettings(settingsImport.getSettings());
            
            if(ruleImport.getName().equals("-"))
              sport.getRules().clear();
            else if (ruleImport!=current)
              sport.setRules(ruleImport.getRules());
            
            if(eventsImport.getName().equals("-"))
              sport.getGameEvents().clear();
            else if(eventsImport!=current)
              sport.setGameEvents(eventsImport.getGameEvents());
            
            if(newSport){
              Organizer.getInstance().getCurrentTournament().setSport(sport);
            }
            
            SportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {

            SportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

}
