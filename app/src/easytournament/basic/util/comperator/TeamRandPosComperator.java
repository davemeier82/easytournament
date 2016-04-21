package easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import easytournament.tournament.valueholder.TableEntry;


public class TeamRandPosComperator implements Comparator<TableEntry>, Serializable {

  @Override
  public int compare(TableEntry t0, TableEntry t1) {
    return t0.getTeam().getRandomPosition().compareTo(t1.getTeam().getRandomPosition()) ;
  }

}
