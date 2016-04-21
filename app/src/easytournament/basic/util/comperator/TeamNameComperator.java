package easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import easytournament.basic.valueholder.Team;


public class TeamNameComperator implements Comparator<Team>, Serializable {

  @Override
  public int compare(Team t0, Team t1) {
    return t0.getName().toLowerCase().compareTo(t1.getName().toLowerCase()) ;
  }

}
