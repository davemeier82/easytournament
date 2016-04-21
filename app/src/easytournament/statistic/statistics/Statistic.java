package easytournament.statistic.statistics;

import easytournament.basic.valueholder.Team;
import easytournament.statistic.model.tablemodel.StatisticTableModel;


public interface Statistic {  
  public String getName();
  public String getPrintName();
  public boolean existsForTeam();
  public boolean existsForPlayers();
  public void addStatistic(StatisticTableModel tm, boolean groupByTeam, Team team);
}
