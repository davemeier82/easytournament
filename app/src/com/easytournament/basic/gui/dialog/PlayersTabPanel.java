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

/**
 * Tab used in the Team Dialog to add/edit/remove players
 * @author David Meier
 *
 */
public class PlayersTabPanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = -598175856761748559L;
  /**
   * The presentation model
   */
  protected PlayersTabPanelPModel pm;
  /**
   * The context menu to add/edit/remove players
   */
  protected TablePopupMenu popup;
  /**
   * Table that lists the players
   */
  protected JTable playerTable;
  /**
   * The table column model
   */
  protected TableColumnModel tcm;
  /**
   * The dialog that contains this tab
   */
  protected JDialog owner;

  /**
   * Constructor
   * @param pm The presentation model
   * @param owner  The dialog that contains this tab
   */
  public PlayersTabPanel(PlayersTabPanelPModel pm, JDialog owner) {
    this.pm = pm;
    this.owner = owner;
    pm.addPropertyChangeListener(this);
    owner.addWindowListener(new WindowAdapter() {

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener when the dialog is closed
        PlayersTabPanel.this.pm
            .removePropertyChangeListener(PlayersTabPanel.this);
        super.windowClosed(e);
      }
    });
    init();
  }

  /**
   * initialize the panels of the tab
   */
  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getPlayerComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  /**
   * @return The component that contains the player table
   */
  private JComponent getPlayerComponent() {
    TableModel tm = this.pm.getTableModel();
    this.playerTable = new JTable(tm);
    // players.setAutoCreateRowSorter(true); //TODO enable if deleting is ok
    this.tcm = this.playerTable.getColumnModel();
    tm.addTableModelListener(this);
    this.playerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.playerTable.getTableHeader().setReorderingAllowed(false);
    this.playerTable.setSelectionModel(this.pm.getSelectionModel());
    this.playerTable.setFillsViewportHeight(true);
    this.playerTable.addMouseListener(new MouseAdapter() {

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          // double click on a row opens the player dialog to edit a player
          int row = PlayersTabPanel.this.playerTable.rowAtPoint(me.getPoint());
          if (row >= 0)
            showPlayerDialog(PlayersTabPanel.this.pm
                .getPlayerDModel(PlayersTabPanel.this.playerTable
                    .convertRowIndexToModel(row)));
        }
      }
    });
    
    // add listener to delete player with the delete-key
    this.playerTable.addKeyListener(new KeyAdapter() {

      /*
       * (non-Javadoc)
       * 
       * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
       */
      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          PlayersTabPanel.this.pm.deleteAction();
        }
      }
    });
    DefaultTableCellRenderer sdtcr = null;
    if (Organizer.getInstance().isSubstance())
      sdtcr = new SubstanceDefaultTableCellRenderer();
    else
      sdtcr = new DefaultTableCellRenderer();
    sdtcr.setHorizontalAlignment(SwingConstants.CENTER);
    this.tcm.getColumn(0).setCellRenderer(sdtcr);
    
    JScrollPane spane = new JScrollPane(this.playerTable);

    // the context menu
    this.popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        this.pm.getAction(PlayersTabPanelPModel.NEW_PLAYER_ACTION));
    this.popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_PLAYER),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      private static final long serialVersionUID = -6462438435319590701L;

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
       * )
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = PlayersTabPanel.this.popup.getRow();
        if (row >= 0) {
          showPlayerDialog(PlayersTabPanel.this.pm
              .getPlayerDModel(PlayersTabPanel.this.playerTable
                  .convertRowIndexToModel(row)));
        }
      }
    });
    this.popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        this.pm.getAction(PlayersTabPanelPModel.DELETE_PLAYER_ACTION));
    this.popup.add(deleteItem);
    this.playerTable.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    
    this.setColumnWidths();
    return spane;
  }

  /**
   * Component with various buttons to change the player table entries
   * @return the button component
   */
  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(this.pm
        .getAction(PlayersTabPanelPModel.NEW_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(PlayersTabPanelPModel.DELETE_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(PlayersTabPanelPModel.IMPORT_PLAYER_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(this.pm
        .getAction(PlayersTabPanelPModel.EXPORT_PLAYER_ACTION)));

    return hBox;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.
   * TableModelEvent)
   */
  @Override
  public void tableChanged(TableModelEvent e) {
    this.setColumnWidths();
  }

  /**
   * adjusts the column width if the table entries changed
   */
  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      /*
       * (non-Javadoc)
       * 
       * @see java.lang.Runnable#run()
       */
      @Override
      public void run() {
        JTable pTable = PlayersTabPanel.this.playerTable;
        FontMetrics fm = pTable.getTableHeader().getFontMetrics(
            pTable.getTableHeader().getFont());

        for (int i = 0; i < pTable.getColumnCount(); i++) {
          int width = fm.stringWidth((String)PlayersTabPanel.this.tcm
              .getColumn(i).getHeaderValue());
          for (int r = 0; r < pTable.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(pTable.getValueAt(r, i).toString()));
          }
          if (i == 0)
            width = Math.max(50, width + 20);
          else
            width = Math.max(150, width + 20);

          PlayersTabPanel.this.tcm.getColumn(i).setPreferredWidth(width);
          PlayersTabPanel.this.tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  /**
   * Creates and shows the dialog to create/edit a player
   * @param pdm The presentation model
   */
  public void showPlayerDialog(PlayerDialogPModel pdm) {
    final PlayerDialog pDialog = new PlayerDialog(this.owner, pdm, true);
    pDialog.addWindowListener(new WindowAdapter() {

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        PlayersTabPanel.this.pm.sortPlayers();
        pDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });
  }

  /**
   * Listener to show the context menu
   * @author David Meier
   * 
   */
  class PopupListener extends MouseAdapter {
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    /**
     * Shows the context menu if the right mouse button is pressed
     * on the player table
     * @param e The mouse event
     */
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        JTable pTable = PlayersTabPanel.this.playerTable;
        int row = pTable.rowAtPoint(e.getPoint());

        TablePopupMenu popupMenu = PlayersTabPanel.this.popup;
        popupMenu.setRow(row);
        popupMenu.setCol(pTable.rowAtPoint(e.getPoint()));

        // hide the "edit" entry if the right click was not 
        // on a player row
        if (row < 0) {
          popupMenu.getComponent(1).setVisible(false);
        }
        else {
          if (!pTable.isCellSelected(row, 0))
            pTable.setRowSelectionInterval(row, row);
          popupMenu.getComponent(1).setVisible(true);
        }

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == PlayersTabPanelPModel.PROPERTY_SHOW_PLAYERDIALOG) {
      this.showPlayerDialog((PlayerDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
    }
  }
}
