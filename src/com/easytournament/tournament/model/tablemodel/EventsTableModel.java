package com.easytournament.tournament.model.tablemodel;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.common.collect.ArrayListModel;

public class EventsTableModel extends AbstractTableModel implements ListDataListener {

  private static final long serialVersionUID = 1L;
  private String[] columnNames = new String[]{"", ResourceManager.getText(Text.EVENT), ResourceManager.getText(Text.TIME), "","","",""};
  private ArrayListModel<GameEventEntry> data;
  private ScheduleEntry sentry;

  public EventsTableModel(ArrayListModel<GameEventEntry> data,  ScheduleEntry sentry) {
    super();
    this.data = data;
    this.sentry = sentry;
    this.data.addListDataListener(this);
    this.columnNames[3] = sentry.getHomeTeam().getName();
    this.columnNames[6] = sentry.getAwayTeam().getName();
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    if (data == null)
      return 0;
    return data.size();
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {

    GameEventEntry tempdata = data.get(row);

    switch (col) {
      case 0:
        return tempdata.getEvent().getIcon();
      case 1:
        return tempdata.getEvent().getName();
      case 2:
        String time = tempdata.getTimeMin()+":";
        if(tempdata.getTimeSec() < 10)
          time += "0";
        return time.concat(tempdata.getTimeSec() +"");
      case 3:
        if (tempdata.getTeam().equals(sentry.getHomeTeam()) && tempdata.getMainPlayers().size() > 0) {
          Player p = tempdata.getMainPlayers().get(0);
          if (p != null) {
            String name = p.getNumber().length()>0?"["+p.getNumber()+"] ":"";
            if (p.getPrename().length() > 0)
              name += p.getPrename().substring(0, 1) + ". ";
            name += p.getName();
            return name;
          }
        }
        return "";
      case 4:
        return tempdata.getSummedHomePoints();

      case 5:
        return tempdata.getSummedAwayPoints();

      case 6:
        if (!tempdata.getTeam().equals(sentry.getHomeTeam()) && tempdata.getMainPlayers().size() > 0) {
          Player p = tempdata.getMainPlayers().get(0);
          if (p != null) {
            String name = p.getNumber().length()>0?"["+p.getNumber()+"] ":"";
            if (p.getPrename().length() > 0)
              name += p.getPrename().substring(0, 1) + ". ";
            name += p.getName();
            return name;
          }
        }
        return "";
    }
    return null;
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public GameEventEntry get(int row) {
    return data.get(row);
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    this.fireTableDataChanged();
    
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    this.fireTableDataChanged();
    
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    this.fireTableDataChanged();
    
  }

}
