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

import com.easytournament.basic.model.StaffTabPanelPModel;
import com.easytournament.basic.model.dialog.StaffDialogPModel;
import com.easytournament.basic.model.dialog.TeamDialogPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;

/**
 * Panel to create and edit the staff of a team.
 * This panel is used as a tab in the team dialog.
 * @author David Meier
 *
 */
public class StaffTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = -8693229904382875038L;
  /**
   * The presentation model
   */
  protected StaffTabPanelPModel pm;
  /**
   * The popup menu for the context menu
   */
  protected TablePopupMenu popup;
  /**
   * The table that show the staff
   */
  protected JTable staff;
  /**
   * The table column model of the table
   */
  protected TableColumnModel tcm;
  /**
   * The owner of this dialog
   */
  protected JDialog owner;

  /**
   * @param pm The presentation model
   * @param owner The owner of this panel
   */
  public StaffTabPanel(StaffTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        StaffTabPanel.this.pm.removePropertyChangeListener(StaffTabPanel.this);
        super.windowClosed(e);
      }
    });
    init();
  }

  /**
   * Initializes the dialog
   */
  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getStaffComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  /**
   * Creates the table
   * @return The staff table
   */
  private JComponent getStaffComponent() {

    TableModel tm = this.pm.getTableModel();
    this.staff = new JTable(tm);
    this.staff.getTableHeader().setReorderingAllowed(false);
    //staff.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    this.tcm = this.staff.getColumnModel();
    tm.addTableModelListener(this);
    this.staff.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.staff.setSelectionModel(this.pm.getSelectionModel());
    this.staff.setFillsViewportHeight(true);
    this.staff.addMouseListener(new MouseAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = StaffTabPanel.this.staff.rowAtPoint(me.getPoint());
          if (row >= 0)
            showStaffDialog(StaffTabPanel.this.pm.getStaffDModel(
                StaffTabPanel.this.staff.convertRowIndexToModel(row)));
        }
      }

    });
    this.staff.addKeyListener(new KeyAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
       */
      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          StaffTabPanel.this.pm.deleteAction();
        }
      }
    });

    JScrollPane spane = new JScrollPane(this.staff);

    createAndAddContextMenu();
    this.staff.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    this.setColumnWidths();

    return spane;
  }

  /**
   * Creates and add the context menu
   */
  private void createAndAddContextMenu() {
    this.popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        this.pm.getAction(StaffTabPanelPModel.NEW_STAFF_ACTION));
    this.popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_PERSON),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      private static final long serialVersionUID = 3117407813433825270L;

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = StaffTabPanel.this.popup.getRow();
        if (row >= 0)
          showStaffDialog(StaffTabPanel.this.pm.getStaffDModel(
              StaffTabPanel.this.staff.convertRowIndexToModel(row)));
      }
    });
    this.popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        this.pm.getAction(StaffTabPanelPModel.DELETE_STAFF_ACTION));
    this.popup.add(deleteItem);
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(this.pm.getAction(StaffTabPanelPModel.NEW_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm.getAction(StaffTabPanelPModel.DELETE_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm.getAction(StaffTabPanelPModel.IMPORT_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm.getAction(StaffTabPanelPModel.EXPORT_STAFF_ACTION)));

    return hBox;
  }

  /* (non-Javadoc)
   * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
   */
  @Override
  public void tableChanged(TableModelEvent e) {
    this.setColumnWidths();
  }

  /**
   * Updates the table column widths according to the table elements
   */
  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      /* (non-Javadoc)
       * @see java.lang.Runnable#run()
       */
      @Override
      public void run() {
        FontMetrics fm = StaffTabPanel.this.staff.getTableHeader().getFontMetrics(
            StaffTabPanel.this.staff.getTableHeader().getFont());

        for (int i = 0; i < StaffTabPanel.this.staff.getColumnCount(); i++) {
          int width = fm.stringWidth((String)StaffTabPanel.this.tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < StaffTabPanel.this.staff.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(StaffTabPanel.this.staff.getValueAt(r, i).toString()));
          }
          width = Math.max(150, width + 20);

          StaffTabPanel.this.tcm.getColumn(i).setPreferredWidth(width);
          StaffTabPanel.this.tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  /**
   * Creates and shows the dialog to add and edit a person of the staff
   * @param pdm
   */
  public void showStaffDialog(StaffDialogPModel pdm) {
    final StaffDialog pDialog = new StaffDialog(this.owner, pdm, true);
    pDialog.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        StaffTabPanel.this.pm.sortStaff();
        pDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });
  }

  /**
   * The popup listener that shows the context menu
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
     * Shows the popup menu
     * @param e The mouse event
     */
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        int row = StaffTabPanel.this.staff.rowAtPoint(e.getPoint());

        StaffTabPanel.this.popup.setRow(row);
        StaffTabPanel.this.popup.setCol(StaffTabPanel.this.staff.rowAtPoint(e.getPoint()));

        if (row < 0) {
          StaffTabPanel.this.popup.getComponent(1).setVisible(false);
        }
        else {
          if (!StaffTabPanel.this.staff.isCellSelected(row, 0))
            StaffTabPanel.this.staff.setRowSelectionInterval(row, row);
          StaffTabPanel.this.popup.getComponent(1).setVisible(true);
        }

        StaffTabPanel.this.popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == StaffTabPanelPModel.PROPERTY_SHOW_STAFFDIALOG) {
      this.showStaffDialog((StaffDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
    }
  }
}
