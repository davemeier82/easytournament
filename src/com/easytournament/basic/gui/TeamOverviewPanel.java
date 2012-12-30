package com.easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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

import com.easytournament.basic.model.TeamOverviewPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;


public class TeamOverviewPanel extends JPanel {

  private static final long serialVersionUID = -6905875957539995275L;
  private TeamOverviewPModel topm;
  private TablePopupMenu popup;
  private JTable teams;

  public TeamOverviewPanel(TeamOverviewPModel pm) {
    this.topm = pm;
    init();
  }

  private void init() {
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getTeamssComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  private JComponent getTeamssComponent() {

    teams = new JTable(topm.getTableModel());
    //teams.setAutoCreateRowSorter(true); // TODO enable if deleting is ok
    teams.setSelectionModel(topm.getSelectionModel());
    teams.setFillsViewportHeight(true);
    teams.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        if (me.getClickCount() > 1) {
          int row = teams.convertRowIndexToModel(teams.rowAtPoint(me.getPoint()));
          if(row >= 0)
            topm.editTeam(row);
        }
      }

    });
    teams.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          topm.deleteSelectedTeams();
        }
      }

    });

    JScrollPane spane = new JScrollPane(teams);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        topm.getAction(TeamOverviewPModel.NEW_TEAM_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(new AbstractAction(
        ResourceManager.getText(Text.EDIT_TEAM),
        ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        int row = popup.getRow();
        if (row >= 0)
          topm.editTeam(teams.convertRowIndexToModel(row));
      }
    });
    popup.add(editItem);
    JMenuItem deleteItem = new JMenuItem(
        topm.getAction(TeamOverviewPModel.DELETE_TEAM_ACTION));
    popup.add(deleteItem);
    teams.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());

    return spane;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(topm.getAction(TeamOverviewPModel.NEW_TEAM_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(topm.getAction(TeamOverviewPModel.DELETE_TEAM_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(topm.getAction(TeamOverviewPModel.IMPORT_TEAM_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(topm.getAction(TeamOverviewPModel.EXPORT_TEAM_ACTION)));

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
        int row = teams.rowAtPoint(e.getPoint());

        popup.setRow(row);
        popup.setCol(teams.rowAtPoint(e.getPoint()));

        if (row < 0) {
          popup.getComponent(1).setVisible(false);
        }
        else {
          if (!teams.isCellSelected(row, 0))
            teams.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

}
