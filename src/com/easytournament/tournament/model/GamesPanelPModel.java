package com.easytournament.tournament.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;



import com.easytournament.basic.Organizer;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.calc.Calculator;
import com.easytournament.tournament.model.dialog.GameDialogPModel;
import com.easytournament.tournament.model.tablemodel.GamesTableModel;
import com.jgoodies.binding.beans.Model;


public class GamesPanelPModel extends Model implements PropertyChangeListener {
  
  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected GamesTableModel tableModel;
  protected DefaultListSelectionModel selectionModel;
  
  public GamesPanelPModel(){
    tableModel = new GamesTableModel(t.getSchedules());
    selectionModel = new DefaultListSelectionModel();
    ScheduleSettings.getInstance().addPropertyChangeListener(ScheduleSettings.PROPERTY_SHOWREFREES, this);
  }

  public TableModel getTableModel() {
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

}
