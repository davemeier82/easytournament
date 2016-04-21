package easytournament.basic.rule;

import java.util.ArrayList;
import java.util.HashMap;

import easytournament.basic.valueholder.Rule;
import easytournament.basic.valueholder.Team;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.ScheduleEntry;
import easytournament.designer.valueholder.SortableGroup;
import easytournament.tournament.calc.Calculator;
import easytournament.tournament.valueholder.TableEntry;

public class DirectGameSortable implements Sortable {
  
  private ArrayList<Integer> lastIntevals;
  private Rule rule;
  
  public DirectGameSortable(Rule rule)
  {
    this.rule = rule;
  }

  @Override
  public void sort(ArrayList<TableEntry> te, ArrayList<Integer> intervals,
      boolean asc) {
    if(te.get(0).getGroup() instanceof DuellGroup)
      return;
    
    AbstractGroup originalGroup = te.get(0).getGroup();
   
    Integer begin, end;
    for(int i = 0; i < intervals.size();){
      begin = intervals.get(i++);
      end = intervals.get(i++);
      
      SortableGroup g = new SortableGroup(end-begin+1);
      
      HashMap<Team, TableEntry> entries = new HashMap<Team, TableEntry>();
      int p = 0;
      for(int j = begin; j <= end; j++){
        TableEntry t = te.get(j);
        g.getPosition(p++).setTeam(t.getTeam());
        g.getTeams().add(t.getTeam());
        entries.put(t.getTeam(), t);
      }
      
      if(originalGroup.isDefaultSettings())
        g.setDefaultSettings(true);
      else
      {
        g.setDefaultSettings(false);
        g.setSettings( originalGroup.getSettings());
      }
           
      g.setDefaultRules(false);
      this.rule.setAscending(asc);
      g.getRules().add(rule);
      
      for(ScheduleEntry se : originalGroup.getSchedules())
      {
        if(g.getTeams().contains(se.getHomeTeam()) && g.getTeams().contains(se.getAwayTeam()))
          g.getSchedules().add(se);
      }
      
      Calculator.calcTableEntries(g, false);
      
      p = 0;
      for(int j = begin; j <= end; j++){
        Team t = g.getTable().get(p++).getTeam();
        te.set(j, entries.get(t));
      }
      
      ArrayList<Integer> newintervals = g.getIntervals();
      
      if(newintervals.size() > 0)
      {
        if(newintervals.size() != 2 || newintervals.get(0) != 0 || 
            newintervals.get(newintervals.size()-1) != g.getTeams().size()-1 )
        {
          for(Integer interval : newintervals)
          {
            interval += begin;
          }
          this.sort(te, newintervals, asc);
        }
      }        
      
      lastIntevals = new ArrayList<Integer>();
      
      for(Integer interval : newintervals)
      {
        lastIntevals.add(interval+begin);
      }     
    }
  }

  @Override
  public ArrayList<Integer> getEquals(ArrayList<TableEntry> te,
      ArrayList<Integer> intervals) {
    if(te.get(0).getGroup() instanceof DuellGroup)
      return intervals;    
    
    return lastIntevals;
  }

}
