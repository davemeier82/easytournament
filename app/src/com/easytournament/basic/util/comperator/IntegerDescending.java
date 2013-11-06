package com.easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

public class IntegerDescending implements Comparator<Integer>, Serializable {

  public int compare(Integer o1, Integer o2) {
    if (o1 < o2)
      return 1;
    if (o1 > o2)
      return -1;
    return 0;
  }

}
