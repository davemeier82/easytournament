/* DateCellEditor.java - Table cell editor to modify dates
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.tablecelleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import com.toedter.calendar.JDateChooser;

/**
 * Table cell editor to modify dates
 * @author David Meier
 *
 */
public class DateCellEditor extends AbstractCellEditor implements
    TableCellEditor, TreeCellEditor {

  private static final long serialVersionUID = -4382713247370231464L;
  /**
   * The date chooser object
   */
  protected JDateChooser dateChooser;
  /**
   * The delegate to handle the cell events
   */
  protected EditorDelegate delegate;

  /**
   * @param dateChooser
   */
  public DateCellEditor(final JDateChooser dateChooser) {
    this.dateChooser = dateChooser;
    this.delegate = new EditorDelegate() {

      private static final long serialVersionUID = 8367738397322658927L;

      /* (non-Javadoc)
       * @see com.easytournament.basic.gui.tablecelleditor.DateCellEditor.EditorDelegate#setValue(java.lang.Object)
       */
      @Override
      public void setValue(Object value) {
        Date date = null;
        try {
          date = (Date)value;
        }
        catch (ClassCastException ex) {
          date = new Date();
        }
        dateChooser.setDate((date != null)? date : new Date());
      }

      /* (non-Javadoc)
       * @see com.easytournament.basic.gui.tablecelleditor.DateCellEditor.EditorDelegate#getCellEditorValue()
       */
      @Override
      public Object getCellEditorValue() {
        return dateChooser.getDate();
      }
    };
  }

  /* (non-Javadoc)
   * @see javax.swing.CellEditor#getCellEditorValue()
   */
  @Override
  public Object getCellEditorValue() {
    return this.dateChooser.getDate();
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
   */
  @Override
  public Component getTreeCellEditorComponent(JTree tree, Object value,
      boolean isSelected, boolean expanded, boolean leaf, int row) {
    String stringValue = tree.convertValueToText(value, isSelected, expanded,
        leaf, row, false);

    this.delegate.setValue(stringValue);
    return this.dateChooser;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    this.delegate.setValue(value);
    return this.dateChooser;
  }

  //
  // Protected EditorDelegate class
  //

  /**
   * The protected <code>EditorDelegate</code> class.
   */
  protected class EditorDelegate implements ActionListener, ItemListener,
      Serializable {

    private static final long serialVersionUID = 4098557386622130308L;
    /** The value of this cell. */
    protected Object value;

    /**
     * Returns the value of this cell.
     * @return the value of this cell
     */
    public Object getCellEditorValue() {
      return this.value;
    }

    /**
     * Sets the value of this cell.
     * @param value
     *          the new value of this cell
     */
    public void setValue(Object value) {
      this.value = value;
    }

    /**
     * Returns true if <code>anEvent</code> is <b>not</b> a
     * <code>MouseEvent</code>. Otherwise, it returns true if the necessary
     * number of clicks have occurred, and returns false otherwise.
     * 
     * @param anEvent
     *          the event
     * @return true if cell is ready for editing, false otherwise
     * @see #setClickCountToStart
     * @see #shouldSelectCell
     */
    public boolean isCellEditable(EventObject anEvent) {
      if (anEvent instanceof MouseEvent) {
        return ((MouseEvent)anEvent).getClickCount() >= 1;
      }
      return true;
    }

    /**
     * Returns true to indicate that the editing cell may be selected.
     * 
     * @param anEvent
     *          the event
     * @return true
     * @see #isCellEditable
     */
    public boolean shouldSelectCell(EventObject anEvent) {
      return true;
    }

    /**
     * Returns true to indicate that editing has begun.
     * 
     * @param anEvent
     *          the event
     */
    public boolean startCellEditing(EventObject anEvent) {
      return true;
    }

    /**
     * Stops editing and returns true to indicate that editing has stopped. This
     * method calls <code>fireEditingStopped</code>.
     * 
     * @return true
     */
    @SuppressWarnings("synthetic-access")
    public boolean stopCellEditing() {
      fireEditingStopped();
      return true;
    }

    /**
     * Cancels editing. This method calls <code>fireEditingCanceled</code>.
     */
    @SuppressWarnings("synthetic-access")
    public void cancelCellEditing() {
      fireEditingCanceled();
    }

    /**
     * When an action is performed, editing is ended.
     * @param e
     *          the action event
     * @see #stopCellEditing
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      DateCellEditor.this.stopCellEditing();
    }

    /**
     * When an item's state changes, editing is ended.
     * @param e
     *          the action event
     * @see #stopCellEditing
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
      DateCellEditor.this.stopCellEditing();
    }
  }
}
