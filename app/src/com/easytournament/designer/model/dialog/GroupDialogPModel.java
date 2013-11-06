package com.easytournament.designer.model.dialog;

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


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.comperator.RuleNameComperator;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Group;
import com.easytournament.designer.valueholder.GroupType;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;


public class GroupDialogPModel extends Model implements ListDataListener {

  public static final int UP_ACTION = 0;
  public static final int DOWN_ACTION = 1;
  public static final int ADDTO_AVAILABLE_ACTION = 2;
  public static final int ADDTO_USED_ACTION = 3;
  public static final int OK_ACTION = 4;
  public static final int CANCEL_ACTION = 5;
  public static final int IMPORT_RULES_ACTION = 6;
  public static final int IMPORT_TSET_ACTION = 7;
  public static final int AVAILABLE_LIST = 0;
  public static final int USED_LIST = 1;
  public static final int IMPORT_LIST = 2;
  public static final Integer GROUP = new Integer(0);
  public static final Integer TYPE = new Integer(1);
  public static final Integer TOURNAMENT = new Integer(2);
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_NUM_POS = "numpos";
  public static final String PROPERTY_EDIT = "edit";
  public static final String PROPERTY_AVAIL_RULES = "availableRules";
  public static final String PROPERTY_USED_RULES = "usedRules";
  public static final String PROPERTY_IMPORTGROUP = "importGroup";
  public static final String PROPERTY_DISPOSE = "dispose";
  public static final String PROPERTY_DEFAULT_SET = "defaultSettings";
  public static final String PROPERTY_DEFAULT_RULES = "defaultRules";
  public static final String PROPERTY_TSETTINGS = "settings";

  private String name;
  private int numpos;
  private AbstractGroup group;
  private DefaultListModel<Rule> availableRules = new DefaultListModel<Rule>();
  private DefaultListModel<Rule> usedRules = new DefaultListModel<Rule>();
  private ListSelectionModel availSelModel = new DefaultListSelectionModel();
  private ListSelectionModel usedSelModel = new DefaultListSelectionModel();

  private GroupType groupType;
  private ValueModel importModel;
  private SelectionInList<ArrayListModel<AbstractGroup>> siImportList;
  private AbstractGroup importGroup;
  private boolean defaultSettings, defaultRules;
  private SportSettings settings = new SportSettings();
  private Tournament tourn = Organizer.getInstance().getCurrentTournament();

  public GroupDialogPModel(AbstractGroup group) {
    this.group = group;

    if (group instanceof Group)
      groupType = GroupType.NORMAL;
    else
      groupType = GroupType.DUELL;
    ArrayListModel<AbstractGroup> groups = Organizer.getInstance()
        .getCurrentTournament().getPlan().getOrderedGroups();
    importGroup = groups.get(0);
    importModel = new PropertyAdapter<GroupDialogPModel>(this,
        PROPERTY_IMPORTGROUP, true);
    siImportList = new SelectionInList<ArrayListModel<AbstractGroup>>(groups,
        importModel);

    this.retrieveData();
  }

  public GroupType getGroupType() {
    return groupType;
  }

  private void retrieveData() {
    this.usedRules.removeListDataListener(this);
    this.availableRules.removeListDataListener(this);

    this.name = this.group.getName();
    this.numpos = this.group.getNumPositions();
    this.defaultSettings = this.group.isDefaultSettings();
    this.defaultRules = this.group.isDefaultRules();
    if (this.group.isDefaultSettings())
      this.settings.setSportSettings(tourn.getSettings());
    else
      this.settings.setSportSettings(this.group.getSettings());

    if (this.group.isDefaultRules()) {
      try {
        for (Rule r : tourn.getDefaultRules()) {
          this.usedRules.addElement((Rule)r.clone());
        }
      }
      catch (CloneNotSupportedException e) {
        ErrorLogger.getLogger()
            .throwing("GroupDialogPModel", "retrieveData", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
    else {
      ArrayList<Rule> rules = this.group.getRules();
      for (Rule r : rules) {
        this.usedRules.addElement(r);
      }
    }

    ArrayList<Rule> temp = new ArrayList<Rule>();
    try {
      for (Rule r : Rule.ruleMap.values()) {
        if (!usedRules.contains(r))
          temp.add((Rule)r.clone());
      }
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("GroupDialogPModel", "retrieveData", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
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
      case IMPORT_RULES_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.IMPORT)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            if (importGroup != group) {
              usedRules.clear();
              availableRules.clear();

              ArrayList<Rule> rules = new ArrayList<Rule>(Rule.ruleMap.values());
              try {
                if (importGroup.isDefaultRules()) {
                  for (Rule r : tourn.getDefaultRules()) {
                    usedRules.addElement((Rule)r.clone());
                    rules.remove(r);
                  }
                }
                else {
                  for (Rule r : importGroup.getRules()) {
                    usedRules.addElement((Rule)r.clone());
                    rules.remove(r);
                  }
                }
              }
              catch (CloneNotSupportedException e) {
                ErrorLogger.getLogger().throwing("GroupDialogPModel",
                    "Import-actionperformed", e);
                ErrorDialog ed = new ErrorDialog(
                    Organizer.getInstance().getMainFrame(),
                    ResourceManager.getText(Text.ERROR), e.toString(), e);
                ed.setVisible(true);
                e.printStackTrace();
              }

              ArrayList<Rule> temp = new ArrayList<Rule>();
              for (Rule r : rules) {
                temp.add(r);
              }
              Collections.sort(temp, new RuleNameComperator<Rule>());
              for (Rule r : temp) {
                availableRules.addElement(r);
              }
            }

          }
        };
      case IMPORT_TSET_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.IMPORT)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            if (importGroup.isDefaultSettings())
              settings.setSportSettings(tourn.getSettings());
            else
              settings.setSportSettings(importGroup.getSettings());
          }
        };
      case CANCEL_ACTION:
      case OK_ACTION:
        return new WindowAction(actionkey);
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

