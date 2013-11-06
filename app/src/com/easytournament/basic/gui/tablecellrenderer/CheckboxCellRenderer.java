/* CheckboxCellRenderer.java - Renderer to show a checkbox in a table cell
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.tablecellrenderer;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer to show a checkbox in a table cell
 * @author David Meier
 *
 */
public class CheckboxCellRenderer extends JCheckBox implements
    TableCellRenderer {

  private static final long serialVersionUID = -4472730152184144386L;

  /* (non-Javadoc)
   * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean arg2, boolean arg3, int arg4, int arg5) {
    this.setSelected(Boolean.TRUE.equals(value));
    return this;
  }
}
