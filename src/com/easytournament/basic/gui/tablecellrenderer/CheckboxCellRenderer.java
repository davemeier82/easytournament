package com.easytournament.basic.gui.tablecellrenderer;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckboxCellRenderer extends JCheckBox implements
    TableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable arg0, Object value,
      boolean arg2, boolean arg3, int arg4, int arg5) {
    this.setSelected(Boolean.TRUE.equals(value));
    return this;
  }

}
