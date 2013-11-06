package com.easytournament.statistic.util;

import java.util.Comparator;

public class IntegerArrayComparator implements Comparator<Integer[]> {

  private Integer[] sortOrder;
  private boolean ascending;

  public IntegerArrayComparator(Integer[] sortOrder, boolean ascending) {
    this.sortOrder = sortOrder;
    this.ascending = ascending;
  }

  @Override
  public int compare(Integer[] o1, Integer[] o2) {
    int p, c = 0;
    for (int i = 0; i < sortOrder.length; i++) {
      p = sortOrder[i];
      c = o1[p].compareTo(o2[p]);
      if (c != 0) {
        if (ascending)
          return c;
        return c == 1? -1 : 1;
      }
    }
    if (ascending || c == 0)
      return c;
    return c == 1? -1 : 1;
  }

}
