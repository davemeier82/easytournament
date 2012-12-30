package com.easytournament.tournament.gui;

import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
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
import com.easytournament.basic.gui.tablecellrenderer.CheckboxCellRenderer;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.designer.gui.editor.RefreeSelectionEditor;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.tournament.gui.dialog.GameDialog;
import com.easytournament.tournament.gui.renderer.PositionTableCellRenderer;
import com.easytournament.tournament.gui.renderer.SubstPositionTableCellRenderer;
import com.easytournament.tournament.model.GamesPanelPModel;
import com.easytournament.tournament.model.dialog.GameDialogPModel;

public class GamesPanel extends JPanel implements TableModelListener {

  private GamesPanelPModel pm;
  private TablePopupMenu popup;
  private JTable gamesTable;
  private TableColumnModel tcm;

  public GamesPanel(GamesPanelPModel pm) {
    this.pm = pm;
    init();
  }

  private void init() {
    this.removeAll();
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());
    this.add(getGroupsComponent(), BorderLayout.CENTER);
  }

  private JComponent getGroupsComponent() {
    Box gBox = Box.createVerticalBox();

    TableModel tm = pm.getTableModel();
    tm.addTableModelListener(this);
    gamesTable = new JTable(tm);
    gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    gamesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    gamesTable.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        int row = gamesTable.rowAtPoint(e.getPoint());
        if (e.getClickCount() > 1 && pm.isGameEditable(row)) {
          GameDialogPModel gdpm = pm.getGameDialogPModel(row);
          if (gdpm != null)
            new GameDialog(Organizer.getInstance().getMainFrame(), true, gdpm);
        }
        else if (gamesTable.columnAtPoint(e.getPoint()) == 0) {
          pm.togglePlayed(row);
        }
      }

    });
    DefaultTableCellRenderer dtcr = null;
    if (Organizer.getInstance().isSubstance()) {
      dtcr = new SubstanceDefaultTableCellRenderer();
      gamesTable.setDefaultRenderer(Position.class,
          new SubstPositionTableCellRenderer());
    }
    else {
      dtcr = new DefaultTableCellRenderer();
      gamesTable.setDefaultRenderer(Position.class,
          new PositionTableCellRenderer());
    }
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    tcm = gamesTable.getColumnModel();
    gamesTable.setDefaultRenderer(Boolean.class, new CheckboxCellRenderer());
    gamesTable.setDefaultEditor(Refree.class,
        new RefreeSelectionEditor(pm.getRefrees()));
    tcm.getColumn(0).setMaxWidth(30);
    tcm.getColumn(2).setCellRenderer(dtcr);
    tcm.getColumn(3).setCellRenderer(dtcr);
    tcm.getColumn(6).setCellRenderer(dtcr);
    tcm.getColumn(7).setCellRenderer(dtcr);

    JScrollPane pane = new JScrollPane(gamesTable);
    gBox.add(pane);

    popup = new TablePopupMenu();
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_GAME),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = popup.getRow();
        if (row >= 0 && pm.isGameEditable(row)) {
          GameDialogPModel gdpm = pm.getGameDialogPModel(row);
          if (gdpm != null)
            new GameDialog(Organizer.getInstance().getMainFrame(), true, gdpm);
        }
      }
    });
    popup.add(editItem);
    gamesTable.addMouseListener(new PopupListener());

    return gBox;
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
        int row = gamesTable.rowAtPoint(e.getPoint());
        popup.setRow(row);
        popup.setCol(gamesTable.rowAtPoint(e.getPoint()));

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = gamesTable.getTableHeader().getFontMetrics(
            gamesTable.getTableHeader().getFont());
        for (int i = 0; i < gamesTable.getColumnCount(); i++) {
          if(gamesTable.getColumnClass(i) == Boolean.class)
            continue;

          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < gamesTable.getRowCount(); r++) {
            Object value = gamesTable.getValueAt(r, i);
            if (value != null)
            {
              try {
                Position pos = (Position) value;
                if(pos.getTeam() == null)
                  width = Math.max(width, fm.stringWidth(value.toString())); 
                else
                  width = Math.max(width, fm.stringWidth(pos.getTeam().getName()));                 
              } catch (Exception ex)
              {
                width = Math.max(width, fm.stringWidth(value.toString())); 
              }             
            }
          }
          width = Math.max(50, width + 20);
          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
  }

  public JTable getTable() {
    return gamesTable;
  }
}
