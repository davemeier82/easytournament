package com.easytournament.basic.valueholder;

import java.util.EnumMap;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.rule.*;
import com.easytournament.tournament.calc.Values;
import com.jgoodies.binding.beans.Model;



public class Rule extends Model implements Cloneable {

  public static final String PROPERTY_ASCENDING = "ascending";  

  public static EnumMap<RuleType,Rule> ruleMap;
  
  public static void initRules() {
    ruleMap = new EnumMap<RuleType,Rule>(RuleType.class);
    ruleMap.put(RuleType.POINTS_RULE, new Rule(RuleType.POINTS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_POINTS, Values.AWAY_POINTS}, new Values[0]), false));
    ruleMap.put(RuleType.GOALDIFF_RULE, new Rule(RuleType.GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS, Values.SCORED_AWAY_GOALS}, new Values[]{Values.RECEIVED_HOME_GOALS, Values.RECEIVED_AWAY_GOALS}), false));
    ruleMap.put(RuleType.SCORED_GOALS_RULE, new Rule(RuleType.SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS, Values.SCORED_AWAY_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.RECEIVED_GOALS_RULE, new Rule(RuleType.RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_HOME_GOALS, Values.RECEIVED_AWAY_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.HOME_SCORED_GOALS_RULE, new Rule(RuleType.HOME_SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.HOME_RECEIVED_GOALS_RULE, new Rule(RuleType.HOME_RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_HOME_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_SCORED_GOALS_RULE, new Rule(RuleType.AWAY_SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_AWAY_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_RECEIVED_GOALS_RULE, new Rule(RuleType.AWAY_RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_AWAY_GOALS}, new Values[0]), false));
    ruleMap.put(RuleType.WINS_RULE, new Rule(RuleType.WINS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_WINS, Values.AWAY_WINS}, new Values[0]), false));
    ruleMap.put(RuleType.DRAWS_RULE, new Rule(RuleType.DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DRAWS, Values.AWAY_DRAWS}, new Values[0]), false));
    ruleMap.put(RuleType.DEFEATS_RULE, new Rule(RuleType.DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DEFEATS, Values.AWAY_DEFEATS}, new Values[0]), false));
    ruleMap.put(RuleType.RANDOM_RULE, new Rule(RuleType.RANDOM_RULE, new Random(), false));
    // ruleMap.put(new Rule(CARDS, false)); //TODO add new rules
    // ruleMap.put(new Rule(YELLOWCARDS, false)); 
    // ruleMap.put(new Rule(REDCARDS, false));
    // ruleMap.put(new Rule(ALPHABET, false));
    ruleMap.put(RuleType.HOME_POINTS_RULE, new Rule(RuleType.HOME_POINTS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_POINTS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_POINTS_RULE, new Rule(RuleType.AWAY_POINTS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_POINTS}, new Values[0]), false));
    ruleMap.put(RuleType.HOME_GOALDIFF_RULE, new Rule(RuleType.HOME_GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS}, new Values[]{Values.RECEIVED_HOME_GOALS}), false));
    ruleMap.put(RuleType.AWAY_GOALDIFF_RULE, new Rule(RuleType.AWAY_GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_AWAY_GOALS}, new Values[]{Values.RECEIVED_AWAY_GOALS}), false));
    ruleMap.put(RuleType.HOME_WINS_RULE, new Rule(RuleType.HOME_WINS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_WINS}, new Values[0]), false));
    ruleMap.put(RuleType.HOME_DRAWS_RULE, new Rule(RuleType.HOME_DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DRAWS}, new Values[0]), false));
    ruleMap.put(RuleType.HOME_DEFEATS_RULE, new Rule(RuleType.HOME_DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DEFEATS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_WINS_RULE, new Rule(RuleType.AWAY_WINS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_WINS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_DRAWS_RULE, new Rule(RuleType.AWAY_DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_DRAWS}, new Values[0]), false));
    ruleMap.put(RuleType.AWAY_DEFEATS_RULE, new Rule(RuleType.AWAY_DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_DEFEATS}, new Values[0]), false));
    
    // DIRECT GAMES
    ruleMap.put(RuleType.DIRECT_GAME_POINT_RULE, new Rule(RuleType.DIRECT_GAME_POINT_RULE, new DirectGameSortable(new Rule(RuleType.POINTS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_POINTS, Values.AWAY_POINTS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_GOALDIFF_RULE, new Rule(RuleType.DIRECT_GAME_GOALDIFF_RULE, new DirectGameSortable(new Rule(RuleType.GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS, Values.SCORED_AWAY_GOALS}, new Values[]{Values.RECEIVED_HOME_GOALS, Values.RECEIVED_AWAY_GOALS}), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_SCORED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_SCORED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS, Values.SCORED_AWAY_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_RECEIVED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_RECEIVED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_HOME_GOALS, Values.RECEIVED_AWAY_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_SCORED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_SCORED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_RECEIVED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_RECEIVED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_HOME_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_SCORED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_SCORED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_SCORED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_AWAY_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_RECEIVED_GOALS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_RECEIVED_GOALS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_RECEIVED_GOALS_RULE, new IntegerValueSortable(new Values[]{Values.RECEIVED_AWAY_GOALS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_WINS_RULE, new Rule(RuleType.DIRECT_GAME_WINS_RULE, new DirectGameSortable(new Rule(RuleType.WINS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_WINS, Values.AWAY_WINS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_DRAWS_RULE, new Rule(RuleType.DIRECT_GAME_DRAWS_RULE, new DirectGameSortable(new Rule(RuleType.DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DRAWS, Values.AWAY_DRAWS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_DEFEATS_RULE, new Rule(RuleType.DIRECT_GAME_DEFEATS_RULE, new DirectGameSortable(new Rule(RuleType.DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DEFEATS, Values.AWAY_DEFEATS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_POINTS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_POINTS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_POINTS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_POINTS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_POINTS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_POINTS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_POINTS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_POINTS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_GOALDIFF_RULE, new Rule(RuleType.DIRECT_GAME_HOME_GOALDIFF_RULE, new DirectGameSortable(new Rule(RuleType.HOME_GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_HOME_GOALS}, new Values[]{Values.RECEIVED_HOME_GOALS}), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_GOALDIFF_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_GOALDIFF_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_GOALDIFF_RULE, new IntegerValueSortable(new Values[]{Values.SCORED_AWAY_GOALS}, new Values[]{Values.RECEIVED_AWAY_GOALS}), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_WINS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_WINS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_WINS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_WINS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_DRAWS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_DRAWS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DRAWS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_HOME_DEFEATS_RULE, new Rule(RuleType.DIRECT_GAME_HOME_DEFEATS_RULE, new DirectGameSortable(new Rule(RuleType.HOME_DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.HOME_DEFEATS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_WINS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_WINS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_WINS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_WINS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_DRAWS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_DRAWS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_DRAWS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_DRAWS}, new Values[0]), false)), false));
    ruleMap.put(RuleType.DIRECT_GAME_AWAY_DEFEATS_RULE, new Rule(RuleType.DIRECT_GAME_AWAY_DEFEATS_RULE, new DirectGameSortable(new Rule(RuleType.AWAY_DEFEATS_RULE, new IntegerValueSortable(new Values[]{Values.AWAY_DEFEATS}, new Values[0]), false)), false));
  }

  private RuleType rule;
  private String name;
  private Sortable sortable;
  private boolean ascending;
  
  public Rule(RuleType rule, Sortable sortable, boolean ascending) {
    super();
    this.rule = rule;
    this.name = ResourceManager.getRuleName(rule);
    this.sortable = sortable;
    this.ascending = ascending;
  }

  public boolean isAscending() {
    return ascending;
  }

  public void setAscending(boolean ascending) {
    boolean old = this.ascending;
    this.ascending = ascending;
    this.firePropertyChange(PROPERTY_ASCENDING, old, this.ascending);
  }

  public RuleType getRule() {
    return rule;
  }

  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((rule == null)? 0 : rule.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Rule other = (Rule)obj;
    if (rule == null) {
      if (other.rule != null)
        return false;
    }
    else if (!rule.equals(other.rule))
      return false;
    return true;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String getName() {
    return name;
  }

  public Sortable getSortable() {
    return sortable;
  }
  

}
