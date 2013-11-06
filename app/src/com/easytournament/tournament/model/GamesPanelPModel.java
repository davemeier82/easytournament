package com.easytournament.tournament.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;



import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.export.ScheduleExportable;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.calc.Calculator;
import com.easytournament.tournament.export.GamesExportable;
import com.easytournament.tournament.model.dialog.GameDialogPModel;
import com.easytournament.tournament.model.tablemodel.GamesTableModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;


public class GamesPanelPModel extends Model implements PropertyChangeListener {
  
  public static final String PROPERTY_FILTERLABELS = "filterLabels";
  public static final String PROPERTY_FILTER = "filter";

  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected GamesTableModel tableModel;
  protected DefaultListSelectionModel selectionModel;
  private String filter = ResourceManager.getText(Text.NOFILTER);
  
  public GamesPanelPModel(){
    tableModel = new GamesTableModel(t.getSchedules());
    selectionModel = new DefaultListSelectionModel();
    ScheduleSettings.getInstance().addPropertyChangeListener(ScheduleSettings.PROPERTY_SHOWREFREES, this);
  }

  public GamesTableModel getTableModel() {
    return tableModel;
  }
  
  public ListSelectionModel getTableSelectionModel(){
    return selectionModel;
  }

  public GameDialogPModel getGameDialogPModel(int row) {
    if(row >= 0)
      return new GameDialogPModel(tableModel.get(row));
    return null;
  }

  public void togglePlayed(int row) {
    ScheduleEntry se = tableModel.get(row);
    
    se.setGamePlayed(!se.isGamePlayed());
    se.setNumHomeGoals(0);
    se.setNumAwayGoals(0);
    AbstractGroup g = se.getGroupAssignedTo();
    Calculator.calcTableEntries(g, true);
    tableModel.fireTableCellUpdated(row, 0);    
  }

  public boolean isGameEditable(int row) {
    ScheduleEntry se = tableModel.get(row);
    return se.getHomeTeam() != null && se.getAwayTeam() != null;
  }

  public ArrayList<Refree> getRefrees() {
    return t.getRefrees();
  }
  

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(ScheduleSettings.PROPERTY_SHOWREFREES)){
      this.tableModel.fireTableStructureChanged();
    }    
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

  public void exportGames(ArrayList<Integer> indices) {
    GamesExportable e = new GamesExportable(null, null);
    ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
    for(Integer i : indices){
        schedule.add(this.t.getSchedules().get(i));
    }       
    e.export(schedule, filter.equals(ResourceManager.getText(Text.NOFILTER))?"":filter);
    
  }
}
