package com.easytournament.designer.util.comperator;

import java.util.Comparator;

import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.Position;

public class PositionComparator<T extends Position> implements Comparator<T> {

  public static final String PROPERTY_SHOW_TEAMS = "showTeamNames";
  private boolean showTeamNames = false;

  @Override
  public int compare(T p1, T p2) {
    if (p1 == null) {
      if (p2 == null)
        return 0;
      return -1;
    }
    if (p2 == null)
      return 1;

    if (this.showTeamNames) {
      Team t1 = p1.getTeam();
      Team t2 = p2.getTeam();

      if (t1 == null) {
        if (t2 == null) {
          return p1.compareTo(p2);
        }
        return p1.getName().compareTo(t2.getName());
      }

      if (t2 == null) {
        return t1.getName().compareTo(p2.getName());
      }
      return t1.getName().compareTo(t2.getName());
    }
    return p1.compareTo(p2);
  }

  /**
   * @return the showTeamNames
   */
  public boolean isShowTeamNames() {
    return this.showTeamNames;
  }

  /**
   * @param showTeamNames
   *          the showTeamNames to set
   */
  public void setShowTeamNames(boolean showTeamNames) {
    this.showTeamNames = showTeamNames;
  }

}
