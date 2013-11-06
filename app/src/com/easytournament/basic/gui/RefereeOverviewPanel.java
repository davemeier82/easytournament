package com.easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.easytournament.basic.model.RefreeOverviewPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;


public class RefereeOverviewPanel extends JPanel implements TableModelListener {

  private RefreeOverviewPModel ropm;
  private TablePopupMenu popup;
  private JTable refs;
  protected TableColumnModel tcm;

  public RefereeOverviewPanel(RefreeOverviewPModel pm) {
    this.ropm = pm;
    init();
  }

  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getGroupsComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  private JComponent getGroupsComponent() {

    TableModel tm = ropm.getTableModel();
    refs = new JTable(tm);
    //refs.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    tcm = refs.getColumnModel();
    tm.addTableModelListener(this);
    refs.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    refs.setSelectionModel(ropm.getSelectionModel());
    refs.setFillsViewportHeight(true);
    refs.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = refs.convertRowIndexToModel(refs.rowAtPoint(me.getPoint()));
          if(row >= 0)
            ropm.editRefree(row);
        }
      }

    });
    refs.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          ropm.deleteAction();
        }
      }

    });

    JScrollPane spane = new JScrollPane(refs);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        ropm.getAction(RefreeOverviewPModel.NEW_REFREE_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_REFEREE),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = refs.convertRowIndexToModel(popup.getRow());
        if (row >= 0)
          ropm.editRefree(row);
      }
    });
    popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        ropm.getAction(RefreeOverviewPModel.DELETE_REFREE_ACTION));
    popup.add(deleteItem);
    refs.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    this.setColumnWidths();
    return spane;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(ropm.getAction(RefreeOverviewPModel.NEW_REFREE_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(ropm
        .getAction(RefreeOverviewPModel.DELETE_REFREE_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(ropm
        .getAction(RefreeOverviewPModel.IMPORT_REFREE_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(ropm
        .getAction(RefreeOverviewPModel.EXPORT_REFREE_ACTION)));

    return hBox;
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
        int row = refs.rowAtPoint(e.getPoint());

        popup.setRow(row);
        popup.setCol(refs.rowAtPoint(e.getPoint()));

        if (row < 0) {
          popup.getComponent(1).setVisible(false);
        }
        else {
          if (!refs.isCellSelected(row, 0))
            refs.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    this.setColumnWidths();
  }

  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = refs.getTableHeader().getFontMetrics(
            refs.getTableHeader().getFont());

        for (int i = 0; i < refs.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < refs.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(refs.getValueAt(r, i).toString()));
          }
          width = Math.max(200, width + 20);

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }
}
