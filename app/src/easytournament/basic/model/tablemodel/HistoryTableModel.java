package easytournament.basic.model.tablemodel;

import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.HistoryFile;


public class HistoryTableModel extends AbstractTableModel {

  private ArrayList<HistoryFile> history;
  private String[] columnNames = new String[] {
      ResourceManager.getText(Text.NAME), ResourceManager.getText(Text.SPORT),
      ResourceManager.getText(Text.LAST_MODIFIED),
      ResourceManager.getText(Text.PATH)};

  private DateFormat formater = DateFormat.getDateTimeInstance(
      DateFormat.MEDIUM, DateFormat.SHORT, ResourceManager.getLocale());

  public HistoryTableModel(ArrayList<HistoryFile> history) {
    this.history = history;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return history.size();
  }

  @Override
  public Object getValueAt(int row, int col) {
    if (col >= 0 && col < columnNames.length) {
      HistoryFile hf = history.get(history.size() - 1 - row);
      switch (col) {
        case 0:
          return hf.getName();
        case 1:
          return hf.getSport();
        case 2:
          return formater.format(hf.getLastModified());
        case 3:
          return hf.getPath();
      }
    }
    return null;
  }

  @Override
  public String getColumnName(int column) {
    if (column >= 0 && column < columnNames.length)
      return columnNames[column];
    return "";
  }

  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return false;
  }

}
