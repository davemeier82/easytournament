package com.easytournament.tournament.calc;

import java.util.*;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.rule.Sortable;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.designer.valueholder.SortableGroup;
import com.easytournament.tournament.valueholder.TableEntry;
import com.jgoodies.common.collect.ArrayListModel;


public class Calculator {
  public static void calcTableEntries(AbstractGroup g, boolean calcRecursive) {
	if(g == null){
		return;
	}
		
    HashMap<Integer,TableEntry> entries = new HashMap<Integer,TableEntry>();
    SportSettings set = null;
    if (g.isDefaultSettings()) {
      set = Organizer.getInstance().getCurrentTournament().getSettings();
    }
    else {
      set = g.getSettings();
    }
    ArrayList<Team> teams = g.getTeams();
    if (teams.size() != g.getPositions().size()){
//      for(Position p : g.getPositions()){
//        if(p.getPrev() != null && p.getPrev().getTeam() != null){
//          p.getGroup().getTeams().remove(p.getPrev().getTeam());
//          p.setTeam(null);
//        }
//      }
      g.setTable(new ArrayListModel<TableEntry>());
      return; 
    }

    for (Team t : teams) {
      TableEntry temp = new TableEntry(t, g);
      entries.put(t.getId(), temp);
    }
    
    int nGameTimes = set.getNumGameTimes();
    int nOvertimes = set.getNumOvertimes();

    boolean oneGamePlayed = false;
    for (ScheduleEntry se : g.getSchedules()) {
      TableEntry home = entries.get(se.getHomeTeam().getId());
      TableEntry away = entries.get(se.getAwayTeam().getId());
      if (home == null || away == null || !se.isGamePlayed())
        continue;
      oneGamePlayed = true;
      ArrayList<Integer> results = se.getResults();
      ArrayList<Boolean> checked = se.getChecked();
      int homeScore = 0, awayScore = 0;
      int i = 0;
      if(results.size() < 2)
        continue;
      if(results.size() < (nGameTimes+2)*2)
      {
        homeScore += results.get(results.size()-2);
        awayScore += results.get(results.size()-1);
      } else
      {
        for(; i < nGameTimes*2;)
        {
          homeScore += results.get(i++);
          awayScore += results.get(i++);
        }
      }
      home.setValue(Values.GAMES, home.getValue(Values.GAMES) + 1);
      away.setValue(Values.GAMES, away.getValue(Values.GAMES) + 1);

      if (homeScore > awayScore) {
        home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
            + set.getPointPerHomeWin());
        away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
            + set.getPointPerAwayDefeat());
        home.setValue(Values.HOME_WINS, home.getValue(Values.HOME_WINS) + 1);
        away.setValue(Values.AWAY_DEFEATS,
            away.getValue(Values.AWAY_DEFEATS) + 1);
      }
      else if (homeScore < awayScore) {
        away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
            + set.getPointPerAwayWin());
        home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
            + set.getPointPerHomeDefeat());
        away.setValue(Values.AWAY_WINS, away.getValue(Values.AWAY_WINS) + 1);
        home.setValue(Values.HOME_DEFEATS,
            home.getValue(Values.HOME_DEFEATS) + 1);
      }
      else {
        boolean overtimePlayed = false;
        boolean penaltyPlayed = false;
        int pIndex = (nGameTimes + nOvertimes)*2;
        if(checked.size() >= (nGameTimes + nOvertimes +2)*2)
        {
          for(int j = i; j < (nGameTimes + nOvertimes)*2;j++)
          {
            if(checked.get(j))
            {
              overtimePlayed = true;
              break;
            }
          }
          
          penaltyPlayed = checked.get(pIndex) || checked.get(pIndex+1);
        }
        if(penaltyPlayed)
        {
          for(; i < pIndex;)
          {
            if(checked.get(i))
            {
              homeScore += results.get(i++);
              awayScore += results.get(i++);
            }
            else
            {
              i += 2;
            }
          }
          homeScore += results.get(pIndex);
          awayScore += results.get(pIndex+1);
          
          if (homeScore > awayScore) {
            home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
                + set.getPointPerHomeWinPenalty());
            away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
                + set.getPointPerAwayDefeatPenalty());
            home.setValue(Values.HOME_WINS, home.getValue(Values.HOME_WINS) + 1);
            away.setValue(Values.AWAY_DEFEATS,
                away.getValue(Values.AWAY_DEFEATS) + 1);
          }
          else {
            away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
                + set.getPointPerAwayWinPenalty());
            home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
                + set.getPointPerHomeDefeatPenalty());
            away.setValue(Values.AWAY_WINS, away.getValue(Values.AWAY_WINS) + 1);
            home.setValue(Values.HOME_DEFEATS,
                home.getValue(Values.HOME_DEFEATS) + 1);
          }
          
        }
        else if(overtimePlayed)
        {
          for(; i < pIndex;)
          {
            if(checked.get(i))
            {
              homeScore += results.get(i++);
              awayScore += results.get(i++);
            }
            else
            {
              i += 2;
            }
          }
          if (homeScore > awayScore) {
            home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
                + set.getPointPerHomeWinOvertime());
            away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
                + set.getPointPerAwayDefeatOvertime());
            home.setValue(Values.HOME_WINS, home.getValue(Values.HOME_WINS) + 1);
            away.setValue(Values.AWAY_DEFEATS,
                away.getValue(Values.AWAY_DEFEATS) + 1);
          }
          else if (homeScore < awayScore) {
            away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
                + set.getPointPerAwayWinOvertime());
            home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
                + set.getPointPerHomeDefeatOvertime());
            away.setValue(Values.AWAY_WINS, away.getValue(Values.AWAY_WINS) + 1);
            home.setValue(Values.HOME_DEFEATS,
                home.getValue(Values.HOME_DEFEATS) + 1);
          } else
          {
            home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
                + set.getPointPerHomeDrawOvertime());
            away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
                + set.getPointPerAwayDrawOvertime());
            away.setValue(Values.AWAY_DRAWS, away.getValue(Values.AWAY_DRAWS) + 1);
            home.setValue(Values.HOME_DRAWS, home.getValue(Values.HOME_DRAWS) + 1);
          }
        }
        else
        {
          home.setValue(Values.HOME_POINTS, home.getValue(Values.HOME_POINTS)
              + set.getPointPerHomeDraw());
          away.setValue(Values.AWAY_POINTS, away.getValue(Values.AWAY_POINTS)
              + set.getPointPerAwayDraw());
          away.setValue(Values.AWAY_DRAWS, away.getValue(Values.AWAY_DRAWS) + 1);
          home.setValue(Values.HOME_DRAWS, home.getValue(Values.HOME_DRAWS) + 1);
        }        

      }
      
      home.setValue(Values.SCORED_HOME_GOALS,
          home.getValue(Values.SCORED_HOME_GOALS) + homeScore);
      away.setValue(Values.SCORED_AWAY_GOALS,
          away.getValue(Values.SCORED_AWAY_GOALS) + awayScore);
      home.setValue(Values.RECEIVED_HOME_GOALS,
          home.getValue(Values.RECEIVED_HOME_GOALS) + awayScore);
      away.setValue(Values.RECEIVED_AWAY_GOALS,
          away.getValue(Values.RECEIVED_AWAY_GOALS) + homeScore);
    }

    ArrayListModel<TableEntry> sortedentries = new ArrayListModel<TableEntry>();
    
    for(Position pos : g.getPositions()){
      sortedentries.add(entries.get(pos.getTeam().getId()));
    }

    ArrayList<Rule> r = null;
    if(g.isDefaultRules())
      r = Organizer.getInstance().getCurrentTournament().getDefaultRules();
    else
      r = g.getRules();
    
    if(r.size() < 1 || !oneGamePlayed) {
      g.setTable(sortedentries);
      return;
    }
    
    int ruleCount = 0;
    Rule rule = r.get(ruleCount);
    boolean asc = rule.isAscending();
    Sortable s = rule.getSortable();
    ArrayList<Integer> intervals =  new ArrayList<Integer>();
    intervals.add(0);
    intervals.add(sortedentries.size()-1);
    s.sort(sortedentries, intervals, asc);

    ruleCount++;
    for (; ruleCount < r.size(); ruleCount++) {
      intervals = s.getEquals(sortedentries, intervals);
      if(intervals.size() == 0)
        break;
      rule = r.get(ruleCount);
      asc = rule.isAscending();
      s = rule.getSortable();
      s.sort(sortedentries, intervals, asc);
    }
    
    if(g instanceof SortableGroup)
    {
      intervals = s.getEquals(sortedentries, intervals);
      ((SortableGroup) g).setIntervals(intervals);
    
    }
    g.setTable(sortedentries);
    
    ArrayList<Position> positions = g.getPositions();
    
    Set<AbstractGroup> followingGroups = new HashSet<AbstractGroup>();
    
    for (int i = 0; i < positions.size(); i++) {
      Team newTeam = sortedentries.get(i).getTeam();
      Position p = positions.get(i);
      for (Position next : p.getNext()) {
        Team old = next.getTeam();
        AbstractGroup nextGroup = next.getGroup();
        if (old != null) {
          nextGroup.getTeams().remove(old);
        }        
        next.setTeam(newTeam);
        nextGroup.getTeams().add(newTeam);
        if(calcRecursive)
          followingGroups.add(nextGroup);
      }
    }
    for(AbstractGroup ag : followingGroups){
      Calculator.calcTableEntries(ag, true);
    }
   
    return;
  }
}
