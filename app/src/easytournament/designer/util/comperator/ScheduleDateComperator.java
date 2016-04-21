package easytournament.designer.util.comperator;

import java.util.Comparator;

import easytournament.designer.valueholder.ScheduleEntry;


public class ScheduleDateComperator<R extends ScheduleEntry> implements Comparator<R> {

  @Override
  public int compare(ScheduleEntry g0, ScheduleEntry g1) {
    return g0.getDate().compareTo(g1.getDate()) ;
  }

}
