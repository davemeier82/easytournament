package com.easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import com.easytournament.designer.valueholder.AbstractGroup;


public class GroupNameComperator<R extends AbstractGroup> implements Comparator<R>, Serializable {

  @Override
  public int compare(AbstractGroup g0, AbstractGroup g1) {
    return g0.getName().toLowerCase().compareTo(g1.getName().toLowerCase()) ;
  }

}
