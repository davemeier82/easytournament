package com.easytournament.basic.rule;

import java.util.ArrayList;
import java.util.Collections;

import com.easytournament.basic.util.comperator.IntegerComparable;
import com.easytournament.tournament.calc.Values;
import com.easytournament.tournament.valueholder.TableEntry;


public class IntegerValueSortable implements Sortable {
  
  protected Values[] add;
  protected Values[] subtract;
  
  public IntegerValueSortable(Values[] add, Values[] subtract){
    this.add = add;
    this.subtract = subtract;
  }
  
  protected Integer getSum(TableEntry t){
    Integer sum = new Integer(0);
    for(Values v : add){
      sum += t.getValue(v);
    }
    for(Values v : subtract){
      sum -= t.getValue(v);
    }
    return sum;
  }

  public void sort(ArrayList<TableEntry> te, ArrayList<Integer> intervals, boolean asc) {
    ArrayList<IntegerComparable> intcomps = new ArrayList<IntegerComparable>();
    Integer begin, end;
    for(int i = 0; i < intervals.size();){
      begin = intervals.get(i++);
      end = intervals.get(i++);
      intcomps.clear();
      for(int j = begin; j <= end; j++){
        TableEntry t = te.get(j);
        intcomps.add(new IntegerComparable(getSum(t), t, asc));
      }
      Collections.sort(intcomps);
      int c = 0;
      for(int j = begin; j <= end; j++){
        te.set(j, intcomps.get(c++).getTe());
      }
    }
  }

  public ArrayList<Integer> getEquals(ArrayList<TableEntry> te, ArrayList<Integer> intervals) {
    ArrayList<Integer> equals = new ArrayList<Integer>();
    
    Integer begin, end;
    for(int i = 0; i < intervals.size();){
      begin = intervals.get(i++);
      end = intervals.get(i++);
      
      Integer curVal = Integer.MIN_VALUE;//value doesn't matter
      Integer newBegin = null;
      int counter = begin;
      
      for(int j = begin; j <= end; j++){
        TableEntry t = te.get(j);
        Integer val = getSum(t);
        
        if (counter == 0 || !val.equals(curVal)) {
          if (newBegin != null && counter-1 != newBegin.intValue()) {
            equals.add(newBegin);
            equals.add(counter-1); 
          }
          newBegin = counter;
          curVal = val;
        }
        counter++;
      }
      if (newBegin != null && counter-1 != newBegin.intValue()){
        equals.add(newBegin);
        equals.add(counter-1);
      }
    }
    return equals;
  }

}
