package com.easytournament.tournament.gui.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.easytournament.basic.resources.ResourceManager;

public class DateTableCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 100452192651676523L;
  
  private DateFormat dateFormatter = DateFormat.getDateInstance(
      DateFormat.SHORT, ResourceManager.getLocale());

  public DateTableCellRenderer() {
    super();
    setHorizontalAlignment(SwingConstants.CENTER);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    JLabel label = (JLabel)super.getTableCellRendererComponent(table, value,
        isSelected, hasFocus, row, column);

    if (value != null) {
      try {
        label.setText(dateFormatter.format((Date)value));
      }
      catch (ClassCastException ex) {
        // do nothing
      }
    }
    return this;
  }
}
