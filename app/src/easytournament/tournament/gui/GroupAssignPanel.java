package easytournament.tournament.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import easytournament.basic.Organizer;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.Position;
import easytournament.tournament.calc.Calculator;
import easytournament.tournament.helper.TeamTransferHandler;
import easytournament.tournament.model.listmodel.TeamListModel;

public class GroupAssignPanel extends JPanel implements ListDataListener {

  private static final long serialVersionUID = -8346523810274837968L;
  private TeamListModel teamTModel;
  private Tournament t;
  private JComponent center;
  private ArrayList<AbstractGroup> groups = new ArrayList<AbstractGroup>();
  private Box hBox;

  private boolean save = false;

  public GroupAssignPanel() {
    this.t = Organizer.getInstance().getCurrentTournament();
    init();
  }

  public void init() {
    this.removeAll();
    teamTModel = new TeamListModel(t.getUnassignedteams(), true);
    final JList<String> teams = new JList<String>(teamTModel);

    teams.setDragEnabled(true);
    teams.setDropMode(DropMode.INSERT);
    teams.setTransferHandler(new TeamTransferHandler());
    JScrollPane pane = new JScrollPane(teams);
    pane.setPreferredSize(new Dimension(400, -1));
    hBox = Box.createHorizontalBox();
    hBox.add(pane);
    center = getGroupsComponent();
    JScrollPane centerpane = new JScrollPane(center);
    centerpane.setOpaque(false);
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(centerpane, BorderLayout.CENTER);
    hBox.add(Box.createHorizontalGlue());
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());
    this.add(hBox, BorderLayout.WEST);
  }

  private JComponent getGroupsComponent() {
    Box gBox = Box.createVerticalBox();
    int minHeight = 0;
    for (AbstractGroup g : t.getPlan().getStartGroups()) {
      groups.add(g);
      JLabel l = new JLabel(g.getName());
      l.setFont(l.getFont().deriveFont(Font.BOLD));
      l.setAlignmentX(CENTER_ALIGNMENT);
      gBox.add(l);
      TeamListModel ttm = new TeamListModel(g.getTeams(), false);
      ttm.addListDataListener(this);
      final JList<String> temp = new JList<String>(ttm);
      temp.setDragEnabled(true);
      temp.setDropMode(DropMode.INSERT);
      temp.setTransferHandler(new TeamTransferHandler(g));
      temp.setFixedCellHeight(25);
      JScrollPane pane = new JScrollPane(temp);
      Dimension dim = new Dimension(400, 25 * g.getNumStartPos() + 2);

      pane.setPreferredSize(dim);
      pane.setMinimumSize(dim);
      pane.setMaximumSize(dim);
      gBox.add(pane);
      gBox.add(Box.createVerticalStrut(10));
      minHeight += dim.getHeight() + 10;
    }

    return gBox;
  }

  public void save() {
    if (save) {
      for (AbstractGroup g : groups) {
        ArrayList<Position> spos = g.getStartPos();
        ArrayList<Team> teams = g.getTeams();
        int j = 0;
        for (int i = 0; i < spos.size() && j < teams.size(); i++) {
          Position p = spos.get(i);
          if (p.getPrev() == null)
            p.setTeamAssignment(teams.get(j++));
        }
      }
      for (Team tm : t.getUnassignedteams()) {
        tm.setPositionAssignedTo(null);
      }
      for (AbstractGroup g : Organizer.getInstance().getCurrentTournament()
          .getPlan().getOrderedGroups()) {
        Calculator.calcTableEntries(g, false);
      }
      save = false;
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    Organizer.getInstance().setSaved(false);
    this.save = true;
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    Organizer.getInstance().setSaved(false);
    this.save = true;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    Organizer.getInstance().setSaved(false);
    this.save = true;
  }

}
