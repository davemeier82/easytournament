package easytournament.tournament.model.tablemodel;

import java.text.DateFormat;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Refree;
import easytournament.designer.settings.ScheduleSettings;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.Position;
import easytournament.designer.valueholder.ScheduleEntry;
import easytournament.tournament.calc.Calculator;

public class GamesTableModel extends AbstractTableModel implements
    ListDataListener {

  private static final long serialVersionUID = 1L;
  private String[] columnNames = {"", ResourceManager.getText(Text.HOME), "",
      "", ResourceManager.getText(Text.AWAY),
      ResourceManager.getText(Text.PLACE), ResourceManager.getText(Text.DATE),
      ResourceManager.getText(Text.TIME), ResourceManager.getText(Text.REFEREE)};
  private ArrayListModel<ScheduleEntry> data = new ArrayListModel<ScheduleEntry>();
  private DateFormat dateFormatter = DateFormat.getDateInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  private DateFormat timeFormatter = DateFormat.getTimeInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  private boolean checkboxVisible = true;

  public GamesTableModel(ArrayListModel<ScheduleEntry> data) {
    super();
    this.data = data;
    this.data.addListDataListener(this);
  }

  public int getColumnCount() {
    if (ScheduleSettings.getInstance().isShowRefrees())
      return checkboxVisible? columnNames.length : columnNames.length - 1;
    return checkboxVisible? columnNames.length - 1 : columnNames.length - 2;
  }

  @Override
  public Class<?> getColumnClass(int column) {
    if (!checkboxVisible)
      column++;

    switch (column) {
      case 0:
        return Boolean.class;
      case 2:
      case 3:
        return String.class;
      case 1:
      case 4:
        return Position.class;
      case 8:
        return Refree.class;
      default:
        return String.class;
    }
  }

  public void setCheckBoxColumnVisible(boolean visible) {
    checkboxVisible = visible;
    this.fireTableStructureChanged();
    this.fireTableDataChanged();
  }

  public boolean isCheckBoxColumnVisible() {
    return this.checkboxVisible;
  }

  public int getRowCount() {
    if (data == null)
      return 0;
    return checkboxVisible? data.size() : data.size() - 1;
  }

  public String getColumnName(int col) {
    return checkboxVisible? columnNames[col] : columnNames[col + 1];
  }

  public Object getValueAt(int row, int col) {

    if (row >= 0 && row < data.size()) {
      if (!checkboxVisible)
        col++;

      ScheduleEntry tempdata = data.get(row);
      if (tempdata == null)
        return null;

      switch (col) {
        case 0:
          return tempdata.isGamePlayed();
        case 1:
          return tempdata.getHomePos();
        case 2:
          if (tempdata.isGamePlayed())
            return Integer.toString(tempdata.getHomeScore());
          return "";
        case 3:
          if (tempdata.isGamePlayed())
            return Integer.toString(tempdata.getAwayScore());
          return "";
        case 4:
          return tempdata.getAwayPos();
        case 5:
          return tempdata.getPlace();
        case 6:
          return dateFormatter.format(tempdata.getDate().getTime());
        case 7:
          return timeFormatter.format(tempdata.getDate().getTime());
        case 8:
          if (tempdata.getReferees().size() > 0)
            return tempdata.getReferees().get(0);
          return null;
      }
    }
    return null;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    if (!checkboxVisible)
      columnIndex++;

    if (rowIndex >= 0 && rowIndex < data.size()) {

      ScheduleEntry tempdata = data.get(rowIndex);
      if (tempdata == null)
        return;

      switch (columnIndex) {
        case 0:
          tempdata.setNumHomeGoals(0);
          tempdata.setNumAwayGoals(0);
          tempdata.setGamePlayed((Boolean)value);
          break;
        case 2:
          try {
            tempdata.setNumHomeGoals(Integer.parseInt((String)value));
            tempdata.setGamePlayed(true);
          }
          catch (Exception e) {
            // do nothing
          }
          break;
        case 3:
          try {
            tempdata.setNumAwayGoals(Integer.parseInt((String)value));
            tempdata.setGamePlayed(true);
          }
          catch (Exception e) {
            // do nothing
          }
          break;
        case 5:
          tempdata.setPlace((String)value);
          break;
        case 8:
          if (value == null)
            tempdata.getReferees().clear();
          else if (tempdata.getReferees().size() > 0)
            tempdata.getReferees().set(0, (Refree)value);
          else
            tempdata.getReferees().add((Refree)value);
      }
      AbstractGroup g = tempdata.getGroupAssignedTo();
      Calculator.calcTableEntries(g, true);
      this.fireTableDataChanged();
    }
  }

  public void addSchedule(ScheduleEntry e) {
    data.add(e);
  }

  public boolean isCellEditable(int row, int col) {
    if (!checkboxVisible)
      col++;

    if (col == 5 || col == 8)
      return true;
    if (col == 2 || col == 3)
      return !data.get(row).isAdvancedGoalMode()
          && data.get(row).getAwayTeam() != null;
    return false;
  }

  public ScheduleEntry get(int row) {
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
