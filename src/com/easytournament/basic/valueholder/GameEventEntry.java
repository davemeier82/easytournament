package com.easytournament.basic.valueholder;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class GameEventEntry extends Model implements Cloneable, ListDataListener {

  public static final String PROPERTY_GAMEEVENT = "event";
  public static final String PROPERTY_TIME_MIN = "timeMin";
  public static final String PROPERTY_TIME_SEC = "timeSec";
  public static final String PROPERTY_TEAM = "team";
  public static final String PROPERTY_SUM_HOME_POINTS = "summedHomePoints";
  public static final String PROPERTY_SUM_AWAY_POINTS = "summedAwayPoints";
  public static final String PROPERTY_MAINPLAYERS = "mainPlayers";
  public static final String PROPERTY_SECONDARYPLAYERS = "secondaryPlayers";
  
  protected GameEvent event;
  protected Team team;
  protected ArrayListModel<Player> mainPlayers = new ArrayListModel<Player>();
  protected ArrayListModel<Player> secondaryPlayers = new ArrayListModel<Player>();
  protected int timeMin; 
  protected int timeSec; 
  protected int summedHomePoints;
  protected int summedAwayPoints;
  
  public GameEventEntry(){
    mainPlayers.addListDataListener(this);
    secondaryPlayers.addListDataListener(this);
  }

  public GameEvent getEvent() {
    return event;
  }
  public void setEvent(GameEvent event) {
    GameEvent old = this.event;
    this.event = event;
    this.firePropertyChange(PROPERTY_GAMEEVENT, old, this.event);
  }
  public Team getTeam() {
    return team;
  }
  public void setTeam(Team team) {
    Team old = this.team;
    this.team = team;
    this.firePropertyChange(PROPERTY_TEAM, old, this.team);
  }
  public ArrayListModel<Player> getMainPlayers() {
    return mainPlayers;
  }
  public void setMainPlayers(ArrayListModel<Player> mainPlayers) {
    this.mainPlayers.removeListDataListener(this);
    ArrayListModel<Player> old = this.mainPlayers;
    this.mainPlayers = mainPlayers;
    this.mainPlayers.addListDataListener(this);
    this.firePropertyChange(PROPERTY_MAINPLAYERS, old, this.mainPlayers);
  }
  public ArrayListModel<Player> getSecondaryPlayers() {
    return secondaryPlayers;
  }
  public void setSecondaryPlayers(ArrayListModel<Player> secondaryPlayers) {
    this.secondaryPlayers.removeListDataListener(this);
    ArrayListModel<Player> old = this.secondaryPlayers;
    this.secondaryPlayers = secondaryPlayers;
    this.secondaryPlayers.addListDataListener(this);
    this.firePropertyChange(PROPERTY_SECONDARYPLAYERS, old, this.secondaryPlayers);
  }
  public int getTimeMin() {
    return timeMin;
  }
  public void setTimeMin(int min) {
    int old = this.timeMin;
    this.timeMin = min;
    this.firePropertyChange(PROPERTY_TIME_MIN, old, this.timeMin);
  }
  public int getTimeSec() {
    return timeSec;
  }
  public void setTimeSec(int sec) {
    int old = this.timeSec;
    this.timeSec = sec;
    this.firePropertyChange(PROPERTY_TIME_SEC, old, this.timeSec);
  }
  public int getSummedHomePoints() {
    return summedHomePoints;
  }
  public void setSummedHomePoints(int summedHomePoints) {
    int old = this.summedHomePoints;
    this.summedHomePoints = summedHomePoints;
    this.firePropertyChange(PROPERTY_SUM_HOME_POINTS, old, this.summedHomePoints);
  }
  public int getSummedAwayPoints() {
    return summedAwayPoints;
  }
  public void setSummedAwayPoints(int summedAwayPoints) {
    int old = this.summedAwayPoints;
    this.summedAwayPoints = summedAwayPoints;
    this.firePropertyChange(PROPERTY_SUM_AWAY_POINTS, old, this.summedAwayPoints);
  }
  @Override
  public Object clone() throws CloneNotSupportedException {
    GameEventEntry clone = (GameEventEntry) super.clone();
    clone.setMainPlayers(new ArrayListModel<Player>(this.mainPlayers));
    clone.setSecondaryPlayers(new ArrayListModel<Player>(this.secondaryPlayers));
    clone.setEvent(this.event);
    return clone;
  }
  @Override
  public void contentsChanged(ListDataEvent e) {
    if(e.getSource() == this.mainPlayers){
      this.firePropertyChange(PROPERTY_MAINPLAYERS, null, this.mainPlayers);
    } else if (e.getSource() == this.secondaryPlayers){
      this.firePropertyChange(PROPERTY_SECONDARYPLAYERS, null, this.secondaryPlayers);
    }    
  }
  @Override
  public void intervalAdded(ListDataEvent e) {
    if(e.getSource() == this.mainPlayers){
      this.firePropertyChange(PROPERTY_MAINPLAYERS, null, this.mainPlayers);
    } else if (e.getSource() == this.secondaryPlayers){
      this.firePropertyChange(PROPERTY_SECONDARYPLAYERS, null, this.secondaryPlayers);
    } 
  }
  @Override
  public void intervalRemoved(ListDataEvent e) {
    if(e.getSource() == this.mainPlayers){
      this.firePropertyChange(PROPERTY_MAINPLAYERS, null, this.mainPlayers);
    } else if (e.getSource() == this.secondaryPlayers){
      this.firePropertyChange(PROPERTY_SECONDARYPLAYERS, null, this.secondaryPlayers);
    } 
  }
  
}
