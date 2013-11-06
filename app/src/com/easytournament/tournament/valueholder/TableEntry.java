package com.easytournament.tournament.valueholder;

import java.io.Serializable;
import java.util.EnumMap;

import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.tournament.calc.Values;



public class TableEntry implements Serializable {

  private Team team;
  private AbstractGroup group;
  private EnumMap<Values,Integer> values = new EnumMap<Values,Integer>(
      Values.class);

  public TableEntry(Team team, AbstractGroup group) {
    this.team = team;
    this.group = group;
  }

  public Integer getValue(Values val) {
    Integer value = values.get(val);
    if (value == null)
      return 0;
    return value;
  }

  public void setValue(Values val, Integer i) {
    values.put(val, i);
  }

  public Team getTeam() {
    return team;
  }

  public AbstractGroup getGroup() {
    return group;
  }
  
}
