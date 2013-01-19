package com.easytournament.designer.model;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.export.ScheduleExportable;
import com.easytournament.designer.model.tablemodel.ScheduleTableModel;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;


public class SchedulePanelPModel extends Model implements TableModelListener, PropertyChangeListener {

  private static final long serialVersionUID = -7596821302494623927L;
  public static final String PROPERTY_SHOW_TEAMS = "showTeams";
  public static final int NEW_ACTION = 0;
  public static final int DELETE_ACTION = 1;
  public static final int EXPORT_ACTION = 2;
  public static final int CHANGE_TEAMVIEW_ACTION = 3;

  protected ScheduleTableModel table;
  protected ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected boolean showTeams = false;

  protected boolean dataChanged = false;

  public SchedulePanelPModel() {
    this.table = new ScheduleTableModel(t.getSchedules());
    this.table.addTableModelListener(this);
    ScheduleSettings.getInstance().addPropertyChangeListener(ScheduleSettings.PROPERTY_SHOWREFREES, this);
  }

  public AbstractTableModel getTableModel() {
    return table;
  }

  public Action getAction(int a) {
    switch (a) {
      case NEW_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.ADD_GAME),
            ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            ArrayListModel<AbstractGroup> groups = t.getPlan().getGroups();
            if (groups.size() > 0) {
              ArrayList<Position> pos = groups.get(0).getPositions();
              ScheduleEntry se = new ScheduleEntry(pos.get(0), pos.get(1));
              pos.get(0).getGroup().getSchedules().add(se);
              table.addShedule(se);
              table.fireTableDataChanged();
            }
          }
        };

      case DELETE_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.DELETE_GAME),
            ResourceManager.getIcon(Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            SchedulePanelPModel.this.deleteAction();
          }
        };
      case EXPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EXPORT_SCHEDULE)) {
          private ScheduleExportable e = new ScheduleExportable();

          @Override
          public void actionPerformed(ActionEvent arg0) {
            e.doExport();
          }
        };
      case CHANGE_TEAMVIEW_ACTION:
        AbstractAction act = new AbstractAction("",
            ResourceManager.getIcon(Icon.CHANGE_VIEW_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            SchedulePanelPModel.this.setShowTeams(!SchedulePanelPModel.this.isShowTeams());
          }
        };
        act.putValue(Action.SHORT_DESCRIPTION,
            ResourceManager.getText(Text.SWITCH_TEAM_DESIGN_VIEW));
        return act;
    }
    return null;
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }

  public void deleteAction() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();
    if (t.getSchedules().size() > 0) {
      ArrayList<ScheduleEntry> toremove = new ArrayList<ScheduleEntry>();
      for (int i = min; i <= max; i++) {
        if(selectionmodel.isSelectedIndex(i)){
          toremove.add(t.getSchedules().get(i));
        }
      }
      for(ScheduleEntry se : toremove){
        t.getSchedules().remove(se);
        se.getGroupAssignedTo().getSchedules().remove(se);
      }
      table.fireTableDataChanged();
    }
  }

  public boolean isDataChanged() {
    return this.dataChanged;
  }

  public void setDataChanged(boolean b) {
    this.dataChanged = b;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    this.dataChanged = true;
  }

  public ArrayList<Refree> getRefrees() {
    return t.getRefrees();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(ScheduleSettings.PROPERTY_SHOWREFREES)){
      this.table.fireTableStructureChanged();
    }
    
  }

  public boolean isShowTeams() {
    return showTeams;
  }

  public void setShowTeams(boolean showTeams) {
    boolean old = this.showTeams;
    this.showTeams = showTeams;
    this.firePropertyChange(PROPERTY_SHOW_TEAMS, old, this.showTeams);
    this.table.fireTableDataChanged();
  }
}
