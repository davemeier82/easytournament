package easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import easytournament.basic.valueholder.Rule;


public class RuleNameComperator<R extends Rule> implements Comparator<R>, Serializable {

  @Override
  public int compare(Rule r0, Rule r1) {
    return r0.getName().toLowerCase().compareTo(r1.getName().toLowerCase()) ;
  }

}
