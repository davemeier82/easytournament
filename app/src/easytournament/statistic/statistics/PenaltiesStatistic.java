package easytournament.statistic.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import easytournament.basic.Organizer;
import easytournament.basic.gameevent.GameEventType;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEventEntry;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.designer.valueholder.ScheduleEntry;
import easytournament.statistic.model.tablemodel.StatisticTableModel;
import easytournament.statistic.util.IntegerArrayComparator;



public class PenaltiesStatistic implements Statistic {

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  @Override
  public String getName() {
    return ResourceManager.getText(Text.PENALTIES);
  }

  @Override
  public String getPrintName() {
    return ResourceManager.getText(Text.PENALTIES);
  }

  @Override
  public boolean existsForTeam() {
    return true;
  }

  @Override
  public boolean existsForPlayers() {
    return true;
  }

  @Override
  public void addStatistic(StatisticTableModel tableModel, boolean groupByTeam,
      Team team) {

    HashMap<Integer,Integer[]> entryMap = new HashMap<Integer,Integer[]>();
    HashMap<Integer,String> nameMap = new HashMap<Integer,String>();

    if (groupByTeam) {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      tableModel.setColumnIdentifiers(new String[] {
          ResourceManager.getText(Text.TEAM),
          ResourceManager.getText(Text.PENALTIES)});
      if (t.getTeams().size() < 1)
        return;
      for (Team tm : t.getTeams()) {
        entryMap.put(tm.getId(), new Integer[] {tm.getId(), 0});
        nameMap.put(tm.getId(), tm.getName());
      }

      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed())
          for (GameEventEntry ge : se.getGameEvents()) {
            if (ge.getEvent().getType() == GameEventType.PENALTY) {
              if (ge.getTeam().equals(se.getHomePos().getTeam())) {
                entryMap.get(se.getHomeTeam().getId())[1]++;
              }
              else {
                entryMap.get(se.getAwayTeam().getId())[1]++;
              }
            }
          }
      }
      ArrayList<Integer[]> vals = new ArrayList<Integer[]>();
      Integer[] arr;
      for (Map.Entry<Integer,Integer[]> e : entryMap.entrySet()) {
        arr = e.getValue();
        if (team == null || team.getId() == arr[0].intValue())
          vals.add(arr);
      }
      Collections.sort(vals, new IntegerArrayComparator(new Integer[] {1},
          false));

      for (Integer[] val : vals) {
        tableModel
            .addRow(new String[] {nameMap.get(val[0]), val[1].toString()});
      }
    }
    else {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      strCols.add(1);
      HashMap<Integer,Team> teamMap = new HashMap<Integer,Team>();
      tableModel.setColumnIdentifiers(new String[] {
          ResourceManager.getText(Text.PLAYER),
          ResourceManager.getText(Text.TEAM),
          ResourceManager.getText(Text.PENALTIES)});

      if (t.getTeams().size() < 1)
        return;
      if (team == null) {
        for (Team tm : t.getTeams()) {
          for (Player p : tm.getPlayers()) {
            entryMap.put(p.getId(), new Integer[] {p.getId(), 0});
            nameMap.put(p.getId(), p.toString());
            teamMap.put(p.getId(), tm);
          }
        }
      }
      else {
        for (Player p : team.getPlayers()) {
          entryMap.put(p.getId(), new Integer[] {p.getId(), 0});
          nameMap.put(p.getId(), p.toString());
          teamMap.put(p.getId(), team);
        }
      }
      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed())
          for (GameEventEntry ge : se.getGameEvents()) {
            if (ge.getEvent().getType() == GameEventType.PENALTY)
              for (Player mp : ge.getMainPlayers()) {
                Team tm = teamMap.get(mp.getId());
                if (team == null || tm == team)
                  if (tm == ge.getTeam()) {
                    entryMap.get(mp.getId())[1]++;
                  }
              }
          }
      }
      ArrayList<Integer[]> vals = new ArrayList<Integer[]>();
      for (Map.Entry<Integer,Integer[]> e : entryMap.entrySet()) {
        if (e.getValue()[1] > 0)
          vals.add(e.getValue());
      }
      Collections.sort(vals, new IntegerArrayComparator(new Integer[] {1},
          false));

      for (Integer[] val : vals) {
        tableModel.addRow(new String[] {nameMap.get(val[0]),
            teamMap.get(val[0]).getName(), val[1].toString()});
      }
    }
  }

}
