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

public class StaffTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private StaffTabPanelPModel pm;
  private TablePopupMenu popup;
  private JTable staff;
  protected TableColumnModel tcm;
  protected JDialog owner;

  public StaffTabPanel(StaffTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        StaffTabPanel.this.pm.removePropertyChangeListener(StaffTabPanel.this);
        super.windowClosed(e);
      }
    });
    init();
  }

  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getStaffComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  private JComponent getStaffComponent() {

    TableModel tm = pm.getTableModel();
    staff = new JTable(tm);
    staff.getTableHeader().setReorderingAllowed(false);
    //staff.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    tcm = staff.getColumnModel();
    tm.addTableModelListener(this);
    staff.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    staff.setSelectionModel(pm.getSelectionModel());
    staff.setFillsViewportHeight(true);
    staff.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = staff.rowAtPoint(me.getPoint());
          if (row >= 0)
            showStaffDialog(pm.getStaffDModel(staff.convertRowIndexToModel(row)));
        }
      }

    });
    staff.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });

    JScrollPane spane = new JScrollPane(staff);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        pm.getAction(StaffTabPanelPModel.NEW_STAFF_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_PERSON),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = popup.getRow();
        if (row >= 0)
          showStaffDialog(pm.getStaffDModel(staff.convertRowIndexToModel(row)));
      }
    });
    popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        pm.getAction(StaffTabPanelPModel.DELETE_STAFF_ACTION));
    popup.add(deleteItem);
    staff.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    this.setColumnWidths();

    return spane;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(pm.getAction(StaffTabPanelPModel.NEW_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(StaffTabPanelPModel.DELETE_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(StaffTabPanelPModel.IMPORT_STAFF_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(StaffTabPanelPModel.EXPORT_STAFF_ACTION)));

    return hBox;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    this.setColumnWidths();
  }

  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = staff.getTableHeader().getFontMetrics(
            staff.getTableHeader().getFont());

        for (int i = 0; i < staff.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < staff.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(staff.getValueAt(r, i).toString()));
          }
          width = Math.max(150, width + 20);

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  public void showStaffDialog(StaffDialogPModel pdm) {
    final StaffDialog pDialog = new StaffDialog(owner, pdm, true);
    pDialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        pm.sortStaff();
        pDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });
  }

  class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        int row = staff.rowAtPoint(e.getPoint());

        popup.setRow(row);
        popup.setCol(staff.rowAtPoint(e.getPoint()));

        if (row < 0) {
          popup.getComponent(1).setVisible(false);
        }
        else {
          if (!staff.isCellSelected(row, 0))
            staff.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == StaffTabPanelPModel.PROPERTY_SHOW_STAFFDIALOG) {
      this.showStaffDialog((StaffDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
    }
  }
}
