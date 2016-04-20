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
 * Singleton to the current states of the application
 * @author David Meier
 * 
 */
public class Organizer extends Model implements PropertyChangeListener {

  private static final long serialVersionUID = 2736354314754798547L;
  public static final String PROPERTY_TOURNAMENT = "current";

  /**
   * Singleton instance
   */
  private static Organizer instance;
  /**
   * The main frame of the application
   */
  private JFrame mainFrame;
  /**
   * The current tournament that is open
   */
  private Tournament current = new Tournament();
  /**
   * Map of the available sports. The key is the sport id.
   */
  private HashMap<String,Sport> sports = new HashMap<String,Sport>();
  /**
   * True if there are no unsaved changes in the current tournament
   */
  private boolean saved = true;
  /**
   * List of the recently opened tournament files
   */
  private ArrayList<HistoryFile> history = new ArrayList<HistoryFile>();
  /**
   * True if the substance look & feel is loaded
   */
  private boolean substance = false;
  /**
   * True if the application has write access in the application directory
   */
  private boolean writeAccess = false;

  /**
   * Constructor
   */
  private Organizer() {
    // listen to changes of the current tournament to set the "saved" flag
    this.current.addPropertyChangeListener(this);
  }

  /**
   * Returns the singleton instance of the organizer
   * @return the only instance of the organizer
   */
  public static Organizer getInstance() {
    if (instance == null) {
      instance = new Organizer();
    }
    return instance;
  }

  /**
   * Returns the currently opened tournament
   * @return the currently opened tournament
   */
  public Tournament getCurrentTournament() {
    return this.current;
  }

  /**
   * Reset the current tournament to an empty soccer tournament
   */
  public void resetTournament() {
    this.current.reset();
    this.current.setName(ResourceManager.getText(Text.NEW_TOURNAMENT));
    this.current.setSport(this.sports.get("soccer")); // TODO set to default)
    this.saved = true;
  }

  /**
   * Returns the main frame of the application
   * @return main frame
   */
  public JFrame getMainFrame() {
    return this.mainFrame;
  }

  /**
   * Sets the main frame of the application
   * @param frame the main frame
   */
  public void setMainFrame(JFrame frame) {
    this.mainFrame = frame;
  }

  /**
   * Returns true if the current tournament is saved
   * @return true if the current tournament is saved
   */
  public boolean isSaved() {
    return this.saved;
  }

  /**
   * Sets the saved state of the current tournament
   * @param saved true if the current tournament is saved
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
   * Returns a map of the available sports
   * @return map of the available sports
   */
  public HashMap<String,Sport> getSports() {
    return this.sports;
  }

  /**
   * Sets the available sports
   * @param sports map of the available sports
   */
  public void setSports(HashMap<String,Sport> sports) {
    this.sports = sports;
  }

  /**
   * Returns a list of the recently opened tournament files
   * @return list of the recently opened tournament files
   */
  public ArrayList<HistoryFile> getHistory() {
    return this.history;
  }

  /**
   * Sets the list of the recently opened tournament files
   * @param history list of the recently opened tournament files
   */
  public void setHistory(ArrayList<HistoryFile> history) {
    this.history = history;
  }

  /**
   * Returns true if the substance look & feel is loaded
   * @return true if the substance look & feel is loaded
   */
  public boolean isSubstance() {
    return this.substance;
  }

  /**
   * Sets the substance look & feel flag
   * @param substance true if the substance look & feel is loaded
   */
  public void setSubstance(boolean substance) {
    this.substance = substance;
  }

  /**
   * Returns true if there are no unsaved changes in the current tournament
   * @return true if there are no unsaved changes in the current tournament
   */
  public boolean hasWriteAccess() {
    return this.writeAccess;
  }

  /** Sets the write access state
   * @param writeAccess true if there are no unsaved changes in the current tournament
   */
  public void setWriteAccess(boolean writeAccess) {
    this.writeAccess = writeAccess;
  }

}
