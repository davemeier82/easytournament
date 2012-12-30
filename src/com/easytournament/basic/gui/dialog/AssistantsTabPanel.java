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

public class AssistantsTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private AssistantsTabPanelPModel pm;
  private TablePopupMenu popup;
  private JTable assis;
  protected TableColumnModel tcm;
  protected JDialog owner;

  public AssistantsTabPanel(AssistantsTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        AssistantsTabPanel.this.pm
            .removePropertyChangeListener(AssistantsTabPanel.this);
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
    assis = new JTable(tm);
    //assis.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    tcm = assis.getColumnModel();
    tm.addTableModelListener(this);
    assis.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    assis.setSelectionModel(pm.getSelectionModel());
    assis.setFillsViewportHeight(true);
    assis.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = assis.rowAtPoint(me.getPoint());
          if (row >= 0)
            showAssistDialog(pm.getAssistantDModel(assis.convertRowIndexToModel(row)));
        }
      }

    });
    assis.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });

    JScrollPane spane = new JScrollPane(assis);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        pm.getAction(AssistantsTabPanelPModel.NEW_ASSI_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_ASSISTANT),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = popup.getRow();
        if (row >= 0)
          showAssistDialog(pm.getAssistantDModel(assis.convertRowIndexToModel(row)));
      }
    });
    popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        pm.getAction(AssistantsTabPanelPModel.DELETE_ASSI_ACTION));
    popup.add(deleteItem);
    assis.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());

    this.setColumnWidths();

    return spane;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(pm.getAction(AssistantsTabPanelPModel.NEW_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(AssistantsTabPanelPModel.DELETE_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(AssistantsTabPanelPModel.IMPORT_ASSI_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(AssistantsTabPanelPModel.EXPORT_ASSI_ACTION)));

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
        FontMetrics fm = assis.getTableHeader().getFontMetrics(
            assis.getTableHeader().getFont());

        for (int i = 0; i < assis.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < assis.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(assis.getValueAt(r, i).toString()));
          }
          width = Math.max(150, width + 20);

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  public void showAssistDialog(RefreeDialogPModel pdm) {
    final RefreeDialog pDialog = new RefreeDialog(owner, pdm, true, true);
    pDialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        pm.sortAssistants();
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
        int row = assis.rowAtPoint(e.getPoint());

        popup.setRow(row);
        popup.setCol(assis.rowAtPoint(e.getPoint()));

        if (row < 0) {
          popup.getComponent(1).setVisible(false);
        }
        else {
          if (!assis.isCellSelected(row, 0))
            assis.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == AssistantsTabPanelPModel.PROPERTY_SHOW_ASSISTDIALOG) {
      this.showAssistDialog((RefreeDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
    }

  }
}
