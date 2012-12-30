package com.easytournament.basic.valueholder;

import javax.swing.ImageIcon;

import com.easytournament.basic.gameevent.GameEventType;
import com.jgoodies.binding.beans.Model;


public class GameEvent extends Model implements Cloneable {

  private static final long serialVersionUID = -5344011039252517462L;
  public static final String PROPERTY_ID = "id";
  public static final String PROPERTY_SPORTID = "sportId";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_TYPE = "type";
  public static final String PROPERTY_POINTS_TEAM = "pointsForTeam";
  public static final String PROPERTY_POINTS_OPPONENT = "pointsForOpponent";
  public static final String PROPERTY_NAME_RID = "nameResourceId";
  public static final String PROPERTY_ICON_RID = "iconResourceId";
  public static final String PROPERTY_SECONDARY_RID = "secondResourceId";
  public static final String PROPERTY_SECONDARY_PLAYER = "secondaryPlayer";
  public static final String PROPERTY_SECONDARY_TEXT = "secondaryPlayerText";
  public static final String PROPERTY_ICON = "icon";
  
  public static int CURRENT_MAX_ID = 0;
   
  protected int id;
  protected String sportId = null;
  protected String name;
  protected String nameResourceId = null;
  protected String iconResourceId = null;
  protected String secondResourceId = null;
  protected ImageIcon icon;
  
  protected int pointsForTeam;
  protected int pointsForOpponent;
  protected GameEventType type = GameEventType.GOAL;  
  protected boolean secondaryPlayer = false;
  protected String secondaryPlayerText;

  public GameEvent(){
    this(CURRENT_MAX_ID++);
  }
  
  public GameEvent(int id){
    this.id = id;
  }

  public void setEvent(GameEvent tmpEvent) {
    sportId = tmpEvent.sportId;
    name = tmpEvent.name;
    nameResourceId = tmpEvent.nameResourceId;
    iconResourceId = tmpEvent.iconResourceId;
    secondResourceId = tmpEvent.secondResourceId;
    icon = tmpEvent.icon;
    pointsForTeam = tmpEvent.pointsForTeam;
    pointsForOpponent = tmpEvent.pointsForOpponent;
    type = tmpEvent.type;
    secondaryPlayer = tmpEvent.secondaryPlayer;
    secondaryPlayerText = tmpEvent.secondaryPlayerText;    
  }

  public int getPointsForTeam() {
    return pointsForTeam;
  }

  public void setPointsForTeam(int pointsForTeam) {
    int old = this.pointsForTeam;
    this.pointsForTeam = pointsForTeam;
    this.firePropertyChange(PROPERTY_POINTS_TEAM, old, this.pointsForTeam);
  }

  public int getPointsForOpponent() {
    return pointsForOpponent;
  }

  public void setPointsForOpponent(int pointsForOpponent) {
    int old = this.pointsForOpponent;
    this.pointsForOpponent = pointsForOpponent;
    this.firePropertyChange(PROPERTY_POINTS_OPPONENT, old, this.pointsForOpponent);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }
  
  public String getIconResourceId() {
    return iconResourceId;
  }

  public void setIconResourceId(String iconResourceId) {
    String old = this.iconResourceId;
    this.iconResourceId = iconResourceId;
    this.firePropertyChange(PROPERTY_ICON_RID, old, this.iconResourceId);
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public void setIcon(ImageIcon icon) {
    ImageIcon old = this.icon;
    this.icon = icon;
    this.firePropertyChange(PROPERTY_ICON, old, this.icon);
  }

  public GameEventType getType() {
    return type;
  }

  public void setType(GameEventType type) {
    GameEventType old = this.type;
    this.type = type;
    this.firePropertyChange(PROPERTY_TYPE, old, this.type);
  }

  public String getNameResourceId() {
    return nameResourceId;
  }

  public void setNameResourceId(String nameResourceId) {
    String old = this.nameResourceId;
    this.nameResourceId = nameResourceId;
    this.firePropertyChange(PROPERTY_NAME_RID, old, this.nameResourceId);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String getSportId() {
    return sportId;
  }

  public void setSportId(String sportId) {
    String old = this.sportId;
    this.sportId = sportId;
    this.firePropertyChange(PROPERTY_SPORTID, old, this.sportId);
  }

  @Override
  public String toString() {
    return this.name;
  }

  public int getId() {
    return id;
  }

  public String getSecondResourceId() {
    return secondResourceId;
  }

  public void setSecondResourceId(String secondResourceId) {
    String old = this.secondResourceId;
    this.secondResourceId = secondResourceId;
    this.firePropertyChange(PROPERTY_SECONDARY_RID, old, this.secondResourceId);
  }

  public boolean isSecondaryPlayer() {
    return secondaryPlayer;
  }

  public void setSecondaryPlayer(boolean secondaryPlayer) {
    boolean old = this.secondaryPlayer;
    this.secondaryPlayer = secondaryPlayer;
    this.firePropertyChange(PROPERTY_SECONDARY_PLAYER, old, this.secondaryPlayer);
  }

  public String getSecondaryPlayerText() {
    return secondaryPlayerText;
  }

  public void setSecondaryPlayerText(String secondaryPlayerText) {
    String old = this.secondaryPlayerText;
    this.secondaryPlayerText = secondaryPlayerText;
    this.firePropertyChange(PROPERTY_SECONDARY_TEXT, old, this.secondaryPlayerText);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    GameEvent other = (GameEvent)obj;
    if (id != other.id)
      return false;
    return true;
  }
  
  public void updateId(){
    this.id = CURRENT_MAX_ID++;
  }


}
