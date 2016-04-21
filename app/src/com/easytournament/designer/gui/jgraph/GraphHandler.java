package com.easytournament.designer.gui.jgraph;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import com.easytournament.designer.TournamentViewer;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraphView;


public class GraphHandler extends mxGraphHandler {

  public GraphHandler(mxGraphComponent graphComponent) {
    super(graphComponent);
  }
  
  protected int mouseDownX, mouseDownY;
  protected double origTitleHight;
  protected boolean resize = false;
  protected DuellGroupCell resizeCell;

  @Override
  protected Cursor getCursor(MouseEvent e) {
    Cursor c = super.getCursor(e);
    Object cellObject = graphComponent.getCellAt(e.getX(), e.getY());
    if (cellObject instanceof DuellGroupCell) {
      DuellGroupCell cell = (DuellGroupCell) cellObject;
      if (cell != null) {
        mxGraphView view = graphComponent.getGraph().getView();
        mxCellState state = view.getState(cell);
        if (e.getY() - state.getY() > cell.getTitleHeigth() * view.getScale() - 5  
            && e.getY() - state.getY() < cell.getTitleHeigth() * view.getScale()) {
          c = new Cursor(Cursor.N_RESIZE_CURSOR);
        }
      }
    }
    return c;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    mouseDownX = e.getX();
    mouseDownY = e.getY();
    this.resize = false;
    Object cellObject = graphComponent.getCellAt(e.getX(), e.getY());
    if (cellObject instanceof DuellGroupCell) {
      resizeCell = (DuellGroupCell) cellObject;
      if (resizeCell != null) {
        this.origTitleHight = resizeCell.getTitleHeigth();
        mxGraphView view = graphComponent.getGraph().getView();
        mxCellState state = view.getState(resizeCell);
        this.resize = (e.getY() - state.getY() > resizeCell.getTitleHeigth() * view.getScale() - 5  
            && e.getY() - state.getY() < resizeCell.getTitleHeigth() * view.getScale()) ;
      }      
    }
    if(resize){
      e.consume();
      graphComponent.getGraph().setSelectionCells(new Object[]{(DuellGroupCell)graphComponent.getCellAt(mouseDownX,
          this.mouseDownY)});
      movePreview.start(e,
          graphComponent.getGraph().getView().getState(initialCell));
    }
    
    super.mousePressed(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    
      if (resize) {
        double th = Math.max(20, this.origTitleHight+(e.getY()-this.mouseDownY)/graphComponent.getGraph().getView().getScale());
        resizeCell.setTitleHeigth(th);
        movePreview.update(e, 0, 0, false);        
      }
    super.mouseDragged(e);
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (resize) {

      resize = false;
      mxGraphModel model = (mxGraphModel)TournamentViewer.getGraphComponent()
          .getGraph().getModel();
      model.beginUpdate();
      try {
        graphComponent.getGraph().setSelectionCells(new Object[] {resizeCell});
        ((GraphModel)graphComponent.getGraph().getModel()).setTitleHeight(
            resizeCell, this.origTitleHight);
        movePreview.stop(true, e, 0, 0, false, graphComponent.getGraph()
            .getDefaultParent());
        graphComponent.getGraph().refresh();
        graphComponent.repaint();
      }
      finally {
        model.endUpdate();
      }
    }
    super.mouseReleased(e);
  }
  
  

}
