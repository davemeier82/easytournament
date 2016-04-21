package easytournament.basic.model.tablemodel;

import javax.swing.table.AbstractTableModel;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.gameevent.GameEventType;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEvent;

public class GameEventsTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -4419435795695572019L;
  private String[] columnNames = {"", ResourceManager.getText(Text.TYPE), ResourceManager.getText(Text.NAME)};
  private ArrayListModel<GameEvent> data = new ArrayListModel<GameEvent>();

  public GameEventsTableModel(ArrayListModel<GameEvent> data) {
    super();
    this.data = data;
  }
  
  public GameEventsTableModel(String[] columNames, ArrayListModel<GameEvent> data) {
    super();
    this.data = data;
    this.columnNames = columNames;
  }
  
  public GameEventsTableModel(){
    super();
  }
  
  public void setData(ArrayListModel<GameEvent> events){
    this.data = events;
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    if (data == null)
      return 0;
    return data.getSize();
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
    switch(col){
      case 0:
        return data.get(row).getIcon();
      case 1:
        return GameEventType.getName(data.get(row).getType());
      case 2:
        return data.get(row).getName();
    }
    return null;
  }

  public GameEvent getEventAt(int row) {
    return data.get(row);
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public void addRow(GameEvent e) {
    data.add(e);
    this.fireTableDataChanged();
  }
  
  public void addRow(int index, GameEvent e) {
    data.add(index, e);
    this.fireTableDataChanged();
  }
  
  public void removeRow(int row) {
    data.remove(row);
    this.fireTableDataChanged();
  }

  public ArrayListModel<GameEvent> getData() {
    return data;    
  }

}
