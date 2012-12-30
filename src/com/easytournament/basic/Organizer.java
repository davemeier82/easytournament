package com.easytournament.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.HistoryFile;
import com.easytournament.basic.valueholder.Sport;
import com.easytournament.basic.valueholder.Tournament;
import com.jgoodies.binding.beans.Model;

public class Organizer extends Model implements PropertyChangeListener{

  private static final long serialVersionUID = 2736354314754798547L;
  public static final String PROPERTY_TOURNAMENT = "current";
  private static Organizer instance;
  private JFrame mainFrame;
  private Tournament current = new Tournament();
  private HashMap<String,Sport> sports =  new HashMap<String,Sport>();
  private boolean saved = true;
  private ArrayList<HistoryFile> history = new ArrayList<HistoryFile>();
  private boolean substance = false;
  private boolean writeAccess = false;
  

  private Organizer() {
    this.current.addPropertyChangeListener(this);
  }

  public static Organizer getInstance() {
    if (instance == null) {
      instance = new Organizer();
    }
    return instance;
  }

  public Tournament getCurrentTournament() {
    return current;
  }

  public void resetTournament() {
    current.reset();
    current.setName(ResourceManager.getText(Text.NEW_TOURNAMENT));
    current.setSport(sports.get("soccer")); //TODO set to default)
    saved = true;
  }

  public JFrame getMainFrame(){
    return mainFrame;
  }
  
  public void setMainFrame(JFrame frame){
    mainFrame = frame;
  }
  
  public boolean isSaved(){
    return saved;
  }
  
  public void setSaved(boolean saved){
    this.saved = saved;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.setSaved(false);    
  }

  public HashMap<String,Sport> getSports() {
    return sports;
  }

  public void setSports(HashMap<String,Sport> sports) {
    this.sports = sports;
  }

  public ArrayList<HistoryFile> getHistory() {
    return history;
  }

  public void setHistory(ArrayList<HistoryFile> history) {
    this.history = history;
  }

  public boolean isSubstance() {
    return substance;
  }

  public void setSubstance(boolean subst) {
    this.substance = subst;
  }

  public boolean isWriteAccess() {
    return writeAccess;
  }

  public void setWriteAccess(boolean writeAccess) {
    this.writeAccess = writeAccess;
  }
  
}
