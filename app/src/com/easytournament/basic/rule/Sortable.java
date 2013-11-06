package com.easytournament.basic.rule;

import java.io.Serializable;
import java.util.ArrayList;

import com.easytournament.tournament.valueholder.TableEntry;


public interface Sortable extends Serializable {

  public void sort(ArrayList<TableEntry> te, ArrayList<Integer> intervals, boolean asc);
  public ArrayList<Integer> getEquals(ArrayList<TableEntry> te, ArrayList<Integer> intervals);
}
