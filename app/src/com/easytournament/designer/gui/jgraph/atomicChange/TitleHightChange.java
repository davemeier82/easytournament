package com.easytournament.designer.gui.jgraph.atomicChange;

import com.easytournament.designer.gui.jgraph.DuellGroupCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;


public class TitleHightChange extends mxAtomicGraphModelChange {
  
  protected DuellGroupCell cell;
  protected double value, previous;
  
  public TitleHightChange(mxGraphModel model, DuellGroupCell cell,
      double oldvalue){
    super(model);
    this.cell = cell;
    this.value = oldvalue;
    this.previous = cell.getTitleHeigth();
  }

  @Override
  public void execute() {
    
    cell.setTitleHeigth(this.previous);
    double tmp = this.previous;
    this.previous = this.value;
    this.value = tmp;
  }

}
