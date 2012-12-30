package com.easytournament.designer.valueholder;

import java.beans.PropertyChangeListener;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Rule;

public class DuellGroup extends AbstractGroup {

  private static final long serialVersionUID = -5796055372818322003L;

  public int getNumPositions() {
    return positions.getSize();
  }

  public DuellGroup(String name) {
    this(name, true);
  }

  public DuellGroup(String name, boolean setPos) {
    super(name);
    if (setPos) {
      this.addPosition(new Position(ResourceManager.getText(Text.WINNER) + " "
          + this.name));
      this.addPosition(new Position(ResourceManager.getText(Text.LOSER) + " "
          + this.name));
    }
  }

  @Override
  public void setName(String name) {
    String old = this.name;
    this.name = name;
    if (name != null && positions.size() > 1) {
      positions.get(0).setName(
          ResourceManager.getText(Text.WINNER) + " " + this.name);
      positions.get(1).setName(
          ResourceManager.getText(Text.LOSER) + " " + this.name);
    }
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  /**
   * HACK for Undo-Redo
   * @param g
   */
  public void setGroup(DuellGroup g) {
    PropertyChangeListener[] listeners = this.getPropertyChangeListeners()
        .clone();
    for (PropertyChangeListener pcl : this.getPropertyChangeListeners()) {
      this.removePropertyChangeListener(pcl);
    }

    this.name = g.getName();

    this.defaultRules = g.defaultRules;
    this.defaultSettings = g.defaultSettings;
    if (this.defaultRules) {
      this.rules = null;
    }
    else {
      this.rules.clear();
      for (Rule r : g.getRules()) {
        this.rules.add(r);
      }
    }
    if (this.defaultSettings) {
      this.setSettings(g.settings);
    }
    else {
      this.settings.setSportSettings(g.settings);
    }
    this.tableOutOfDate = true;

    for (PropertyChangeListener pcl : listeners) {
      this.addPropertyChangeListener(pcl);
    }
  }

}
