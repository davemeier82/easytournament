package com.easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import com.easytournament.basic.valueholder.GameEventEntry;


public class GEventEntryTimeComperator<E extends GameEventEntry> implements Comparator<E>, Serializable {

  @Override
  public int compare(E o1, E o2) {
    //TODO  Integer.compare is available with Java 1.7
    int min = new Integer(o1.getTimeMin()).compareTo(o2.getTimeMin());
    if(min == 0)
      return new Integer(o1.getTimeSec()).compareTo(o2.getTimeSec());

    return min;
  }

}
