package com.easytournament.designer.gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.easytournament.designer.valueholder.DuellGroup;
import com.easytournament.designer.valueholder.Position;

public class PositionTableCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 100452192651676523L;

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    JLabel label = (JLabel)super.getTableCellRendererComponent(table, value,
        isSelected, hasFocus, row, column);

    if (value != null)
      try {
        DuellGroup dg = (DuellGroup)((Position)value).getGroup();
        int idx = dg.getPositions().indexOf(value);
        label.setText((idx + 1) + ". " + dg.getName());
      }
      catch (Exception e) {
        label.setText(((Position)value).getName());
      }

    return this;
  }

}
