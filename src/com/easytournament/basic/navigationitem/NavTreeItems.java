package com.easytournament.basic.navigationitem;

import java.util.ArrayList;
import java.util.List;

import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.navigationitem.ScheduleItem;
import com.easytournament.statistic.navigationitem.StatisticItem;
import com.easytournament.tournament.navigationitem.GamesItem;
import com.easytournament.tournament.navigationitem.GroupAssignItem;
import com.easytournament.tournament.navigationitem.TablesItem;





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
