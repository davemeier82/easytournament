package com.easytournament.designer.valueholder;

import java.util.ArrayList;

public class SortableGroup extends Group {
  
  private ArrayList<Integer> intervals = new ArrayList<Integer>();

  public SortableGroup(int numPos) {
    super(numPos);
  }

  public ArrayList<Integer> getIntervals() {
    return intervals;
  }

  public void setIntervals(ArrayList<Integer> intervals) {
    this.intervals = intervals;
  }  

}
