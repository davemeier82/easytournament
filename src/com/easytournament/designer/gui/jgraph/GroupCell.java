package com.easytournament.designer.gui.jgraph;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Group;
import com.easytournament.designer.valueholder.Position;
import com.jgoodies.common.collect.ArrayListModel;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;


public class GroupCell extends DuellGroupCell {

  private static final long serialVersionUID = -7391360674491922447L;

  protected HashMap<Integer,mxICell> inEdges;
  protected HashMap<Integer,ArrayList<mxICell>> outEdges;
  protected HashMap<Integer, String> styles;

  public GroupCell(Group g, mxGeometry geom, double titleHeight) {
    super(g, geom, titleHeight);
  }

  @Override
  protected void addPostions(AbstractGroup g) {
    if (inEdges == null) { // not nice!
      inEdges = new HashMap<Integer,mxICell>();
      outEdges = new HashMap<Integer,ArrayList<mxICell>>();
      styles = new HashMap<Integer, String>();
    }

    int nPos = g.getNumPositions();

    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    mxGraphModel model = (mxGraphModel)graph.getModel();

    double posStartY = this.titleHeight / this.geometry.getHeight();

    for (int i = 0; i < nPos; i++) {
      mxGeometry geo1 = new mxGeometry(0, posStartY + (1.0-posStartY) / nPos * i,
          this.geometry.getWidth(), (this.geometry.getHeight() * (1.0-posStartY)) / nPos);

      geo1.setRelative(true);
      String style = styles.get(new Integer(i));
      if(style == null && i > 0)
        style = styles.get(new Integer(styles.size()-1));
        
      mxCell pos = new mxCell(getLabel(g, i), geo1, style==null?TournamentViewer.TEAM_IDENTIFIER+";whiteSpace=wrap;deletable=false":style);
      pos.setId(model.createId(pos));

      this.insert(pos, i);
      pos.setParent(this);

      pos.setConnectable(true);
      pos.setVertex(true);

      mxICell inEdge = inEdges.get(new Integer(i));
      if (inEdge != null) {
        inEdge.setTerminal(pos, false);
        pos.insertEdge(inEdge, false);
      }
      ArrayList<mxICell> outEdges2 = outEdges.get(new Integer(i));
      if (outEdges2 != null)
        for (mxICell edge : outEdges2) {
          edge.setTerminal(pos, true);
          pos.insertEdge(edge, true);
        }
    }
    
  }
  
  protected ArrayList<EdgeValueHolder> removeEdges(){

    int nPos = this.getGroup().getNumPositions();
    ArrayList<EdgeValueHolder> deletedEdges = new ArrayList<EdgeValueHolder>();

    for (Integer i : inEdges.keySet()) {
      if (i.intValue() >= nPos) {
        mxICell e = inEdges.get(i);
        mxICell sTerminal = e.getTerminal(true);
        DuellGroupCell sGroup= (DuellGroupCell) sTerminal.getParent();
        int sIdx = sGroup.getIndex(sTerminal);
        int tIdx = i.intValue();
        e.setTerminal(null, true);
        e.setTerminal(null, false);
        mxICell parent = e.getParent();
        parent.remove(e);
        e.setParent(null);
        
        deletedEdges.add(new EdgeValueHolder(e, sGroup, this, sIdx, tIdx, parent));
      }
    }
    for (Integer i : outEdges.keySet()) {
      if (i.intValue() >= nPos) {
        for (mxICell edge : outEdges.get(i)) {
          mxICell tTerminal = edge.getTerminal(false);
          DuellGroupCell tGroup= (DuellGroupCell) tTerminal.getParent();
          int tIdx = tGroup.getIndex(tTerminal);
          int sIdx = i.intValue();
          
          edge.setTerminal(null, true);
          edge.setTerminal(null, false);
          mxICell parent = edge.getParent();
          parent.remove(edge);
          edge.setParent(null);
          
          deletedEdges.add(new EdgeValueHolder(edge, this, tGroup, sIdx, tIdx, parent));          
        }
      }
    }

    return deletedEdges;
  }

  @Override
  protected String getLabel(AbstractGroup g, int i) {
    String value;
    Position p = g.getPositions().get(i);
    if (p.getPrev() == null) {
      if(DesignerItem.getTournamentViewer() == null || !DesignerItem.getTournamentViewer().isTeamView() || g.getTable().size() <= i)
        value = (i + 1) + ". " + g.getName();
      else
        value = g.getTable().get(i).getTeam().getName();
    }
    else {
      AbstractGroup pg = p.getPrev().getGroup();
      int idx = pg.getPositions().indexOf(p.getPrev());
      if(DesignerItem.getTournamentViewer() == null || !DesignerItem.getTournamentViewer().isTeamView() || !p.getPrev().getGroup().isAllGamesPlayed() || p.getPrev().getGroup().getTable().size() <= idx)
        value = p.getPrev().getName();
      else
        value = pg.getTable().get(idx).getTeam().getName();
    }
    return value;
  }

