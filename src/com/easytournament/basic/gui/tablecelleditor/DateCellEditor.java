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
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import com.toedter.calendar.JDateChooser;

public class DateCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

  JComponent editorComponent;
  int clickCountToStart = 1;
  EditorDelegate delegate;

  public DateCellEditor(final JDateChooser dateChooser) {
    editorComponent = dateChooser;
    delegate = new EditorDelegate() {
      public void setValue(Object value) {
        Date date = null;
        try{
          date = (Date) value;
        } catch (ClassCastException ex) {
          date = new Date();
        }
        dateChooser.setDate((date != null)? date : new Date());
      }

      public Object getCellEditorValue() {
        return dateChooser.getDate();
      }
    };
  }

  @Override
  public Object getCellEditorValue() {
    return delegate.getCellEditorValue();
  }

  @Override
  public Component getTreeCellEditorComponent(JTree tree, Object value,
      boolean isSelected, boolean expanded, boolean leaf, int row) {
    String stringValue = tree.convertValueToText(value, isSelected, expanded,
        leaf, row, false);

    delegate.setValue(stringValue);
    return editorComponent;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    delegate.setValue(value);
    return editorComponent;
  }
  
  //
//Protected EditorDelegate class
//

  /**
   * The protected <code>EditorDelegate</code> class.
   */
  protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

      /**  The value of this cell. */
      protected Object value;

     /**
      * Returns the value of this cell.
      * @return the value of this cell
      */
      public Object getCellEditorValue() {
          return value;
      }

     /**
      * Sets the value of this cell.
      * @param value the new value of this cell
      */
      public void setValue(Object value) {
          this.value = value;
      }

     /**
      * Returns true if <code>anEvent</code> is <b>not</b> a
      * <code>MouseEvent</code>.  Otherwise, it returns true
      * if the necessary number of clicks have occurred, and
      * returns false otherwise.
      *
      * @param   anEvent         the event
      * @return  true  if cell is ready for editing, false otherwise
      * @see #setClickCountToStart
      * @see #shouldSelectCell
      */
      public boolean isCellEditable(EventObject anEvent) {
          if (anEvent instanceof MouseEvent) {
              return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
          }
          return true;
      }

     /**
      * Returns true to indicate that the editing cell may
      * be selected.
      *
      * @param   anEvent         the event
      * @return  true
      * @see #isCellEditable
      */
      public boolean shouldSelectCell(EventObject anEvent) {
          return true;
      }

     /**
      * Returns true to indicate that editing has begun.
      *
      * @param anEvent          the event
      */
      public boolean startCellEditing(EventObject anEvent) {
          return true;
      }

     /**
      * Stops editing and
      * returns true to indicate that editing has stopped.
      * This method calls <code>fireEditingStopped</code>.
      *
      * @return  true
      */
      public boolean stopCellEditing() {
          fireEditingStopped();
          return true;
      }

     /**
      * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
      */
     public void cancelCellEditing() {
         fireEditingCanceled();
     }

     /**
      * When an action is performed, editing is ended.
      * @param e the action event
      * @see #stopCellEditing
      */
      public void actionPerformed(ActionEvent e) {
        DateCellEditor.this.stopCellEditing();
      }

     /**
      * When an item's state changes, editing is ended.
      * @param e the action event
      * @see #stopCellEditing
      */
      public void itemStateChanged(ItemEvent e) {
          DateCellEditor.this.stopCellEditing();
      }
  }
}
