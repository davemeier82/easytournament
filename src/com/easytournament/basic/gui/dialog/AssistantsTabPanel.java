/* AssistantsTabPanel.java - Tab in the refree window to add assistants
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.easytournament.basic.model.AssistantsTabPanelPModel;
import com.easytournament.basic.model.dialog.RefreeDialogPModel;
import com.easytournament.basic.model.dialog.TeamDialogPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;

/**
 * Tab in the refree window to add,remove or edit assistants
 * @author David Meier
 *
 */
public class AssistantsTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = -7542163391961008257L;
  /**
   * Presentation model of the assistants UI
   */
  private AssistantsTabPanelPModel pm;
  /**
   * Context menu to add/delete/edit assistants
   */
  private TablePopupMenu popup;
  /**
   * The table that shows all assistants
   */
  private JTable assistantsTable;
  /**
   * Table column model used to adjust the column widths
   */
  protected TableColumnModel tcm;
  /**
   * The dialog containing this panel
   */
  protected JDialog owner;

  /**
   * Constructor
   * @param pm The presentation model for this panel
   * @param owner The dialog containing this panel
   */
  public AssistantsTabPanel(AssistantsTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener when the windows is closed
        AssistantsTabPanel.this.getPresentationModel()
            .removePropertyChangeListener(AssistantsTabPanel.this);
        super.windowClosed(e);
      }
    });
    
    init();
  }

  /**
   * Initializes the panel
   */
  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getAssistantsComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  /**
   * Creates the assistants table
   * @return The assistants table
   */
  private JComponent getAssistantsComponent() {
    TableModel tm = this.pm.getTableModel();
    this.assistantsTable = new JTable(tm);
    this.assistantsTable.getTableHeader().setReorderingAllowed(false);
    //assis.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    this.tcm = this.assistantsTable.getColumnModel();
    tm.addTableModelListener(this);
    this.assistantsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.assistantsTable.setSelectionModel(this.pm.getSelectionModel());
    this.assistantsTable.setFillsViewportHeight(true);
    this.assistantsTable.addMouseListener(new MouseAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        // open the assistants dialog if the user doubl-clicks on a
        // row of the assistants table
        if (me.getClickCount() > 1) {
          JTable assiTable = getAssistantsTable();
          int row = assiTable.rowAtPoint(me.getPoint());
          if (row >= 0)
            showAssistDialog(getPresentationModel().getAssistantDModel(assiTable.convertRowIndexToModel(row)));
        }
      }
    });
    // add key listener to delete an assistant if the delete-button was pressed
    this.assistantsTable.addKeyListener(new KeyAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
       */
      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          getPresentationModel().deleteAction();
        }
      }
    });

    JScrollPane spane = new JScrollPane(this.assistantsTable);

    // create and attach the popup menu
    this.popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        this.pm.getAction(AssistantsTabPanelPModel.NEW_ASSI_ACTION));
    this.popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_ASSISTANT),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

        private static final long serialVersionUID = 8120380122345814090L;

      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = getPopup().getRow();
        if (row >= 0)
          showAssistDialog(getPresentationModel().getAssistantDModel(getAssistantsTable().convertRowIndexToModel(row)));
      }
    });
    this.popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        this.pm.getAction(AssistantsTabPanelPModel.DELETE_ASSI_ACTION));
    this.popup.add(deleteItem);
    this.assistantsTable.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());

    // update the column widths
    this.setColumnWidths();
    return spane;
  }

  /**
   * Creates the button component on the bottom of the tab
   * @return The button compenent
   */
  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(this.pm.getAction(AssistantsTabPanelPModel.NEW_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(AssistantsTabPanelPModel.DELETE_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(AssistantsTabPanelPModel.IMPORT_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(AssistantsTabPanelPModel.EXPORT_ASSI_ACTION)));

    return hBox;
  }

  /* (non-Javadoc)
   * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
   */
  @Override
  public void tableChanged(TableModelEvent e) {
    // update the column widths when the table changed
    this.setColumnWidths();
  }

  /**
   * Updates the column widths
   */
  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      /* (non-Javadoc)
       * @see java.lang.Runnable#run()
       */
      @Override
      public void run() {
        JTable assiTable = getAssistantsTable();
        FontMetrics fm = assiTable.getTableHeader().getFontMetrics(
            assiTable.getTableHeader().getFont());

        for (int i = 0; i < assiTable.getColumnCount(); i++) {
          int width = fm.stringWidth((String)AssistantsTabPanel.this.tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < assiTable.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(assiTable.getValueAt(r, i).toString()));
          }
          width = Math.max(150, width + 20);

          AssistantsTabPanel.this.tcm.getColumn(i).setPreferredWidth(width);
          AssistantsTabPanel.this.tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  /**
   * Creates and shows the assistant dialog
   * @param refreePModel
   */
  public void showAssistDialog(RefreeDialogPModel refreePModel) {
    final RefreeDialog pDialog = new RefreeDialog(this.owner, refreePModel, true, true);
    pDialog.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // sort the assistants and remove window listener
        // when the window is closed
        getPresentationModel().sortAssistants();
        pDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });
  }

  /**
   * The popup listener to show the context menu
   * @author David Meier
   *
   */
  class PopupListener extends MouseAdapter {
    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    /**
     * Shows the context menu if the right mouse
     * button is pressed
     * @param e
     */
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        JTable assiTable = getAssistantsTable();
        TablePopupMenu popupMenu = getPopup();
        int row = assiTable.rowAtPoint(e.getPoint());

        popupMenu.setRow(row);
        popupMenu.setCol(assiTable.rowAtPoint(e.getPoint()));

        if (row < 0) {
          // hide the "edit" menu entry since we are not on a row
          popupMenu.getComponent(1).setVisible(false);
        }
        else {
          if (!assiTable.isCellSelected(row, 0))
            assiTable.setRowSelectionInterval(row, row);
          popupMenu.getComponent(1).setVisible(true);
        }

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == AssistantsTabPanelPModel.PROPERTY_SHOW_ASSISTDIALOG) {
      this.showAssistDialog((RefreeDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      getPresentationModel().removePropertyChangeListener(this);
    }
  }

  /**
   * @return the presentation model
   */
  public AssistantsTabPanelPModel getPresentationModel() {
    return this.pm;
  }

  /**
   * @return the assistantsTable
   */
  public JTable getAssistantsTable() {
    return this.assistantsTable;
  }

  /**
   * @return the popup menu
   */
  public TablePopupMenu getPopup() {
    return this.popup;
  }
  
  
}
