package com.easytournament.designer.valueholder;

import java.util.Collections;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.util.comperator.GroupNameComperator;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.LinkedListModel;

public class TournamentPlan extends Model implements ListDataListener {

  public static final String PROPERTY_GROUPS = "groups";
  private static final long serialVersionUID = 6615248444314752699L;
  private ArrayListModel<AbstractGroup> groups = new ArrayListModel<AbstractGroup>();
  GroupNameComperator<AbstractGroup> gnc = new GroupNameComperator<AbstractGroup>();

  public TournamentPlan() {
    groups.addListDataListener(this);
  }

  public ArrayListModel<AbstractGroup> getGroups() {
    return groups;
  }

  /**
   * Groups with starting position
   * @return
   */
  public ArrayListModel<AbstractGroup> getStartGroups() {
    ArrayListModel<AbstractGroup> sgroups = new ArrayListModel<AbstractGroup>();
    for (AbstractGroup g : groups) {
      if (g.getNumStartPos() > 0)
        sgroups.add(g);
    }
    Collections.sort(sgroups, gnc);
    return sgroups;
  }

  public ArrayListModel<AbstractGroup> getOrderedGroups() {
    ArrayListModel<AbstractGroup> sgroups = new ArrayListModel<AbstractGroup>();

    LinkedListModel<AbstractGroup> tmp = new LinkedListModel<AbstractGroup>(
        groups);
    for (AbstractGroup g : groups) {
      if (g.getNumStartPos() > 0) {
        boolean add = true;
        for (Position p : g.getPositions()) {
          if (p.getPrev() != null) {
            add = false;
            break;
          }
        }
        if (add) {
          sgroups.add(g);
          tmp.remove(g);
        }
      }
    }
    Collections.sort(sgroups, gnc);

    while (!tmp.isEmpty()) {
      ArrayListModel<AbstractGroup> toAdd = new ArrayListModel<AbstractGroup>();
      for (AbstractGroup g : tmp) {
        boolean add = true;
        for (int i = 0; i < g.getNumPositions(); i++) {
          Position p = g.getPosition(i);
          if (p.getPrev() != null && p.getPrev().getGroup() != null
              && !sgroups.contains(p.getPrev().getGroup())) {
            add = false;
            break;
          }
        }
        if (add) {
          toAdd.add(g);
        }
      }
      tmp.removeAll(toAdd);
      Collections.sort(toAdd, gnc);
      sgroups.addAll(toAdd);
      toAdd.clear();
    }
    return sgroups;
  }

  public void addGroup(AbstractGroup g) {
    groups.add(g);
  }

  public boolean removeGroup(AbstractGroup g) {    
    Organizer.getInstance().getCurrentTournament().getSchedules().removeAll(g.getSchedules());
    g.getSchedules().clear();
    return groups.remove(g);
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_GROUPS, null, groups);

  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_GROUPS, null, groups);

  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_GROUPS, null, groups);

  }

}
