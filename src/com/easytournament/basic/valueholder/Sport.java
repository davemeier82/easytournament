package com.easytournament.basic.valueholder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class Sport extends Model implements Cloneable, PropertyChangeListener, ListDataListener {
  
  
  public static final String PROPERTY_ID = "id";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_EDITED = "edited";
  public static final String PROPERTY_PATH = "path";
  public static final String PROPERTY_TEXT_RB = "textResourceBundle";
  public static final String PROPERTY_ICON_RB = "iconResourceBundle";
  public static final String PROPERTY_RULES = "rules";
  public static final String PROPERTY_GAMEEVENTS = "gameEvents";
  public static final String PROPERTY_SETTINGS = "settings";
  
  protected String id = "";
  protected String name = "";
  protected boolean edited;
  protected File path;
  protected ResourceBundle textResourceBundle ;
  protected ResourceBundle iconResourceBundle;
  protected ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
  protected ArrayListModel<GameEvent> gameEvents = new ArrayListModel<GameEvent>();
  protected SportSettings settings = new SportSettings();
  
  public  Sport(){
    this.settings.addPropertyChangeListener(this);
    this.rules.addListDataListener(this);
    this.gameEvents.addListDataListener(this);
  }
  
  public void setSport(Sport s){
    if(s != null)
    {
      this.setId(s.id);
      this.setName(s.name);
      this.setEdited(s.edited);
      this.setPath(s.path);    
      this.setTextResourceBundle(s.textResourceBundle);
      this.setIconResourceBundle(s.iconResourceBundle);
      this.setSettings(s.getSettings());
      this.setRules(s.getRules());
      this.setGameEvents(s.getGameEvents());
    }
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    String old = this.id;
    this.id = id;
    this.firePropertyChange(PROPERTY_ID, old, this.id);
  }
  public ArrayListModel<Rule> getRules() {
    return rules;
  }
  public void setRules(ArrayListModel<Rule> rules) {
    this.rules.clear();
    this.rules.addAll(rules);
  }
  public ArrayListModel<GameEvent> getGameEvents() {
    return gameEvents;
  }
  public void setGameEvents(ArrayListModel<GameEvent> gameEvents) {
    this.gameEvents.clear();
    this.gameEvents.addAll(gameEvents);
  }
  public SportSettings getSettings() {
    return settings;
  }
  public void setSettings(SportSettings settings) {
    this.settings.setSportSettings(settings);
    this.firePropertyChange(PROPERTY_SETTINGS, null, this.settings);
  }
  public ResourceBundle getTextResourceBundle() {
    return textResourceBundle;
  }
  public void setTextResourceBundle(ResourceBundle textResourceBundle) {
    this.textResourceBundle = textResourceBundle;
    this.firePropertyChange(PROPERTY_TEXT_RB, null, this.textResourceBundle);
  }
  public ResourceBundle getIconResourceBundle() {
    return iconResourceBundle;
  }
  public void setIconResourceBundle(ResourceBundle iconResourceBundle) {
    this.iconResourceBundle = iconResourceBundle;
    this.firePropertyChange(PROPERTY_ICON_RB, null, this.iconResourceBundle);
  }
  public String getName() {
    if(textResourceBundle != null){
      return textResourceBundle.getString("NAME");
    }
    return name;
  }
  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }
  public File getPath() {
    return path;
  }
  public void setPath(File path) {
    File old = this.path;
    this.path = path;
    this.firePropertyChange(PROPERTY_PATH, old, this.path);
  }
  @Override
  public String toString() {
    return getName();
  }
  public void reset() {
    rules.clear();
    gameEvents.clear();
    //TODO reset settings, name ??? default setting    
    this.firePropertyChange(PROPERTY_RULES, null, this.rules);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    Sport clone = new Sport();
    clone.setSport(this);
    return clone;
  }
  
  public void setEdited(boolean edited){
    boolean old = this.edited;
    this.edited = edited;
    this.firePropertyChange(PROPERTY_EDITED, old, this.edited);
    this.firePropertyChange(PROPERTY_SETTINGS, null, this.settings);
  }

  public boolean isEdited() {
    return this.edited;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() instanceof Rule)
      this.fireIndexedPropertyChange(PROPERTY_RULES, this.rules.indexOf(evt.getSource()), null, this.rules);
    else if(evt.getSource() instanceof GameEvent)
      this.fireIndexedPropertyChange(PROPERTY_GAMEEVENTS, this.gameEvents.indexOf(evt.getSource()), null, this.gameEvents);
    else if(evt.getSource() instanceof SportSettings)
      this.firePropertyChange(PROPERTY_SETTINGS, null, this.settings);
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if(e.getSource() == this.rules){
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if (e.getSource() == this.gameEvents){
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }    
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if(e.getSource() == this.rules){
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if (e.getSource() == this.gameEvents){
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if(e.getSource() == this.rules){
      this.firePropertyChange(PROPERTY_RULES, null, this.rules);
    } else if (e.getSource() == this.gameEvents){
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this.edited? 1231 : 1237);
    result = prime * result + ((this.gameEvents == null)? 0 : this.gameEvents.hashCode());
    result = prime * result
        + ((this.iconResourceBundle == null)? 0 : this.iconResourceBundle.hashCode());
    result = prime * result + ((this.id == null)? 0 : this.id.hashCode());
    result = prime * result + ((this.name == null)? 0 : this.name.hashCode());
    result = prime * result + ((this.path == null)? 0 : this.path.hashCode());
    result = prime * result + ((this.rules == null)? 0 : this.rules.hashCode());
    result = prime * result + ((this.settings == null)? 0 : this.settings.hashCode());
    result = prime * result
        + ((this.textResourceBundle == null)? 0 : this.textResourceBundle.hashCode());
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
    Sport other = (Sport)obj;
    if (this.edited != other.edited)
      return false;
    if (this.gameEvents == null) {
      if (other.gameEvents != null)
        return false;
    }
    else if (!this.gameEvents.equals(other.gameEvents))
      return false;
    if (this.iconResourceBundle == null) {
      if (other.iconResourceBundle != null)
        return false;
    }
    else if (!this.iconResourceBundle.equals(other.iconResourceBundle))
      return false;
    if (this.id == null) {
      if (other.id != null)
        return false;
    }
    else if (!this.id.equals(other.id))
      return false;
    if (this.name == null) {
      if (other.name != null)
        return false;
    }
    else if (!this.name.equals(other.name))
      return false;
    if (this.path == null) {
      if (other.path != null)
        return false;
    }
    else if (!this.path.equals(other.path))
      return false;
    if (this.rules == null) {
      if (other.rules != null)
        return false;
    }
    else if (!this.rules.equals(other.rules))
      return false;
    if (this.settings == null) {
      if (other.settings != null)
        return false;
    }
    else if (!this.settings.equals(other.settings))
      return false;
    if (this.textResourceBundle == null) {
      if (other.textResourceBundle != null)
        return false;
    }
    else if (!this.textResourceBundle.equals(other.textResourceBundle))
      return false;
    return true;
  }  

}
