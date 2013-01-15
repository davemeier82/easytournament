/* Organizer.java - 
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

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

/**
 * Singleton to 
 * @author David Meier
 * 
 */
public class Organizer extends Model implements PropertyChangeListener {

  private static final long serialVersionUID = 2736354314754798547L;
  /**
   * 
   */
  public static final String PROPERTY_TOURNAMENT = "current";
  /**
   * 
   */
  private static Organizer instance;
  /**
   * 
   */
  private JFrame mainFrame;
  /**
   * 
   */
  private Tournament current = new Tournament();
  /**
   * 
   */
  private HashMap<String,Sport> sports = new HashMap<String,Sport>();
  /**
   * 
   */
  private boolean saved = true;
  /**
   * 
   */
  private ArrayList<HistoryFile> history = new ArrayList<HistoryFile>();
  /**
   * 
   */
  private boolean substance = false;
  /**
   * 
   */
  private boolean writeAccess = false;

  /**
   * 
   */
  private Organizer() {
    this.current.addPropertyChangeListener(this);
  }

  /**
   * @return
   */
  public static Organizer getInstance() {
    if (instance == null) {
      instance = new Organizer();
    }
    return instance;
  }

  /**
   * @return
   */
  public Tournament getCurrentTournament() {
    return this.current;
  }

  /**
   * 
   */
  public void resetTournament() {
    this.current.reset();
    this.current.setName(ResourceManager.getText(Text.NEW_TOURNAMENT));
    this.current.setSport(this.sports.get("soccer")); // TODO set to default)
    this.saved = true;
  }

  /**
   * @return
   */
  public JFrame getMainFrame() {
    return this.mainFrame;
  }

  /**
   * @param frame
   */
  public void setMainFrame(JFrame frame) {
    this.mainFrame = frame;
  }

  /**
   * @return
   */
  public boolean isSaved() {
    return this.saved;
  }

  /**
   * @param saved
   */
  public void setSaved(boolean saved) {
    this.saved = saved;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.setSaved(false);
  }

  /**
   * @return
   */
  public HashMap<String,Sport> getSports() {
    return this.sports;
  }

  /**
   * @param sports
   */
  public void setSports(HashMap<String,Sport> sports) {
    this.sports = sports;
  }

  /**
   * @return
   */
  public ArrayList<HistoryFile> getHistory() {
    return this.history;
  }

  /**
   * @param history
   */
  public void setHistory(ArrayList<HistoryFile> history) {
    this.history = history;
  }

  /**
   * @return
   */
  public boolean isSubstance() {
    return this.substance;
  }

  /**
   * @param subst
   */
  public void setSubstance(boolean subst) {
    this.substance = subst;
  }

  /**
   * @return
   */
  public boolean isWriteAccess() {
    return this.writeAccess;
  }

  /**
   * @param writeAccess
   */
  public void setWriteAccess(boolean writeAccess) {
    this.writeAccess = writeAccess;
  }

}
