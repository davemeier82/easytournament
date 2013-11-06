package com.easytournament.statistic.gui.editor;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.easytournament.basic.valueholder.GameEvent;

public class EventSelectionEditor extends AbstractCellEditor implements
    TableCellEditor {

  private JComboBox<GameEvent> combobox;
  private ArrayList<GameEvent> events;

  public EventSelectionEditor(ArrayList<GameEvent> events) {
    this.events = events;
    combobox = new JComboBox<GameEvent>();
  }

  @Override
  public Object getCellEditorValue() {
    return combobox.getSelectedItem();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    Object tmp = table.getValueAt(row, column);
    this.combobox.removeAllItems();
    boolean contains = false;
    for (GameEvent e : this.events) {
      this.combobox.addItem(e);
      if (e == tmp)
        contains = true;
    }
    if (contains) {
      combobox.setSelectedItem(tmp);
      combobox.setVisible(true);
    }
    return combobox;
  }

}
