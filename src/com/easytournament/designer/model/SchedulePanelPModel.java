package com.easytournament.designer.model;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

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
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.export.ScheduleExportable;
import com.easytournament.designer.model.tablemodel.ScheduleTableModel;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class SchedulePanelPModel extends Model implements TableModelListener,
		PropertyChangeListener {

	private static final long serialVersionUID = -7596821302494623927L;
	public static final String PROPERTY_SHOW_TEAMS = "showTeams";
	public static final String PROPERTY_STOP_CELL_EDITING = "stopCellEditing";
	public static final String PROPERTY_FILTERLABELS = "filterLabels";
	public static final String PROPERTY_FILTER = "filter";

	public static final int NEW_ACTION = 0;
	public static final int DELETE_ACTION = 1;
	public static final int CHANGE_TEAMVIEW_ACTION = 3;

	protected ScheduleTableModel tableModel;
	protected ListSelectionModel selectionmodel = new DefaultListSelectionModel();
	protected Tournament t = Organizer.getInstance().getCurrentTournament();
	protected boolean showTeams = false;
	protected boolean dataChanged = false;
	private String filter = ResourceManager.getText(Text.NOFILTER);

	public SchedulePanelPModel() {
		this.tableModel = new ScheduleTableModel(t.getSchedules());
		this.tableModel.addTableModelListener(this);
		ScheduleSettings.getInstance().addPropertyChangeListener(
				ScheduleSettings.PROPERTY_SHOWREFREES, this);
	}

	public AbstractTableModel getTableModel() {
		return tableModel;
	}

	public Action getAction(int a) {
		switch (a) {
		case NEW_ACTION:
			return new AbstractAction(ResourceManager.getText(Text.ADD_GAME),
					ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ArrayListModel<AbstractGroup> groups = t.getPlan()
							.getGroups();
					if (groups.size() > 0) {
						ArrayList<Position> pos = groups.get(0).getPositions();
						ScheduleEntry se = new ScheduleEntry(pos.get(0),
								pos.get(1));
						pos.get(0).getGroup().getSchedules().add(se);
						tableModel.addShedule(se);
						tableModel.fireTableDataChanged();
					}
				}
			};

		case DELETE_ACTION:
			return new AbstractAction(
					ResourceManager.getText(Text.DELETE_GAME),
					ResourceManager.getIcon(Icon.DELETE_ICON_SMALL)) {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					SchedulePanelPModel.this.deleteAction();
				}
			};
		case CHANGE_TEAMVIEW_ACTION:
			AbstractAction act = new AbstractAction("",
					ResourceManager.getIcon(Icon.CHANGE_VIEW_ICON_SMALL)) {

				@Override
				public void actionPerformed(ActionEvent e) {
					SchedulePanelPModel.this
							.setShowTeams(!SchedulePanelPModel.this
									.isShowTeams());
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
				if (selectionmodel.isSelectedIndex(i)) {
					toremove.add(t.getSchedules().get(i));
				}
			}
			for (ScheduleEntry se : toremove) {
				t.getSchedules().remove(se);
				se.getGroupAssignedTo().getSchedules().remove(se);
			}
			tableModel.fireTableDataChanged();
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
		if (evt.getPropertyName().equals(ScheduleSettings.PROPERTY_SHOWREFREES)) {
			this.tableModel.fireTableStructureChanged();
		}

	}

	public boolean isShowTeams() {
		return showTeams;
	}

	public void setShowTeams(boolean showTeams) {
		boolean old = this.showTeams;
		this.showTeams = showTeams;
		this.firePropertyChange(PROPERTY_SHOW_TEAMS, old, this.showTeams);
		this.tableModel.fireTableDataChanged();
	}

	public void stopCellEditing() {
		this.firePropertyChange(PROPERTY_STOP_CELL_EDITING, false, true);
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
		this.tableModel.fireTableDataChanged();
	}

	public List<String> getFilterLabels() {
		ArrayList<String> filters = new ArrayList<String>();
		ArrayListModel<AbstractGroup> groups = this.t.getPlan()
				.getOrderedGroups();
		filters.add(ResourceManager.getText(Text.NOFILTER));
		for (AbstractGroup g : groups) {
			filters.add(g.getName());
		}

		for (Team team : this.t.getTeams()) {
			filters.add(team.getName());
		}
		for (Refree ref : this.t.getRefrees()) {
			filters.add(ref.getName());
		}
		return filters;
	}
	
	public void exportSchedule(List<Integer> indices){		
		ScheduleExportable e = new ScheduleExportable(null, null);
		ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
		for(Integer i : indices){
			schedule.add(this.t.getSchedules().get(i));
		}		
		e.export(schedule, isShowTeams(), filter.equals(ResourceManager.getText(Text.NOFILTER))?"":filter);
	}
}
