package com.easytournament.designer.valueholder;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.event.ListDataEvent;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Team;
import com.jgoodies.common.collect.ArrayListModel;

public class Group extends AbstractGroup {

  private static final long serialVersionUID = -887135543754220314L;
  public static final String PROPERTY_NUMPOS = "grp_numPositions";
  protected static final String PROPERTY_POSITIONS = "grp_positions";

  public int getNumPositions() {
    return positions.getSize();
  }
  
  //no id
  public Group(int numPos) {
    super();
    this.setNumPositions(numPos);
    this.positions.addListDataListener(this);
  }

  public Group(String name, int numPos) {
    super(name);
    this.setNumPositions(numPos);
    this.positions.addListDataListener(this);
  }

  public void setGroupName(String s) {
    this.setName(s);
    for (int i = 1; i <= positions.size(); i++) {
      Position t = positions.get(i - 1);
      t.setName(i + ". " + s);
      if (t.getNext() != null) {
        t.getNext();
      }
    }
  }

  public void setNumPositions(int num) {
    Integer old = new Integer(this.getNumPositions());
    if (num < old.intValue()) {
      ArrayListModel<Team> unassTeams = Organizer.getInstance()
          .getCurrentTournament().getUnassignedteams();
      while (num < this.getNumPositions()) {

        Position p = this.positions.get(this.positions.getSize() - 1);
        ArrayList<ScheduleEntry> toRemove = new ArrayList<ScheduleEntry>();
        for (ScheduleEntry se : schedules) {
          if (se.getHomePos() == p || se.getAwayPos() == p) {
            toRemove.add(se);
          }
        }
        schedules.removeAll(toRemove);
        Organizer.getInstance().getCurrentTournament().getSchedules().removeAll(toRemove); //TODO remove reference to tournament

        if (p.getPrev() != null) {
          p.getPrev().removeNext(p);
          p.setPrev(null);
        }
        else if (p.getTeam() != null) {
          Team t = p.getTeam();
          unassTeams.add(t);
          teams.remove(t);
          p.setTeam(null);
        }
        for (Position n : p.getNext()) {
          n.setPrev(null);
        }
        p.getNext().clear();
        this.removePosition(p);
      }
    }
    else if (num > old.intValue()) {
      while (num > this.getNumPositions()) {
        this.addPosition(new Position((this.positions.size() + 1) + ". "
            + this.name));
      }
    }

    this.firePropertyChange(PROPERTY_NUMPOS, old,
        new Integer(this.getNumPositions()));
  }

  @Override
  protected void addPosition(Position p) {
    super.addPosition(p);
  }

  protected boolean removePosition(Position p) {
    for (PropertyChangeListener pcl : p.getPropertyChangeListeners()) {
      p.removePropertyChangeListener(pcl);
    }
    p.clear();
    boolean r = this.positions.remove(p);
    return r;
  }

  /**
   * HACK for Undo-Redo
   * @param g
   */
  public void setGroup(Group g) {
    if(g == null)
      return;
    
    PropertyChangeListener[] listeners = this.getPropertyChangeListeners()
        .clone();
    for (PropertyChangeListener pcl : this.getPropertyChangeListeners()) {
      this.removePropertyChangeListener(pcl);
    }
    this.positions.removeListDataListener(this);

    this.setName(g.getName());
    this.setDefaultRules(g.defaultRules);
    this.setDefaultSettings(g.defaultSettings);
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
      // add reference to default settings
      this.setSettings(g.settings);
    }
    else if(this.settings == null){
      // copy settings
      this.setSettings(new SportSettings(g.settings));
    }
    else {
      // copy settings
      this.settings.setSportSettings(g.settings);
    }
    int nPos = this.getNumPositions();
    int num = g.getNumPositions();
    if (num < nPos) {
      while (num < this.getNumPositions()) {
        Position p = this.positions.get(this.positions.getSize() - 1);
        this.removePosition(p);
      }
    }
    else if (num > nPos) {
      int i = this.positions.size() + 1;
      while (num > this.getNumPositions()) {
        this.addPosition(new Position((i++) + ". "
            + this.name));
      }
    }
    for (PropertyChangeListener pcl : listeners) {
      this.addPropertyChangeListener(pcl);
    }
    this.positions.addListDataListener(this);
    this.tableOutOfDate = true;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if (e.getSource() == this.positions) {
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
      this.tableOutOfDate = true;
    }
    else
      super.contentsChanged(e);

  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if (e.getSource() == this.positions) {
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
      this.tableOutOfDate = true;
    }
    else
      super.intervalAdded(e);

  }

  @Override
  public void setName(String name) {
    String old = this.name;
    this.name = name;
    if (name != null)
      for (int i = 0; i < positions.size(); i++) {
        positions.get(i)
            .setName((i + 1) + ". " + this.name);
      }
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if (e.getSource() == this.positions) {
      this.firePropertyChange(PROPERTY_POSITIONS, null, this.positions);
      this.tableOutOfDate = true;
    }
    else
      super.intervalRemoved(e);
  }

}
