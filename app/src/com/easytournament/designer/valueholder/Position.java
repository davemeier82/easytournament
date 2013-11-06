package com.easytournament.designer.valueholder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.easytournament.basic.valueholder.Team;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class Position extends Model implements PropertyChangeListener,
    ListDataListener, Comparable<Position> {

  private static final long serialVersionUID = 1L;
  public static final String PROPERTY_GROUP = "pos_group";
  public static final String PROPERTY_PERVIOUS = "pos_prev";
  public static final String PROPERTY_NEXT = "pos_next";
  public static final String PROPERTY_TEAM = "pos_team";
  public static final String PROPERTY_NAME = "pos_name";

  public static int CURRENT_MAX_ID = 0;

  private AbstractGroup group;
  private Position prev;
  private String name = "";
  private Team team;
  private ArrayListModel<Position> next = new ArrayListModel<Position>();
  private int id;

  public Position(String name) {
    this.name = name;
    this.id = CURRENT_MAX_ID++;
    this.next.addListDataListener(this);
  }

  public ArrayList<Position> getNext() {
    return next;
  }

  public boolean addNext(Position next) {
    if (next == null)
      return false;
    return this.next.add(next);
  }

  public boolean removeNext(Position next) {
    return this.next.remove(next);
  }

  public Position getPrev() {
    return prev;
  }

  public void setPrev(Position prev) {
    Position old = this.prev;
    if (old != null)
      old.removePropertyChangeListener(this);

    this.prev = prev;
    if (prev != null)
      this.prev.addPropertyChangeListener(this);

    this.firePropertyChange(PROPERTY_PERVIOUS, old, this.prev);
  }

  public AbstractGroup getGroup() {
    return group;
  }

  public void setGroup(AbstractGroup group) {
    AbstractGroup old = this.getGroup();
    this.group = group;
    this.firePropertyChange(PROPERTY_GROUP, old, this.group);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    Team old = this.team;
    if (old != null) {
      this.group.getTeams().remove(old);
      old.setPositionAssignedTo(null);
    }
    this.team = team;
    if (team != null)
      team.setPositionAssignedTo(this);

    this.firePropertyChange(PROPERTY_TEAM, old, this.team);
  }

  public void setTeamAssignment(Team team) {
    Team old = this.team;
    this.team = team;
    if (team != null)
      team.setPositionAssignedTo(this);

    this.firePropertyChange(PROPERTY_TEAM, old, this.team);
  }

  public void clear() {
    if (!this.next.isEmpty()) {
      for (Position p : this.next)
        p.setPrev(null);
    }
    if (this.prev != null) {
      this.prev.removePropertyChangeListener(this);
      this.prev.removeNext(this);
      this.setPrev(null);
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(PROPERTY_NAME)) {
      // refresh if previous positions name has changed
      this.firePropertyChange(PROPERTY_NAME, "", this.name);
    }
  }

  @Override
  public void contentsChanged(ListDataEvent arg0) {
    this.firePropertyChange(PROPERTY_NEXT, null, this.next);

  }

  @Override
  public void intervalAdded(ListDataEvent arg0) {
    this.firePropertyChange(PROPERTY_NEXT, null, this.next);

  }

  @Override
  public void intervalRemoved(ListDataEvent arg0) {
    this.firePropertyChange(PROPERTY_NEXT, null, this.next);
  }

  public static void removeConnections(Position p) {
    if (p.getPrev() != null) {
      p.getPrev().removeNext(p);
      p.setPrev(null);
    }
    for (Position n : p.getNext()) {
      n.setPrev(null);
    }
    p.getNext().clear();
  }

  @Override
  public String toString() {
    return this.name;
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
    Position other = (Position)obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public int compareTo(Position o) {
    if (this.name == null) {
      if (o == null)
        return 0;
      return -1;
    }
    if (o == null)
      return 1;

    if (this.getGroup() == null || o.getGroup() == null) {
      return this.name.compareTo(o.getName());
    }

    int value = this.getGroup().getName().compareTo(o.getGroup().getName());
    if (value == 0) {
      return this.name.compareTo(o.getName());
    }
    return value;
  }
}
