package com.easytournament.designer.gui.editor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellEditor;

import com.easytournament.basic.Organizer;
import com.easytournament.designer.gui.renderer.PositionListCellRenderer;
import com.easytournament.designer.gui.renderer.SubstPositionListCellRenderer;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;

public class PositionSelectionEditor extends AbstractCellEditor implements
    TableCellEditor {

  private static final long serialVersionUID = 1L;
  
  public static final String PROPERTY_SHOW_TEAMS = "showTeams";

  private JComboBox<Position> combobox;
  private int serachCol;
  private boolean allteams = true;

  public PositionSelectionEditor(int col, ListCellRenderer<? super Position> renderer) {
    this.combobox = new JComboBox<Position>();
    this.serachCol = col;
    this.combobox.setRenderer(renderer);
    this.allteams = false;
  }

  public PositionSelectionEditor(ListCellRenderer<? super Position> renderer) {
    this.combobox = new JComboBox<Position>();
      this.combobox.setRenderer(renderer);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    Object tmp = table.getValueAt(row, column);
    combobox.removeAllItems();
    if (allteams) {
      boolean contains = false;
      for (AbstractGroup g : Organizer.getInstance().getCurrentTournament()
          .getPlan().getGroups()) {
        for (Position p : g.getPositions()) {
          combobox.addItem(p);
          if (p == tmp)
            contains = true;
        }
      }
      if (contains) {
        combobox.setSelectedItem(tmp);
        combobox.setVisible(true);
      }

    }
    else {
      Position pos = (Position)table.getValueAt(row, serachCol);
      if (pos != null) {
        boolean contains = false;
        for (Position p : pos.getGroup().getPositions()) {
          if (p != pos) {
            combobox.addItem(p);
            if(p == tmp)
              contains = true;
          }
        }

        if (contains) {
          combobox.setSelectedItem(tmp);
          combobox.setVisible(true);
        }
      }
    }
    return combobox;
  }

  public Object getCellEditorValue() {
    return combobox.getSelectedItem();
  }
}
