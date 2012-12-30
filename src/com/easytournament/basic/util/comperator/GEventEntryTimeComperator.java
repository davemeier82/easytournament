package com.easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import com.easytournament.basic.valueholder.GameEventEntry;


public class GEventEntryTimeComperator<E extends GameEventEntry> implements Comparator<E>, Serializable {

  @Override
  public int compare(E o1, E o2) {
    int min = Integer.compare(o1.getTimeMin(), o2.getTimeMin());
    if(min == 0)
      return Integer.compare(o1.getTimeSec(), o2.getTimeSec());

    return min;
  }

}
