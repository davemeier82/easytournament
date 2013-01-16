package com.easytournament.designer.valueholder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


import com.easytournament.basic.valueholder.Rule;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.tournament.valueholder.TableEntry;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public abstract class AbstractGroup extends Model implements
    PropertyChangeListener, ListDataListener {

  private static final long serialVersionUID = -5796055372818322003L;

  public static int CURRENT_MAX_ID = 0;

  public static final String PROPERTY_NAME = "agrp_name";
  public static final String PROPERTY_RULES = "agrp_rules";
  public static final String PROPERTY_DEFAULT_SETTINGS = "defaultSettings";
  public static final String PROPERTY_DEFAULT_RULES = "defaultRules";
  public static final String PROPERTY_POSITIONS = "positions";
  public static final String PROPERTY_SCHEDULES = "schedules";
  public static final String PROPERTY_TEAMS = "teams";
  public static final String PROPERTY_TABLE = "table";
  public static final String PROPERTY_SETTINGS = "settings";

  protected String name;
  protected int id;
  protected boolean defaultSettings = true;
  protected boolean defaultRules = true;
  protected ArrayListModel<Position> positions = new ArrayListModel<Position>();
  protected ArrayListModel<ScheduleEntry> schedules = new ArrayListModel<ScheduleEntry>();
  protected ArrayListModel<Team> teams = new ArrayListModel<Team>();
  protected ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
  protected ArrayListModel<TableEntry> table = new ArrayListModel<TableEntry>();
  protected SportSettings settings = new SportSettings();
  
  protected boolean tableOutOfDate = false;

  public int getNumPositions() {
    return this.positions.getSize();
  }
  
  //no id
  public AbstractGroup() {
    this.setName("");
    if(this.rules != null)
      this.rules.addListDataListener(this);
    this.positions.addListDataListener(this);
    this.teams.addListDataListener(this);
    this.schedules.addListDataListener(this);
    if(this.settings != null)
      this.settings.addPropertyChangeListener(this);
  }

  public AbstractGroup(String name) {
    this.setName(name);
    if(this.rules != null)
      this.rules.addListDataListener(this);
    this.positions.addListDataListener(this);
    this.teams.addListDataListener(this);
    this.schedules.addListDataListener(this);
    if(this.settings != null)
      this.settings.addPropertyChangeListener(this);
    this.id = CURRENT_MAX_ID++;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public Position getPosition(int i) {
    return this.positions.get(i);
  }

  public ArrayList<Position> getPositions() {
    return this.positions;
  }

  protected void addPosition(Position p) {
    p.addPropertyChangeListener(this);
    this.positions.add(p);
    p.setGroup(this);
    this.tableOutOfDate = true;
  }

  public ArrayListModel<Rule> getRules() {
    return this.rules;
  }

  public void setRules(ArrayListModel<Rule> rules) {
    ArrayListModel<Rule> old = this.rules;
    if(this.rules != null)
      this.rules.removeListDataListener(this);
    this.rules = rules;
    if(this.rules != null)
      this.rules.addListDataListener(this);
    this.tableOutOfDate = true;
    this.firePropertyChange(PROPERTY_RULES, old, this.rules);
  }

  public int getNumStartPos() {
    int i = 0;
    for (Position p : this.positions) {
      if (p.getPrev() == null)
        i++;
    }
    return i;
  }

  public ArrayListModel<Position> getStartPos() {
    ArrayListModel<Position> spos = new ArrayListModel<Position>();
    for (Position p : this.positions) {
      if (p.getPrev() == null)
        spos.add(p);
    }
    return spos;
  }

  public ArrayListModel<Team> getTeams() {
    return this.teams;
  }

  public void setTeams(ArrayListModel<Team> teams) {
    this.teams.removeListDataListener(this);
    ArrayListModel<Team> old = this.teams;
    this.teams = teams;
    this.teams.addListDataListener(this);
    this.firePropertyChange(PROPERTY_TEAMS, old, this);
  }

  public ArrayListModel<ScheduleEntry> getSchedules() {
    return this.schedules;
  }

  public void setSchedules(ArrayListModel<ScheduleEntry> schedules) {
    this.schedules.removeListDataListener(this);
    ArrayListModel<ScheduleEntry> old = this.schedules;
    this.schedules = schedules;
    this.schedules.addListDataListener(this);
    this.firePropertyChange(PROPERTY_SCHEDULES, old, this.schedules);
  }

  public ArrayListModel<TableEntry> getTable() {
    return this.table;
  }

  public void setTable(ArrayListModel<TableEntry> table) {
    ArrayListModel<TableEntry> old = this.table;
    this.table = table;
    this.tableOutOfDate = false;
    this.firePropertyChange(PROPERTY_TABLE, old, this.table);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() instanceof SportSettings){
      this.firePropertyChange(PROPERTY_SETTINGS, null, this.settings);
    } else {
      this.tableOutOfDate = true;
      this.firePropertyChange(evt);   
    }
  }

  public int getId() {
    return this.id;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    this.tableOutOfDate = true;
    if(e.getSource() == this.rules) {      
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if(e.getSource() == this.teams) {
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules) {
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.positions){
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    this.tableOutOfDate = true;
    if(e.getSource() == this.rules) {      
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if(e.getSource() == this.teams) {
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules) {
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.positions){
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    this.tableOutOfDate = true;
    if(e.getSource() == this.rules) {      
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if(e.getSource() == this.teams) {
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules) {
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.positions){
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractGroup other = (AbstractGroup)obj;
    if (this.id != other.id)
      return false;
    return true;
  }

  public SportSettings getSettings() {
    return this.settings;
  }

  public void setSettings(SportSettings settings) {
    if(this.settings != null)
      this.settings.removePropertyChangeListener(this);
    SportSettings old = this.settings;
    this.settings = settings;
    if(this.settings != null)
      this.settings.addPropertyChangeListener(this);
    this.tableOutOfDate = true;
    this.firePropertyChange(PROPERTY_SETTINGS, old, this.settings);
  }

  public boolean isDefaultSettings() {
    return this.defaultSettings;
  }

  public void setDefaultSettings(boolean defaultSettings) {
    boolean old = this.defaultSettings;
    this.defaultSettings = defaultSettings;
    this.tableOutOfDate = true;
    this.firePropertyChange(PROPERTY_DEFAULT_SETTINGS, old, this.defaultSettings);
  }

  public boolean isDefaultRules() {
    return this.defaultRules;
  }

  public void setDefaultRules(boolean defaultRules) {
    boolean old = this.defaultRules;
    this.defaultRules = defaultRules;
    this.tableOutOfDate = true;
    this.firePropertyChange(PROPERTY_DEFAULT_RULES, old, this.defaultRules);    
  }

  public boolean isTableOutOfDate() {
    return this.tableOutOfDate;
  }

  public void setTableOutOfDate(boolean tableOutOfDate) {
    this.tableOutOfDate = tableOutOfDate;
  }
  
  public boolean isAllGamesPlayed(){
    if(this.schedules.size() < 1)
      return false;
    for(ScheduleEntry s : this.schedules)
      if(!s.isGamePlayed())
        return false;
    
    return true;
  }
   
}