  public SelectionInList getSelectionInList(int listkey) {
    switch (listkey) {
      case IMPORT_LIST:
        return siImportList;
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumpos() {
    return numpos;
  }

  public void setNumpos(int numpos) {
    if (numpos >= 2)
      this.numpos = numpos;
  }

  private boolean save() {
    if (!group.getName().equals(this.name))
      group.setName(this.name);
    if (groupType.equals(GroupType.NORMAL)) {
      if (this.numpos < 2) {
        return false;
      }
      if (((Group)group).getNumStartPos() != this.numpos)
        ((Group)group).setNumPositions(this.numpos);
    }

    this.group.setDefaultSettings(this.defaultSettings);
    this.group.setDefaultRules(this.defaultRules);

    // ArrayListModel<Rule> defRules = tourn.getDefaultRules();
    if (this.defaultRules) { // && this.group.getRules() != defRules
      this.group.setRules(null);
    }
    else if (!defaultRules) {
      ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
      for (int i = 0; i < this.usedRules.size(); i++) {
        rules.add(this.usedRules.get(i));
      }
      group.setRules(rules);
    }

    // SportSettings defSet = tourn.getSettings();
    if (this.defaultSettings) { // && this.group.getSettings() != defSet
      this.group.setSettings(null);
    }
    else if (!defaultSettings) {
      this.group.setSettings(this.settings);
    }

    return true;
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
      ArrayList<Point> intervals = null;
      if (this.type == ADDTO_USED_ACTION) {
        usedRules.removeListDataListener(GroupDialogPModel.this);
        availableRules.removeListDataListener(GroupDialogPModel.this);
        int max = availSelModel.getMaxSelectionIndex();
        int min = availSelModel.getMinSelectionIndex();
        if (min == -1)
          return;

        int usedmin = usedSelModel.getMinSelectionIndex() + 1;
        for (int i = max; i >= min; i--) {
          if (availSelModel.isSelectedIndex(i))
            usedRules.add(usedmin, availableRules.remove(i));
        }
        usedRules.addListDataListener(GroupDialogPModel.this);
        availableRules.addListDataListener(GroupDialogPModel.this);
      }
      else if (this.type == ADDTO_AVAILABLE_ACTION) {
        usedRules.removeListDataListener(GroupDialogPModel.this);
        availableRules.removeListDataListener(GroupDialogPModel.this);
        int max = usedSelModel.getMaxSelectionIndex();
        int min = usedSelModel.getMinSelectionIndex();
        if (min == -1)
          return;

        for (int i = max; i >= min; i--) {
          if (usedSelModel.isSelectedIndex(i))
            availableRules.addElement(usedRules.remove(i));
        }
        sortAvailableRules();
        usedRules.addListDataListener(GroupDialogPModel.this);
        availableRules.addListDataListener(GroupDialogPModel.this);
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

  class WindowAction extends AbstractAction {

    private int type;

    public WindowAction(int type) {
      this.type = type;
      if (type == GroupDialogPModel.OK_ACTION)
        putValue(Action.NAME, ResourceManager.getText(Text.OK));
      else if (type == GroupDialogPModel.CANCEL_ACTION)
        putValue(Action.NAME, ResourceManager.getText(Text.CANCEL));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      if (type == GroupDialogPModel.OK_ACTION) {
        if (GroupDialogPModel.this.save())
          GroupDialogPModel.this.firePropertyChange(
              GroupDialogPModel.PROPERTY_DISPOSE, false, true);
      }
      else if (type == GroupDialogPModel.CANCEL_ACTION) {
        GroupDialogPModel.this.firePropertyChange(
            GroupDialogPModel.PROPERTY_DISPOSE, false, true);
      }

    }

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

  public AbstractGroup getImportGroup() {
    return importGroup;
  }

  public void setImportGroup(AbstractGroup importGroup) {
    // AbstractGroup old = this.importGroup;
    this.importGroup = importGroup;
    // this.firePropertyChange(PROPERTY_IMPORTGROUP, old,
    // this.importGroup);//FIXME closes window if uncommented, why???
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

  @Override
  public void contentsChanged(ListDataEvent arg0) {
    sortAvailableRules();
  }

  @Override
  public void intervalAdded(ListDataEvent arg0) {
    sortAvailableRules();
  }

  @Override
  public void intervalRemoved(ListDataEvent arg0) {
    sortAvailableRules();
  }

  public boolean isDefaultSettings() {
    return defaultSettings;
  }

  public void setDefaultSettings(boolean defaultSettings) {
    this.defaultSettings = defaultSettings;
  }

  public boolean isDefaultRules() {
    return defaultRules;
  }

  public void setDefaultRules(boolean defaultRules) {
    this.defaultRules = defaultRules;
  }

  public ValueModel getSettingsValueModel(String propertyName) {
    return new PropertyAdapter<SportSettings>(this.settings, propertyName, true);
  }

  public void changeOrdering(int list, int index) {
    switch (list) {
      case AVAILABLE_LIST: {
        Rule r = this.availableRules.get(index);
        r.setAscending(!r.isAscending());
        break;
      }
      case USED_LIST: {
        Rule r = this.usedRules.get(index);
        r.setAscending(!r.isAscending());
        break;
      }
    }
  }

  public boolean isAscending(int list, int index) {
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
    return false;
  }

}
