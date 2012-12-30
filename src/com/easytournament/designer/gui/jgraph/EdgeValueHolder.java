package com.easytournament.designer.gui.jgraph;

import com.mxgraph.model.mxICell;

public class EdgeValueHolder {

  protected DuellGroupCell sourceGroup;
  protected DuellGroupCell targetGroup;
  protected int sourcePosIndex;
  protected int targetPosIndex;
  protected mxICell parent;
  protected mxICell edge; 
  
  public EdgeValueHolder(){}
  
  public EdgeValueHolder(mxICell edge, DuellGroupCell sourceGroup,
      DuellGroupCell targetGroup, int sourcePosIndex, int targetPosIndex, mxICell parent) {
    super();
    this.edge = edge;
    this.sourceGroup = sourceGroup;
    this.targetGroup = targetGroup;
    this.sourcePosIndex = sourcePosIndex;
    this.targetPosIndex = targetPosIndex;
    this.parent = parent;
  }
  public DuellGroupCell getSourceGroup() {
    return sourceGroup;
  }
  public void setSourceGroup(DuellGroupCell sourceGroup) {
    this.sourceGroup = sourceGroup;
  }
  public DuellGroupCell getTargetGroup() {
    return targetGroup;
  }
  public void setTargetGroup(DuellGroupCell targetGroup) {
    this.targetGroup = targetGroup;
  }
  public int getSourcePosIndex() {
    return sourcePosIndex;
  }
  public void setSourcePosIndex(int sourcePosIndex) {
    this.sourcePosIndex = sourcePosIndex;
  }
  public int getTargetPosIndex() {
    return targetPosIndex;
  }
  public void setTargetPosIndex(int targetPosIndex) {
    this.targetPosIndex = targetPosIndex;
  }
  public mxICell getParent() {
    return parent;
  }
  public void setParent(mxICell parent) {
    this.parent = parent;
  }
  public mxICell getEdge() {
    return edge;
  }
  public void setEdge(mxICell edge) {
    this.edge = edge;
  } 
  
}
