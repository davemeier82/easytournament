package easytournament.basic.model;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.comperator.RuleNameComperator;
import easytournament.basic.valueholder.Rule;
import easytournament.basic.valueholder.Tournament;

public class RulesPanelPModel extends Model implements ListDataListener {

  public static final int UP_ACTION = 0;
  public static final int DOWN_ACTION = 1;
  public static final int ADDTO_AVAILABLE_ACTION = 2;
  public static final int ADDTO_USED_ACTION = 3;
  public static final int AVAILABLE_LIST = 0;
  public static final int USED_LIST = 1;

  private DefaultListModel<Rule> availableRules = new DefaultListModel<Rule>();
  private DefaultListModel<Rule> usedRules = new DefaultListModel<Rule>();
  private ListSelectionModel availSelModel = new DefaultListSelectionModel();
  private ListSelectionModel usedSelModel = new DefaultListSelectionModel();
  private Tournament t = Organizer.getInstance().getCurrentTournament();
  private ArrayListModel<Rule> rules;

  public RulesPanelPModel() {
    rules = t.getDefaultRules();
    rules.addListDataListener(this);
    availSelModel
        .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    usedSelModel
        .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    retrieveData();
  }

  private void retrieveData() {
    this.usedRules.removeListDataListener(this);
    this.availableRules.removeListDataListener(this);

    this.usedRules.clear();
    this.availableRules.clear();

    for (Rule r : rules) {
      this.usedRules.addElement(r);
    }

    ArrayList<Rule> temp = new ArrayList<Rule>();
    try {
      for (Rule r : Rule.ruleMap.values()) {
        if (!rules.contains(r))
          temp.add((Rule)r.clone());
      }
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("RulesPanelPModel", "retrieveData", e);
      ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    Collections.sort(temp, new RuleNameComperator<Rule>());
    for (Rule s : temp) {
      this.availableRules.addElement(s);
    }
    this.usedRules.addListDataListener(this);
    this.availableRules.addListDataListener(this);

  }

  public Action getAction(int actionkey) {
    switch (actionkey) {
      case ADDTO_AVAILABLE_ACTION:
        return new ArrowAction(ResourceManager.getText(Text.REMOVE),
            ResourceManager.getIcon(Icon.RIGHT_ICON), actionkey);
      case ADDTO_USED_ACTION:
        return new ArrowAction(ResourceManager.getText(Text.ADD),
            ResourceManager.getIcon(Icon.LEFT_ICON), actionkey);
      case UP_ACTION:
        return new ArrowAction(ResourceManager.getText(Text.UP),
            ResourceManager.getIcon(Icon.UP_ICON), actionkey);
      case DOWN_ACTION:
        return new ArrowAction(ResourceManager.getText(Text.DOWN),
            ResourceManager.getIcon(Icon.DOWN_ICON), actionkey);
    }
    return null;
  }

  public ListModel<Rule> getListModel(int listkey) {
    switch (listkey) {
      case AVAILABLE_LIST:
        return this.availableRules;
      case USED_LIST:
        return this.usedRules;
    }
    return null;
  }

  public ListSelectionModel getListSelectionModel(int usedList) {
    switch (usedList) {
      case AVAILABLE_LIST:
        return this.availSelModel;
      case USED_LIST:
        return this.usedSelModel;
    }
    return null;
  }

  class ArrowAction extends AbstractAction {
    private int type;

    public ArrowAction(String text, ImageIcon i, int type) {
      this.putValue(Action.SHORT_DESCRIPTION, text);
      this.putValue(Action.SMALL_ICON, i);
      this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      t.getSport().setEdited(true);
      ArrayList<Point> intervals = null;
      if (this.type == ADDTO_USED_ACTION) {
        usedRules.removeListDataListener(RulesPanelPModel.this);
        availableRules.removeListDataListener(RulesPanelPModel.this);
        int max = availSelModel.getMaxSelectionIndex();
        int min = availSelModel.getMinSelectionIndex();
        if (min == -1)
          return;
        int usedmin = usedSelModel.getMinSelectionIndex() + 1;
        for (int i = max; i >= min; i--) {
          if (availSelModel.isSelectedIndex(i))
            usedRules.add(usedmin, availableRules.remove(i));
        }
        usedRules.addListDataListener(RulesPanelPModel.this);
        availableRules.addListDataListener(RulesPanelPModel.this);
      }
      else if (this.type == ADDTO_AVAILABLE_ACTION) {
        usedRules.removeListDataListener(RulesPanelPModel.this);
        availableRules.removeListDataListener(RulesPanelPModel.this);
        int max = usedSelModel.getMaxSelectionIndex();
        int min = usedSelModel.getMinSelectionIndex();
        if (min == -1)
          return;

        for (int i = max; i >= min; i--) {
          if (usedSelModel.isSelectedIndex(i))
            availableRules.addElement(usedRules.remove(i));
        }
        sortAvailableRules();
        usedRules.addListDataListener(RulesPanelPModel.this);
        availableRules.addListDataListener(RulesPanelPModel.this);
      }
      else if (this.type == UP_ACTION) {
        intervals = new ArrayList<Point>();
        int i = usedSelModel.getMinSelectionIndex();
        if (i >= 0) {
          int start = -1, end = 0, first = 0;
          for (; i <= usedSelModel.getMaxSelectionIndex(); i++) {
            if (usedSelModel.isSelectedIndex(i)) {
              if (i - 1 >= first) {
                usedRules.add(i - 1, usedRules.remove(i));
                if (start < 0)
                  start = i - 1;
                // set interval end
                end = i - 1;
                if (i - 1 == first) {
                  first++;
                }
              }
              else {
                if (start < 0)
                  start = i;
                // set interval end
                end = i;
                if (i == first) {
                  first++;
                }
              }

            }
            else if (start >= 0) {
              intervals.add(new Point(start, end));
              start = -1;
            }
          }
          intervals.add(new Point(start, end));
        }
      }
      else if (this.type == DOWN_ACTION) {
        intervals = new ArrayList<Point>();
        int i = usedSelModel.getMaxSelectionIndex();
        if (i >= 0) {
          int start = -1, end = 0, last = usedRules.getSize() - 1;
          for (; i >= usedSelModel.getMinSelectionIndex(); i--) {
            if (usedSelModel.isSelectedIndex(i)) {
              if (i + 1 <= last) {
                usedRules.add(i + 1, usedRules.remove(i));
                if (start < 0)
                  start = i + 1;
                // set interval end
                end = i + 1;
                if (i + 1 == last) {
                  last--;
                }
              }
              else {
                if (start < 0)
                  start = i;
                // set interval end
                end = i;
                if (i == last) {
                  last--;
                }
              }
            }
            else if (start >= 0) {
              intervals.add(new Point(start, end));
              start = -1;
            }
          }
          intervals.add(new Point(start, end));
        }

      }
      if (intervals != null) {
        for (int i = 0; i < intervals.size(); i++) {
          Point p = intervals.get(i);
          if (i == 0) {
            usedSelModel.setSelectionInterval(p.x, p.y);
          }
          else {
            usedSelModel.addSelectionInterval(p.x, p.y);
          }
        }
      }
    }
  }

  private void updateUsedRules() {
    this.rules.removeListDataListener(this);
    ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
    for (int i = 0; i < this.usedRules.getSize(); i++) {
      rules.add(this.usedRules.get(i));
    }
    t.setDefaultRules(rules);
    this.rules.addListDataListener(this);
  }

  private void sortAvailableRules() {
    this.availableRules.removeListDataListener(this);

    Rule[] rules = new Rule[availableRules.size()];
    for (int i = 0; i < availableRules.size(); i++) {
      rules[i] = availableRules.get(i);
    }
    Arrays.sort(rules, new RuleNameComperator<Rule>());
    for (int i = 0; i < availableRules.size(); i++) {
      availableRules.setElementAt(rules[i], i);
    }
    this.availableRules.addListDataListener(this);
  }

  public void changeOrdering(int list, int index) {
    switch (list) {
      case AVAILABLE_LIST: {
        Rule r = this.availableRules.get(index);
        r.setAscending(!r.isAscending());
        break;
      }
      case USED_LIST: {
        t.getSport().setEdited(true);
        Rule r = this.usedRules.get(index);
        r.setAscending(!r.isAscending());
        break;
      }
    }
  }

  public boolean isAscending(int list, int index) {
    if (index >= 0) {
      switch (list) {
        case AVAILABLE_LIST: {
          Rule r = this.availableRules.get(index);
          return r.isAscending();
        }
        case USED_LIST: {
          Rule r = this.usedRules.get(index);
          return r.isAscending();
        }
      }
    }
    return false;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    t.getSport().setEdited(true);
    if (e.getSource() == this.usedRules)
      updateUsedRules();
    else if (e.getSource() == this.availableRules) {
      sortAvailableRules();
    }
    else {
      retrieveData();
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    t.getSport().setEdited(true);
    if (e.getSource() == this.usedRules)
      updateUsedRules();
    else if (e.getSource() == this.availableRules) {
      sortAvailableRules();
    }
    else {
      retrieveData();
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    t.getSport().setEdited(true);
    if (e.getSource() == this.usedRules)
      updateUsedRules();
    else if (e.getSource() == this.availableRules) {
      sortAvailableRules();
    }
    else {
      retrieveData();
    }
  }
}
