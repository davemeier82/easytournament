package com.easytournament.designer.gui.jgraph;

import java.util.ArrayList;
import java.util.Collection;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.settings.DesignerSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.Filter;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

public class TGraph extends mxGraph {

  private mxICell currentSource, currentTarget, oldCell, currentEdge;

  public TGraph() {
    super(new GraphModel(), null);
    this.setAllowDanglingEdges(false);
    this.allowLoops = false;
    this.multigraph = false;
  }

  @Override
  public String getEdgeValidationError(Object edge, Object source, Object target) {
    String err = super.getEdgeValidationError(edge, source, target);

    if (err == null) {
      try {

        boolean isNewEdge = (source instanceof mxCell)
            && (target instanceof mxCell);

        boolean isSource;
        if (isNewEdge) {
          isSource = false;
        }
        else {
          isSource = (source instanceof mxCell) && !(target instanceof mxCell);
        }
        int srcIdx, trgIdx;
        DuellGroupCell src;
        DuellGroupCell trg;
        DuellGroupCell old;

        String oldEdgeId = null;
        if (isSource) {
          src = (DuellGroupCell)((mxCell)source).getParent();
          mxICell tmpTrg = ((mxCell)edge).getTerminal(false);
          if (tmpTrg == null)
            return err;
          trg = (DuellGroupCell)tmpTrg.getParent();
          old = (DuellGroupCell)((mxCell)edge).getTerminal(true).getParent();
          srcIdx = src.getIndex((mxCell)source);
          trgIdx = trg.getIndex(((mxCell)edge).getTerminal(false));

          currentSource = (mxCell)source;
          currentTarget = ((mxCell)edge).getTerminal(false);
          oldCell = ((mxCell)edge).getTerminal(true);

          for (int i = 0; i < currentTarget.getEdgeCount(); i++) {
            if (currentTarget.getEdgeAt(i).getTerminal(false) == currentTarget) {
              oldEdgeId = currentTarget.getEdgeAt(i).getId();
              break;
            }
          }

        }
        else {
          currentSource = ((mxCell)edge).getTerminal(true);
          currentTarget = (mxCell)target;
          if (currentSource == null)
            return err;
          try {
            src = (DuellGroupCell)currentSource.getParent();
            if (isNewEdge)
              oldCell = null;
            else
              oldCell = ((mxCell)edge).getTerminal(false);
            old = oldCell == null? null : (DuellGroupCell)oldCell.getParent();

            trg = (DuellGroupCell)((mxCell)target).getParent();
          }
          catch (ClassCastException ccex) {
            return "";
          }

          srcIdx = src.getIndex(currentSource);
          trgIdx = trg.getIndex(currentTarget);

          if (oldCell != null) {
            for (int i = 0; i < oldCell.getEdgeCount(); i++) {
              if (oldCell.getEdgeAt(i).getTerminal(false) == oldCell) {
                oldEdgeId = oldCell.getEdgeAt(i).getId();
                break;
              }
            }
          }
        }
        currentEdge = (mxICell)edge;

        // target same group as source
        if (src == trg) {
          return "";
        }

        Position srcPos, trgPos;
        try {
          srcPos = src.getGroup().getPosition(srcIdx);
          trgPos = trgIdx < 0? null : trg.getGroup().getPosition(trgIdx);
        }
        catch (IndexOutOfBoundsException e) {
          return "";
        }

        // more than one in-flow in a position
        if (!isSource && trgPos != null && trgPos.getPrev() != null
            && trgPos.getPrev() != srcPos)
          return "";

        ArrayList<Position> srcPosList = src.getGroup().getPositions();

        // more than one flow to a group from same position
        if (isSource) {
          for (Position next : srcPosList.get(srcIdx).getNext()) {
            if (next.getGroup() == trg.getGroup() && next != trgPos) {
              return "";
            }
          }
        }
        else {
          for (Position next : srcPosList.get(srcIdx).getNext()) {
            if (next.getGroup() == trg.getGroup()
                && (old == null || trg.getGroup() != old.getGroup())) {
              return "";
            }
          }
        }

        // loop check
        for (int i = 0; i < trg.getChildCount(); i++) {
          if (hasLoop(src.getGroup(), trg.getChildAt(i)))
            return "";
        }
        // same source check
        if (isNewEdge) {
          ArrayList<mxICell> splits = new ArrayList<mxICell>();
          getSplitPosBeforeNew(splits, src.getChildAt(srcIdx));
          for (mxICell c : splits) {
            // System.out.println(c.getValue());
            if (doSplitRejoinNew(c)) {
              return "";
            }
          }
        }
        else {
          ArrayList<mxICell> splits = new ArrayList<mxICell>();
          getSplitPosBefore(splits, src.getChildAt(srcIdx), isSource);
          for (mxICell c : splits) {
            // System.out.println(c.getValue());
            if (doSplitRejoin(c, oldEdgeId)) {
              return "";
            }
          }
        }
      }
      catch (Exception ex) {
        ErrorLogger.getLogger()
            .throwing("TGraph", "getEdgeValidationError", ex);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), ex.toString(), ex);
        ed.setVisible(true);
        ex.printStackTrace();
      }
    }
    return err;
  }

  private boolean hasLoop(AbstractGroup loopGroup, mxICell target) {
    DuellGroupCell trg = (DuellGroupCell)((mxCell)target).getParent();

    if (trg.getGroup() == loopGroup)
      return true;

    mxICell cell, edge;
    for (int i = 0; i < trg.getChildCount(); i++) {
      cell = trg.getChildAt(i);
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        if (edge.getTerminal(false) != cell)
          if (hasLoop(loopGroup, edge.getTerminal(false)))
            return true;
      }
    }
    return false;
  }

  private void getSplitPosBeforeNew(ArrayList<mxICell> splits, mxICell source) {
    mxICell trg = ((mxCell)source).getParent();
    mxICell cell, edge;
    for (int i = 0; i < trg.getChildCount(); i++) {
      cell = trg.getChildAt(i);
      int numOut = 0;
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        if (edge.getTerminal(true) == cell) {
          numOut++;
        }
      }
      if (numOut > 1)
        splits.add(cell);
    }

    for (int i = 0; i < trg.getChildCount(); i++) {
      cell = trg.getChildAt(i);
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        if (edge.getTerminal(true) != cell) {
          getSplitPosBeforeNew(splits, edge.getTerminal(true));
        }
      }
    }
  }

  private boolean doSplitRejoinNew(mxICell split) {
    mxICell edge;

    ArrayList<String> ids = new ArrayList<String>();
    for (int j = 0; j < split.getEdgeCount(); j++) {
      edge = split.getEdgeAt(j);
      if (edge.getTerminal(false) != split && edge.getTerminal(false) != null)
        if (checkRejoinNew(ids, edge.getTerminal(false).getParent()))
          return true;
    }
    if (split == currentSource && currentEdge.getTerminal(false) == null) {
      if (checkRejoinNew(ids, currentTarget.getParent()))
        return true;
    }
    return false;
  }

  private boolean checkRejoinNew(ArrayList<String> ids, mxICell group) {
    if (ids.contains(group.getId())) {
      return true;
    }
    ids.add(group.getId());

    mxICell cell, edge;
    ArrayList<String> visitedids = new ArrayList<String>();
    for (int i = 0; i < group.getChildCount(); i++) {
      cell = group.getChildAt(i);
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);

        if (edge.getTerminal(false) != cell && edge.getTerminal(false) != null) {
          String trgGroupId = edge.getTerminal(false).getParent().getId();
          if (!visitedids.contains(trgGroupId)) {
            if (checkRejoinNew(ids, edge.getTerminal(false).getParent()))
              return true;
            visitedids.add(trgGroupId);
          }
        }

      }

      if (cell == currentSource && currentEdge.getTerminal(false) == null
          && !visitedids.contains(currentTarget.getParent().getId())) {
        if (checkRejoinNew(ids, currentTarget.getParent()))
          return true;
      }
    }
    return false;
  }

  private void getSplitPosBefore(ArrayList<mxICell> splits, mxICell source,
      boolean isSource) {
    mxICell trg = ((mxCell)source).getParent();
    mxICell cell, edge;
    for (int i = 0; i < trg.getChildCount(); i++) {
      cell = trg.getChildAt(i);
      int numOut = isSource && cell == currentSource? 1 : 0;
      if (oldCell == cell)
        numOut--;
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        if (edge.getTerminal(true) == cell) {
          numOut++;
        }
      }
      if (numOut > 1)
        splits.add(cell);
    }

    for (int i = 0; i < trg.getChildCount(); i++) {
      cell = trg.getChildAt(i);
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        mxICell prev = edge.getTerminal(true);
        if (prev != null && prev != cell) {
          getSplitPosBefore(splits, prev, isSource);
        }
      }
    }
  }

  private boolean doSplitRejoin(mxICell split, String oldEdgeId) {
    mxICell edge;

    ArrayList<String> ids = new ArrayList<String>();
    for (int j = 0; j < split.getEdgeCount(); j++) {
      edge = split.getEdgeAt(j);
      if (oldEdgeId == null || !oldEdgeId.equals(edge.getId()))
        if (edge.getTerminal(false) != split && edge.getTerminal(false) != null)
          if (checkRejoin(ids, edge.getTerminal(false).getParent(), oldEdgeId))
            return true;
    }
    if (split == currentSource && currentTarget != null) {
      if (checkRejoin(ids, currentTarget.getParent(), oldEdgeId))
        return true;
    }
    return false;
  }

  private boolean checkRejoin(ArrayList<String> ids, mxICell group,
      String oldEdgeId) {
    if (ids.contains(group.getId())) {
      return true;
    }
    ids.add(group.getId());

    mxICell cell, edge;
    ArrayList<String> visitedids = new ArrayList<String>();
    for (int i = 0; i < group.getChildCount(); i++) {
      cell = group.getChildAt(i);
      for (int j = 0; j < cell.getEdgeCount(); j++) {
        edge = cell.getEdgeAt(j);
        if (oldEdgeId == null || !oldEdgeId.equals(edge.getId())) {
          if (edge.getTerminal(false) != cell
              && edge.getTerminal(false) != null) {
            String trgGroupId = edge.getTerminal(false).getParent().getId();
            if (!visitedids.contains(trgGroupId)) {
              if (checkRejoin(ids, edge.getTerminal(false).getParent(),
                  oldEdgeId))
                return true;
              visitedids.add(trgGroupId);
            }
          }
        }
      }
      if (cell == currentSource && currentTarget != null
          && !visitedids.contains(currentTarget.getParent().getId())) {
        if (checkRejoin(ids, currentTarget.getParent(), oldEdgeId))
          return true;
      }
    }
    return false;
  }

  /**
   * Selects all vertices and/or edges depending on the given boolean arguments
   * recursively, starting at the given parent or the default parent if no
   * parent is specified. Use <code>selectAll</code> to select all cells.
   * 
   * @param vertices
   *          Boolean indicating if vertices should be selected.
   * @param edges
   *          Boolean indicating if edges should be selected.
   * @param parent
   *          Optional cell that acts as the root of the recursion. Default is
   *          <code>defaultParent</code>.
   */
  public void selectCells(final boolean vertices, final boolean edges,
      Object parent) {
    if (parent == null) {
      parent = getDefaultParent();
    }

    Collection<Object> cells = mxGraphModel.filterDescendants(getModel(),
        new mxGraphModel.Filter() {

          /**
                   * 
                   */
          public boolean filter(Object cell) {
            return view.getState(cell) != null
            // && model.getChildCount(cell) == 0
                && ((model.isVertex(cell) && vertices) || (model.isEdge(cell) && edges));
          }

        });
    setSelectionCells(cells);
  }

  /**
   * Hook method that creates the new edge for insertEdge. This implementation
   * does not set the source and target of the edge, these are set when the edge
   * is added to the model.
   * 
   * @param parent
   *          Cell that specifies the parent of the new edge.
   * @param id
   *          Optional string that defines the Id of the new edge.
   * @param value
   *          Object to be used as the user object.
   * @param source
   *          Cell that defines the source of the edge.
   * @param target
   *          Cell that defines the target of the edge.
   * @param style
   *          Optional string that defines the cell style.
   * @return Returns the new edge to be inserted.
   */
  public Object createEdge(Object parent, String id, Object value,
      Object source, Object target, String style) {
    mxCell edge = new mxCell(value, new mxGeometry(), style);
    DesignerSettings set = DesignerSettings.getInstance();
    String s = mxUtils.setStyle(edge.getStyle(), mxConstants.STYLE_FONTCOLOR,
        set.getLineFontColor());
    s = mxUtils.setStyle(s, mxConstants.STYLE_FONTSIZE, set.getLineFontSize());
    s = mxUtils
        .setStyle(s, mxConstants.STYLE_FONTFAMILY, set.getLineFontType());
    s = mxUtils.setStyle(s, mxConstants.STYLE_STROKECOLOR, set.getLineColor());
    s = mxUtils.setStyle(s, mxConstants.STYLE_ENDARROW, set.getLineendType());
    s = mxUtils.setStyle(s, mxConstants.STYLE_ENDSIZE, set.getLineendSize()
        + "");
    s = mxUtils.setStyle(s, mxConstants.STYLE_STROKEWIDTH, set.getLineWidth()
        + "");
    edge.setStyle(s);

    edge.setId(id);
    edge.setEdge(true);
    edge.getGeometry().setRelative(true);

    return edge;
  }
  
  /**
   * Returns the cells which are movable in the given array of cells.
   */
  public Object[] getDeletableCells(Object[] cells)
  {
    Object[] filteredObjects = mxGraphModel.filterCells(cells, new Filter()
      {
          public boolean filter(Object cell)
          {
              return isCellDeletable(cell);
          }
      });
    
    ArrayList<Object> deletable =  new ArrayList<Object>();
    
    for(Object obj : filteredObjects)
    {
      try{
        mxCell cell = (mxCell) obj;
        if(!cell.isEdge())
        {
          DuellGroupCell group = (DuellGroupCell) cell.getParent();
          // do not delete lonely sub group elements
          continue;
        }
      }
      catch(Exception e)
      {
        //do nothing
      }
      
      deletable.add(obj);
    }    
    return deletable.toArray();
  }
}
