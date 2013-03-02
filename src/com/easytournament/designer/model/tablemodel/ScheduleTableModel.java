package com.easytournament.designer.model.tablemodel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.util.comperator.ScheduleDateComperator;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.common.collect.ArrayListModel;

public class ScheduleTableModel extends DefaultTableModel implements
    ListDataListener {

  private static final long serialVersionUID = 1L;
  private String[] columnNames = {ResourceManager.getText(Text.HOME),
      ResourceManager.getText(Text.AWAY), ResourceManager.getText(Text.PLACE),
      ResourceManager.getText(Text.DATE), ResourceManager.getText(Text.TIME),
      ResourceManager.getText(Text.REFREE)};
  private ArrayListModel<ScheduleEntry> data;
  
  private DateFormat timeFormatter = DateFormat.getTimeInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  private Calendar calendar = new GregorianCalendar(ResourceManager.getLocale());
  private ScheduleDateComperator<ScheduleEntry> comp = new ScheduleDateComperator<ScheduleEntry>();

  public ScheduleTableModel(ArrayListModel<ScheduleEntry> data) {
    super();
    this.data = data;
    this.data.addListDataListener(this);
  }

  @Override
  public Class<?> getColumnClass(int column) {
    switch (column) {
      case 3:
        return Date.class;
      case 5:
        return Refree.class;
      default:
        return String.class;
    }
  }

  public int getColumnCount() {
    if (ScheduleSettings.getInstance().isShowRefrees())
      return columnNames.length;
    return columnNames.length - 1;
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
    if (row >= 0 && row < data.size()) {

      ScheduleEntry tempdata = data.get(row);
      
      if(tempdata == null)
        return null;

      switch (col) {
        case 0:
          return tempdata.getHomePos();

        case 1:
          return tempdata.getAwayPos();

        case 2:
          return tempdata.getPlace();

        case 3:
          return tempdata.getDate().getTime();

        case 4:
          return timeFormatter.format(tempdata.getDate().getTime());

        case 5:
          if (tempdata.getReferees().size() > 0)
            return tempdata.getReferees().get(0);
          return null;
      }
    }
    return null;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    if (rowIndex >= 0 && rowIndex < data.size()) {
      ScheduleEntry tempdata = data.get(rowIndex);
      if(tempdata == null)
        return;

      switch (columnIndex) {
        case 0:
          tempdata.getGroupAssignedTo().getSchedules().remove(tempdata);
          tempdata.setHomePos((Position)value);
          tempdata.getGroupAssignedTo().getSchedules().add(tempdata);
          for (Position pos : tempdata.getGroupAssignedTo().getPositions()) {
            if (pos != value) {
              tempdata.setAwayPos(pos);
              break;
            }
          }
          break;
        case 1:
          tempdata.setAwayPos((Position)value);
          break;
        case 2:
          tempdata.setPlace(value.toString());
          break;
        case 3:
          try {
            calendar.setTime((Date) value);
            tempdata.getDate().set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            Collections.sort(this.data, comp);
          }
          catch (Exception e) {/* do nothing */}
          break;
        case 4:
          try {
            calendar.setTime(timeFormatter.parse((String) value));
            tempdata.getDate().set(Calendar.HOUR, calendar.get(Calendar.HOUR));
            tempdata.getDate().set(Calendar.MINUTE,
                calendar.get(Calendar.MINUTE));
            tempdata.getDate().set(Calendar.SECOND,
                calendar.get(Calendar.SECOND));
            tempdata.getDate().set(Calendar.MILLISECOND,
                calendar.get(Calendar.MILLISECOND));
            tempdata.getDate()
                .set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
            Collections.sort(this.data, comp);
          }
          catch (Exception e) {/* do nothing */}
          break;
        case 5:
          if (value == null)
            tempdata.getReferees().clear();
          else if (tempdata.getReferees().size() > 0)
            tempdata.getReferees().set(0, (Refree)value);
          else
            tempdata.getReferees().add((Refree)value);
      }
      this.data.fireContentsChanged(rowIndex);
      this.fireTableDataChanged();
    }
  }

  public void addShedule(ScheduleEntry e) {
    data.add(e);
  }

  public boolean isCellEditable(int row, int col) {
    return true;
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
