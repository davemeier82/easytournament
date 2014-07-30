package com.easytournament.designer.gui.jgraph;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.DuellGroup;
import com.easytournament.designer.valueholder.Position;
import com.jgoodies.common.collect.ArrayListModel;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;


public class DuellGroupCell extends mxCell implements PropertyChangeListener {

  private static final long serialVersionUID = 5944383440934258448L;
  protected double titleHeight;

  public DuellGroupCell(AbstractGroup g, mxGeometry geom, double titleHeight) {
    super(g, geom,
        TournamentViewer.GROUP_IDENTIFIER+";foldable=false;editable=false;whiteSpace=wrap");
    this.titleHeight = titleHeight;
    this.setConnectable(false);
    this.setVertex(true);

    g.addPropertyChangeListener(this);

    addPostions(g);
  }

  protected void addPostions(AbstractGroup g) {

    double posStartY = this.titleHeight / this.geometry.getHeight();
    mxGeometry geo1 = new mxGeometry(0, posStartY, this.geometry.getWidth() / 2.0,
        this.geometry.getHeight() * (1.0-posStartY));
    mxGeometry geo2 = new mxGeometry(0.5, posStartY, this.geometry.getWidth() / 2.0,
        this.geometry.getHeight() * (1.0-posStartY));
    geo1.setRelative(true);
    geo2.setRelative(true);

    mxCell pos1 = new mxCell(getLabel(g, 0), geo1, TournamentViewer.TEAM_IDENTIFIER+";whiteSpace=wrap;deletable=false");
    this.insert(pos1, 0);
    pos1.setParent(this);
    pos1.setConnectable(true);
    pos1.setVertex(true);

    mxCell pos2 = new mxCell(getLabel(g, 1), geo2, TournamentViewer.TEAM_IDENTIFIER+";whiteSpace=wrap;deletable=false");
    this.insert(pos2, 1);
    pos2.setParent(this);
    pos2.setConnectable(true);
    pos2.setVertex(true);
  }

  public void setPositionStyle(int index, String style) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    mxIGraphModel model = graph.getModel();
    model.beginUpdate();
    try {
      model.setStyle(this.getChildAt(index), style);
    }
    finally {
      model.endUpdate();
    }
  }

  protected String getLabel(AbstractGroup g, int i) {
    String value;
    Position p = g.getPositions().get(i);
    if (p.getPrev() == null) {
      if(DesignerItem.getTournamentViewer() == null || !DesignerItem.getTournamentViewer().isTeamView() || g.getTable().size() <= i) {
        if (i == 0)
          value = ResourceManager.getText(Text.WINNER) + " " + g.getName();
        else
          value = ResourceManager.getText(Text.LOSER) + " " + g.getName();
      } else {
        value = g.getTable().get(i).getTeam().getName();
      }
    }
    else {
      AbstractGroup pg = p.getPrev().getGroup();
      int idx = pg.getPositions().indexOf(p.getPrev());
      if(DesignerItem.getTournamentViewer() == null || !DesignerItem.getTournamentViewer().isTeamView() || !p.getPrev().getGroup().isAllGamesPlayed()|| p.getPrev().getGroup().getTable().size() <= idx)
        value = p.getPrev().getName();
      else
        value = pg.getTable().get(idx).getTeam().getName();
    }
    return value;
  }
  
  public void updateLabels(){
	if(this.getChildCount() > 0) {
		mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
		mxCellState state = graph.getView().getState(this.getChildAt(0));
		AbstractGroup group = this.getGroup();
		if(group != null){
	        state.setLabel(getLabel(group, 0));		  
		}
		if(this.getChildCount() > 1) {
		  this.getChildAt(0).setValue(state.getLabel());
		  state = graph.getView().getState(this.getChildAt(1));
		  state.setLabel(getLabel(this.getGroup(), 1));
		  this.getChildAt(1).setValue(state.getLabel());
		}
	}
  }

  @Override
  public void propertyChange(PropertyChangeEvent e) {
    if (e.getPropertyName() == null)
      return;
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
      
    mxIGraphModel model = graph.getModel();
    model.beginUpdate();
    try {
      if (e.getPropertyName().equals(AbstractGroup.PROPERTY_NAME)) {
        DuellGroup oldValue = (DuellGroup)cloneValue();
        oldValue.setName((String)e.getOldValue());
        model.setValue(this, oldValue);        
        model.setValue(this.getChildAt(0), getLabel(this.getGroup(), 0));
        model.setValue(this.getChildAt(1), getLabel(this.getGroup(), 1));
      }
      else if (e.getPropertyName().equals(Position.PROPERTY_PERVIOUS)) {
        if(this.getChildCount() == 2)
        {
          mxCellState state = graph.getView().getState(this.getChildAt(0));
          if(state != null)
          {
            state.setLabel(getLabel(this.getGroup(), 0));
            this.getChildAt(0).setValue(state.getLabel());
          }
          state = graph.getView().getState(this.getChildAt(1));
          if(state != null)
          {
            state.setLabel(getLabel(this.getGroup(), 1));
            this.getChildAt(1).setValue(state.getLabel());
          }
        }
      }
      else if (e.getPropertyName().equals(AbstractGroup.PROPERTY_RULES)) {
          DuellGroup oldValue = (DuellGroup) cloneValue();
          oldValue.setRules((ArrayListModel<Rule>) e.getOldValue());
          model.setValue(this, oldValue);
      }
    }
    finally {
      model.endUpdate();
    }
    DesignerItem.getTournamentViewer().refreshLabels();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    DuellGroupCell c = (DuellGroupCell)super.clone();
    c.getGroup().addPropertyChangeListener(c);
    return c;
  }

  @Override
  protected Object cloneValue() {
    AbstractGroup g = this.getGroup();
    AbstractGroup clone = new DuellGroup(g.getName(), true);
    if(clone.isDefaultRules())
      clone.setRules(g.getRules());
    else
      try {
        for (Rule r : g.getRules()) {
          clone.getRules().add((Rule)r.clone());
        }
      }
      catch (CloneNotSupportedException cce) {
        ErrorLogger.getLogger().throwing("DuellGroupCell", "cloneValue", cce);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), cce.toString(), cce);
        ed.setVisible(true);
        cce.printStackTrace();
      }
    if(clone.isDefaultSettings())
      clone.setSettings(g.getSettings());
    else
      clone.getSettings().setSportSettings(g.getSettings());
    return clone;
  }

  public AbstractGroup getGroup() {
    return (AbstractGroup)value;
  }
  
  public void updateLabel(){
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    mxCellState state = graph.getView().getState(this);
    state.setLabel(this.getGroup().getName());
    graph.repaint();
  }
  
  public double getTitleHeigth(){
    return this.titleHeight;
  }
  
  public void setTitleHeigth(double h){
    this.titleHeight = h;
  }

}
