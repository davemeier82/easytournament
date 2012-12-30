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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.model.PlayersTabPanelPModel;
import com.easytournament.basic.model.dialog.PlayerDialogPModel;
import com.easytournament.basic.model.dialog.TeamDialogPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;

public class PlayersTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private PlayersTabPanelPModel pm;
  private TablePopupMenu popup;
  private JTable players;
  protected TableColumnModel tcm;
  protected JDialog owner;

  public PlayersTabPanel(PlayersTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        PlayersTabPanel.this.pm
            .removePropertyChangeListener(PlayersTabPanel.this);
        super.windowClosed(e);
      }
    });
    init();
  }

  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getGroupsComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  private JComponent getGroupsComponent() {
    TableModel tm = pm.getTableModel();
    players = new JTable(tm);
    //players.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    tcm = players.getColumnModel();
    tm.addTableModelListener(this);
    players.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    players.setSelectionModel(pm.getSelectionModel());
    players.setFillsViewportHeight(true);
    players.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = players.rowAtPoint(me.getPoint());
          if (row >= 0)
            showPlayerDialog(pm.getPlayerDModel(players.convertRowIndexToModel(row)));
        }
      }

    });
    players.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });
    DefaultTableCellRenderer sdtcr = null;
    if(Organizer.getInstance().isSubstance())        
      sdtcr = new SubstanceDefaultTableCellRenderer();
    else
      sdtcr = new DefaultTableCellRenderer();
    sdtcr.setHorizontalAlignment(SwingConstants.CENTER);
    tcm.getColumn(0).setCellRenderer(sdtcr);
    JScrollPane spane = new JScrollPane(players);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        pm.getAction(PlayersTabPanelPModel.NEW_PLAYER_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_PLAYER),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = popup.getRow();
        if (row >= 0) {
          showPlayerDialog(pm.getPlayerDModel(players.convertRowIndexToModel(row)));
        }
      }
    });
    popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        pm.getAction(PlayersTabPanelPModel.DELETE_PLAYER_ACTION));
    popup.add(deleteItem);
    players.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    this.setColumnWidths();

    return spane;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(pm.getAction(PlayersTabPanelPModel.NEW_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(PlayersTabPanelPModel.DELETE_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(PlayersTabPanelPModel.IMPORT_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm
        .getAction(PlayersTabPanelPModel.EXPORT_PLAYER_ACTION)));

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
        FontMetrics fm = players.getTableHeader().getFontMetrics(
            players.getTableHeader().getFont());

        for (int i = 0; i < players.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < players.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(players.getValueAt(r, i).toString()));
          }
          if (i == 0)
            width = Math.max(50, width + 20);
          else
            width = Math.max(150, width + 20);

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  public void showPlayerDialog(PlayerDialogPModel pdm) {
    final PlayerDialog pDialog = new PlayerDialog(owner, pdm, true);
    pDialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        pm.sortPlayers();
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
        int row = players.rowAtPoint(e.getPoint());

        popup.setRow(row);
        popup.setCol(players.rowAtPoint(e.getPoint()));

        if (row < 0) {
          popup.getComponent(1).setVisible(false);
        }
        else {
          if (!players.isCellSelected(row, 0))
            players.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == PlayersTabPanelPModel.PROPERTY_SHOW_PLAYERDIALOG) {
      this.showPlayerDialog((PlayerDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
    }
  }
}
