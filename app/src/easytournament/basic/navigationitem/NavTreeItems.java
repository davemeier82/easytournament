package easytournament.basic.navigationitem;

import java.util.ArrayList;
import java.util.List;

import easytournament.designer.navigationitem.DesignerItem;
import easytournament.designer.navigationitem.ScheduleItem;
import easytournament.statistic.navigationitem.StatisticItem;
import easytournament.tournament.navigationitem.GamesItem;
import easytournament.tournament.navigationitem.GroupAssignItem;
import easytournament.tournament.navigationitem.TablesItem;

public class NavTreeItems {
  public static final List<NavigationItem> NAVMENU_ITEMS = new ArrayList<NavigationItem>() {
    private static final long serialVersionUID = 1L;

    {
      add(new TournamentDetailsItem()); //TODO move to config file
      add(new TeamsItem());
      add(new RefreesItem());
      add(new DesignerItem());
      add(new ScheduleItem());      
      add(new GroupAssignItem());
      add(new GamesItem());
      add(new TablesItem());
      add(new StatisticItem());
    }
  };
}
