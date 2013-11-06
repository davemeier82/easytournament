package com.easytournament.basic.rule;

import java.util.ArrayList;
import java.util.Collections;

import com.easytournament.basic.util.comperator.TeamRandPosComperator;
import com.easytournament.tournament.valueholder.TableEntry;


public class Random implements Sortable {
  
  TeamRandPosComperator comp = new TeamRandPosComperator();

  @Override
  public void sort(ArrayList<TableEntry> te, ArrayList<Integer> intervals,
      boolean asc) {
    ArrayList<TableEntry> tEntries = new ArrayList<TableEntry>();
    Integer begin, end;
    for (int i = 0; i < intervals.size();) {
      begin = intervals.get(i++);
      end = intervals.get(i++);
      tEntries.clear();
      for (int j = begin; j <= end; j++) {
        tEntries.add(te.get(j));
      }
      Collections.sort(tEntries, comp);
      int c = 0;
      for (int j = begin; j <= end; j++) {
        te.set(j, tEntries.get(c++));
      }
    }

  }

  @Override
  public ArrayList<Integer> getEquals(ArrayList<TableEntry> te,
      ArrayList<Integer> intervals) {
    return new ArrayList<Integer>();
  }

}
