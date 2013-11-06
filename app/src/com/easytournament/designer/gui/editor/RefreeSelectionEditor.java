package com.easytournament.designer.gui.editor;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.easytournament.basic.valueholder.Refree;

public class RefreeSelectionEditor extends AbstractCellEditor implements
    TableCellEditor {

  private static final long serialVersionUID = 1L;
  private JComboBox<Refree> combobox;
  private ArrayList<Refree> refs;

  public RefreeSelectionEditor(ArrayList<Refree> refs) {
    this.combobox = new JComboBox<Refree>();
    this.refs = refs;
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    Object tmp = table.getValueAt(row, column);
    this.combobox.removeAllItems();
    this.combobox.addItem(null);
    boolean contains = false;
    for (Refree r : this.refs) {
      this.combobox.addItem(r);
      if (r == tmp)
        contains = true;
    }
    if (contains) {
      combobox.setSelectedItem(tmp);
      combobox.setVisible(true);
    }
    return combobox;
  }

  public Object getCellEditorValue() {
    return combobox.getSelectedItem();
  }

}