  public  ArrayList<EdgeValueHolder> setPostions() {
    int count = this.getChildCount();

    ArrayList<mxICell> toRemove = new ArrayList<mxICell>();
    for (int i = 0; i < count; i++) {
      mxICell cell = this.getChildAt(i);
      styles.put(new Integer(i), cell.getStyle());
      ArrayList<mxICell> out = new ArrayList<mxICell>();
      for (int e = 0; e < cell.getEdgeCount(); e++) {
        mxICell edge = cell.getEdgeAt(e);
        if (edge.getTerminal(true) == cell) {
          out.add(edge);
        }
        else {
          inEdges.put(new Integer(i), edge);
        }
      }
      if (out.size() > 0)
        outEdges.put(new Integer(i), out);

      toRemove.add(cell);
    }
    
    ArrayList<EdgeValueHolder> remEdges = removeEdges();
    for(mxICell c : toRemove){
      this.remove(c);
    }
    
    addPostions(this.getGroup());
    
    inEdges.clear();
    outEdges.clear();
    return remEdges;
  }

  @Override
  public void updateLabels(){
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    for (int i = 0; i < this.getChildCount(); i++) {
      mxCellState state = graph.getView().getState(this.getChildAt(i));
      try {
        String label = getLabel(this.getGroup(), i);
        if(state != null)
          state.setLabel(label);
        this.getChildAt(i).setValue(label);
      } catch(IndexOutOfBoundsException iobe){/*do nothing*/}
    }
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent e) {

    if (e.getPropertyName() == null)
      return;

    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    GraphModel model = (GraphModel)graph.getModel();
    model.beginUpdate();
    try {
      if (e.getPropertyName().equals(Group.PROPERTY_NUMPOS)) {
        // setPostions();

        Group oldValue = (Group)cloneValue();
        oldValue.setNumPositions(((Integer)e.getOldValue()).intValue());
        model.setValue(this, oldValue);
      }
      else if (e.getPropertyName().equals(AbstractGroup.PROPERTY_NAME)) {
        Group oldValue = (Group)cloneValue();
        oldValue.setGroupName((String)e.getOldValue());
        model.setValue(this, oldValue);
        for (int i = 0; i < this.getChildCount(); i++) {
          model.setValue(this.getChildAt(i), getLabel(this.getGroup(), i));
        }
      }
      else if (e.getPropertyName().equals(Position.PROPERTY_PERVIOUS)) {
        for (int i = 0; i < this.getChildCount(); i++) {
          mxCellState state = graph.getView().getState(this.getChildAt(i));
          try {
            String label = getLabel(this.getGroup(), i);
            if(state != null)
              state.setLabel(label);
            this.getChildAt(i).setValue(label);
          } catch(IndexOutOfBoundsException iobe){/*do nothing*/}
        }
      }
      else if (e.getPropertyName().equals(AbstractGroup.PROPERTY_RULES)) {
        Group oldValue = (Group)cloneValue();
        oldValue.setRules((ArrayListModel<Rule>)e.getOldValue());
        model.setValue(this, oldValue);
      }
    }
    finally {
      model.endUpdate();
    }
    TournamentViewer.getGraphComponent().refresh();
    DesignerItem.getTournamentViewer().refreshLabels();
    
  }

  @Override
  protected Object cloneValue() {
    Group g = (Group)this.getGroup();
    Group clone = new Group(g.getName(), g.getNumPositions());
    if(clone.isDefaultRules())
      clone.setRules(null);
    else
      try {
        for (Rule r : g.getRules()) {
          clone.getRules().add((Rule)r.clone());
        }
      }
      catch (CloneNotSupportedException cce) {
        ErrorLogger.getLogger().throwing("GroupCell", "cloneValue", cce);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), cce.toString(), cce);
        ed.setVisible(true);
        cce.printStackTrace();
      }
    if(clone.isDefaultSettings())
      clone.setSettings(null);
    else
      clone.getSettings().setSportSettings(g.getSettings());
    return clone;
  }
  
  public void addEdges(ArrayList<EdgeValueHolder> edges) {
    for (EdgeValueHolder e : edges) {
      mxCell edge = (mxCell) e.getEdge();
      edge.setParent(e.getParent());
      e.getParent().insert(edge);
      mxICell source = e.getSourceGroup().getChildAt(e.getSourcePosIndex());
      source.insertEdge(edge, true);
      edge.setTerminal(source, true);
      mxICell target = e.getTargetGroup().getChildAt(e.getTargetPosIndex());
      target.insertEdge(edge, false);
      edge.setTerminal(target, false);
      
      Position sPos = e.getSourceGroup().getGroup().getPosition(e.getSourcePosIndex());
      Position tPos = e.getTargetGroup().getGroup().getPosition(e.getTargetPosIndex());
      sPos.addNext(tPos);
      tPos.setPrev(sPos);
    }
  }

}
