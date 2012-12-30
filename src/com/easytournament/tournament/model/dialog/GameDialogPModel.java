package com.easytournament.tournament.model.dialog;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;



import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.comperator.GEventEntryTimeComperator;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.calc.Calculator;
import com.easytournament.tournament.csv.GameReportCSVHandler;
import com.easytournament.tournament.html.GameReportHTMLHandler;
import com.easytournament.tournament.model.tablemodel.EventsTableModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;


public class GameDialogPModel extends Model implements ListDataListener,
    PropertyChangeListener {

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  public static final int ADD_ACTION = 2;
  public static final int EDIT_ACTION = 3;
  public static final int DELETE_ACTION = 4;
  public static final int EXPORT_ACTION = 5;
  public static final int PRINT_ACTION = 6;
  public static final String DISPOSE = "dispose";
  public static final String SHOW_EVENTDIALOG = "showEventDialog";
  public static final String ENABLE_RESULTS = "resultsEabled";
  public static final String PRINT = "print";

  protected ScheduleEntry entry;
  protected AbstractGroup group;
  protected SportSettings settings;
  protected ArrayList<PointClass> points = new ArrayList<PointClass>();
  protected EventsTableModel tableModel;
  protected DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();

  protected GEventEntryTimeComperator<GameEventEntry> comp = new GEventEntryTimeComperator<GameEventEntry>();
  protected ArrayListModel<GameEventEntry> data = new ArrayListModel<GameEventEntry>();

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  public GameDialogPModel(ScheduleEntry entry) {
    this.entry = entry;
    this.group = entry.getGroupAssignedTo();
    this.init();
  }

  private void init() {

    this.data.addAll(this.entry.getGameEvents());

    this.tableModel = new EventsTableModel(data, entry);
    if (this.group.isDefaultSettings()) {
      settings = t.getSettings();
    }
    else {
      settings = this.group.getSettings();
    }

    int nResults = settings.getNumGameTimes() + settings.getNumOvertimes() + 2;
    ArrayList<Integer> results = this.entry.getResults();
    ArrayList<Boolean> checked = this.entry.getChecked();
    if (results.size() < nResults * 2) {
      int idx = Math.max(0, results.size() - 3);
      while (results.size() < nResults * 2) {
        results.add(idx, new Integer(0));
        checked.add(false);
      }
    }
    else if (results.size() > nResults * 2) {
      while (results.size() > nResults * 2) {
        results.remove(Math.max(0, results.size() - 3));
        checked.remove(Math.max(0, results.size() - 3));
      }
    }

    for (int i = 0; i < nResults - 1; i++) {
      PointClass pc1 = new PointClass(results.get(2 * i), checked.get(2 * i));
      PointClass pc2 = new PointClass(results.get(2 * i + 1), checked.get(2 * i +1));
      this.points.add(pc1);
      this.points.add(pc2);
      pc1.addPropertyChangeListener(this);
      pc2.addPropertyChangeListener(this);
    }
    // result fields
    this.points.add(new PointClass(entry.getHomeScore(), true));
    this.points.add(new PointClass(entry.getAwayScore(), true));

    data.addListDataListener(this);
    update();
  }

  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            entry.setGameEvents(GameDialogPModel.this.data);
            ArrayList<Integer> results = entry.getResults();
            ArrayList<Boolean> checked = entry.getChecked();
            for (int i = 0; i < points.size(); i++) {
              results.set(i, points.get(i).getPoints());
              checked.set(i, points.get(i).isChecked());
            }
            entry.setGamePlayed(true);
            Calculator.calcTableEntries(group, true);      
            Organizer.getInstance().setSaved(false);
            GameDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            GameDialogPModel.this.data.clear(); // recalculate points
            GameDialogPModel.this.data.addAll(GameDialogPModel.this.entry
                .getGameEvents());
            GameDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case ADD_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.ADD),
            ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            GameDialogPModel.this.firePropertyChange(SHOW_EVENTDIALOG, null,
                new EventDialogPModel(entry, data));
          }
        };
      case EDIT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EDIT),
            ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            int idx = selectionModel.getMinSelectionIndex();
            if (idx >= 0) {
              GameDialogPModel.this.firePropertyChange(SHOW_EVENTDIALOG, null,
                  new EventDialogPModel(entry, data, data.get(idx)));
            }
          }
        };
      case DELETE_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.DELETE),
            ResourceManager.getIcon(Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            deleteAction();
          }
        };
      case PRINT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.PRINT_MENU),
            ResourceManager.getIcon(Icon.PRINT_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            GameDialogPModel.this.firePropertyChange(PRINT, 0, 1);
          }
        };
      case EXPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EXPORT_MENU)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            exportAction();
          }
        };
    }
    return null;
  }

  public int getNumGameTimes() {
    return this.settings.getNumGameTimes();
  }

  public int getNumOvertimes() {
    return this.settings.getNumOvertimes();
  }

  public ValueModel getScoreModel(int i, boolean hometeam) {
    int idx = 2 * i;
    if (!hometeam)
      idx++;
    return new PropertyAdapter<PointClass>(this.points.get(idx), "points", true);
  }

  public ValueModel getCheckedModel(int i) {
    int idx = 2 * i;
    return new PropertyAdapter<PointClass>(this.points.get(idx), "checked", true);
  }


  protected class PointClass extends Model {
    private int points;
    private boolean checked;

    public PointClass(int points, boolean checked) {
      this.points = points;
      this.checked = checked;
    }

    public int getPoints() {
      return points;
    }

    public void setPoints(int points) {
      int old = this.points;
      this.points = points;
      this.firePropertyChange("points", old, this.points);
    }

    public boolean isChecked() {
      return checked;
    }

    public void setChecked(boolean checked) {
      boolean old = this.checked;
      this.checked = checked;
      this.firePropertyChange("checked", old, this.checked);
    }

  }

  public TableModel getTableModel() {
    return this.tableModel;
  }

  public void deleteAction() {
    int min = selectionModel.getMinSelectionIndex();
    if (min < 0)
      return;
    int max = selectionModel.getMaxSelectionIndex();
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for (int i = min; i <= max; i++) {
      if (selectionModel.isSelectedIndex(i))
        indices.add(i);
    }
    for (int i = indices.size() - 1; i >= 0; i--) {
      this.data.remove(indices.get(i).intValue());
    }
  }

  public String getTeamName(boolean home) {
    if (home)
      return this.entry.getHomeTeam().getName();
    return this.entry.getAwayTeam().getName();
  }

  public EventDialogPModel getEventDialogPModel(int row) {
    if (row < 0 || row >= data.getSize())
      return null;
    return new EventDialogPModel(entry, data, data.get(row));
  }

  public ListSelectionModel getTableSelectionModel() {
    return selectionModel;
  }

  private void update() {

    boolean enable = data.size() < 1;
    this.firePropertyChange(ENABLE_RESULTS, !enable, enable);
    if (enable) {
      sumFields();
      return;
    }
    data.removeListDataListener(this);

    Collections.sort(data, comp);
    int homePts = 0, tmpHome;
    int awayPts = 0, tmpAway;
    int nGameTimes = t.getSettings().getNumGameTimes();
    int nOvertimes = t.getSettings().getNumOvertimes();
    int time = t.getSettings().getMinPerGameTime();
    this.points.get(0).setPoints(0);
    this.points.get(1).setPoints(0);
    int i = 0;
    boolean readPenalties = false;

    for (GameEventEntry e : data) {

      if (e.getTeam().equals(entry.getHomeTeam())) {
        tmpHome = e.getEvent().getPointsForTeam();
        tmpAway = e.getEvent().getPointsForOpponent();
      }
      else {
        tmpHome = e.getEvent().getPointsForOpponent();
        tmpAway = e.getEvent().getPointsForTeam();
      }
      homePts += tmpHome;
      awayPts += tmpAway;
      e.setSummedHomePoints(homePts);
      e.setSummedAwayPoints(awayPts);

      if (e.getTimeMin() * 60 + e.getTimeSec() <= time * 60 || readPenalties) {
        PointClass pc = this.points.get(2 * i);
        pc.setPoints(pc.getPoints() + tmpHome);
        pc = this.points.get(2 * i + 1);
        pc.setPoints(pc.getPoints() + tmpAway);
      }
      else {
        do {
          i++;
          if (i < nGameTimes) {
            time += t.getSettings().getMinPerGameTime();
          }
          else if (i < nGameTimes + nOvertimes) {
            time += t.getSettings().getMinPerOvertime();
            points.get(2 * i).setChecked(true);
            points.get(2 * i + 1).setChecked(true);
          }
          else {
            readPenalties = true;
            points.get(2 * i).setChecked(true);
            points.get(2 * i + 1).setChecked(true);
            break;
          }
          this.points.get(2 * i).setPoints(0);
          this.points.get(2 * i + 1).setPoints(0);
        } while (e.getTimeMin() * 60 + e.getTimeSec() > time * 60 && !readPenalties);
        this.points.get(2 * i).setPoints(tmpHome);
        this.points.get(2 * i + 1).setPoints(tmpAway);
      }
    }
    for (i = 2 * i + 2; i < points.size() - 2; i++) {
      this.points.get(i).setPoints(0);
      points.get(i).setChecked(false);
    }

    if (data.size() > 0) {
      GameEventEntry lastEntry = data.get(data.size() - 1);
      this.points.get(this.points.size() - 2).setPoints(
          lastEntry.getSummedHomePoints());
      this.points.get(this.points.size() - 1).setPoints(
          lastEntry.getSummedAwayPoints());
    }

    data.addListDataListener(this);
    tableModel.fireTableDataChanged();
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    update();
    tableModel.fireTableDataChanged();
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    update();
    tableModel.fireTableDataChanged();
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    update();
    tableModel.fireTableDataChanged();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (data.size() == 0 && evt.getPropertyName().equals("points")) {
      sumFields();
    }

  }

  private void sumFields() {
    int homeSum = 0;
    int awaySum = 0;
    for (int i = 0; i < points.size() - 2;) {
      homeSum += points.get(i++).getPoints();
      awaySum += points.get(i++).getPoints();
    }
    points.get(points.size() - 2).setPoints(homeSum);
    points.get(points.size() - 1).setPoints(awaySum);
  }

  public int getGameEvents() {
    return this.data.size();
  }

  public void exportAction() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
      }

      public String getDescription() {
        return "CSV (*.csv)";
      }
    });
    FileFilter htmlfilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "HTML (*.html)";
      }

      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".html") || f.isDirectory();
      }
    };
    chooser.addChoosableFileFilter(htmlfilter);
    int answer = chooser.showSaveDialog(Organizer.getInstance().getMainFrame());
    if (answer == JFileChooser.APPROVE_OPTION) {
      File filename = chooser.getSelectedFile();

      if (filename != null) {
        try {
          if (chooser.getFileFilter().equals(htmlfilter)) {
            if (!filename.getPath().toLowerCase().endsWith(".html")) {
              filename = new File(filename.getPath() + ".html");
            }
            GameReportHTMLHandler.saveGameReport(filename, data, entry);
          }
          else {
            if (!filename.getPath().toLowerCase().endsWith(".csv")) {
              filename = new File(filename.getPath() + ".csv");
            }

            GameReportCSVHandler.saveGameReport(filename, data, entry);

          }
        }
        catch (FileNotFoundException e1) {
          JOptionPane.showInternalMessageDialog(Organizer.getInstance()
              .getMainFrame(), ResourceManager
              .getText(Text.COULD_NOT_SAVE_GAME), ResourceManager
              .getText(Text.ERROR), JOptionPane.ERROR_MESSAGE);
        }

      }
    }
  }
}
