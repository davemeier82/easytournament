package com.easytournament.designer.gui.jgraph;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.gui.jgraph.atomicChange.TitleHightChange;
import com.easytournament.designer.gui.jgraph.atomicChange.ValueChange;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.TournamentPlan;
import com.easytournament.tournament.calc.Calculator;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;


public class GraphModel extends mxGraphModel {

  private static final long serialVersionUID = -2208493213329777891L;

  @Override
  public boolean isCreateIds() {
    return true;
  }

  /*
   * for AbstractGroup we have to pass the old instead of the new value
   * 
   * @see com.mxgraph.model.mxGraphModel#setValue(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public Object setValue(Object cell, Object value) {
    try {
      AbstractGroup g = (AbstractGroup)value;
      execute(new ValueChange(this, (DuellGroupCell)cell, g));
    }
    catch (ClassCastException cce) {
      return super.setValue(cell, value);
    }
    return ((mxICell)cell).getValue();
  }

  public void setTitleHeight(DuellGroupCell cell, double oldvalue) {
    execute(new TitleHightChange(this, cell, oldvalue));
  }

  @Override
  protected Object terminalForCellChanged(Object edge, Object terminal,
      boolean isSource) {
    mxICell previous = (mxICell)getTerminal(edge, isSource);
    mxICell adjTerm = (mxICell)getTerminal(edge, !isSource);

    if (terminal != null) {
      mxICell term = (mxICell)terminal;
      term.insertEdge((mxICell)edge, isSource);

      if (adjTerm != null && adjTerm.getParent() != null
          && term.getParent() != null) {
        try {

          DuellGroupCell dgc = (DuellGroupCell)term.getParent();
          int termIdx = dgc.getIndex(term);

          DuellGroupCell adjacent = (DuellGroupCell)adjTerm.getParent();
          int idx = adjacent.getIndex(adjTerm);

          Position newPos = dgc.getGroup().getPosition(termIdx);
          Position adjPos = adjacent.getGroup().getPositions().get(idx);

          if (previous != null && previous.getParent() != null) {
            DuellGroupCell prevGroupCell = (DuellGroupCell)previous.getParent();
            int pidx = prevGroupCell.getIndex(previous);
            Position prevPos = prevGroupCell.getGroup().getPosition(pidx);

            if (isSource) {
              prevPos.removeNext(adjPos);
              adjPos.setPrev(null);
              Calculator.calcTableEntries(adjPos.getGroup(), true);
            }
            else {
              if(prevPos.getTeam() != null) {
                Team t = prevPos.getTeam();
                prevPos.getGroup().getTeams().remove(t);
                prevPos.setTeam(null);
                if(prevPos.getPrev() == null)
                 Organizer.getInstance().getCurrentTournament().getUnassignedteams().add(t);
                
              }
              prevPos.setPrev(null);
              adjPos.removeNext(prevPos);
              Calculator.calcTableEntries(prevPos.getGroup(), true);
            }
          }
          if (isSource) {
            newPos.addNext(adjPos);
            adjPos.setPrev(newPos);
            Calculator.calcTableEntries(newPos.getGroup(), true);
          }
          else {
            if(newPos.getTeam() != null) {
              Team t = newPos.getTeam();
              newPos.getGroup().getTeams().remove(t);
              newPos.setTeam(null);
              if(newPos.getPrev() == null)
                Organizer.getInstance().getCurrentTournament().getUnassignedteams().add(t);
              
            }
            newPos.setPrev(adjPos);
            adjPos.getNext().add(newPos);
            Calculator.calcTableEntries(adjPos.getGroup(), true);
          }
        }
        catch (ClassCastException cce) {/* do nothing */}
      }
    }
    else if (previous != null) {
      previous.removeEdge((mxICell)edge, isSource);
      if (adjTerm != null && adjTerm.getParent() != null
          && previous.getParent() != null) {
        try {

          DuellGroupCell adjacent = (DuellGroupCell)adjTerm.getParent();
          int idx = adjacent.getIndex(adjTerm);

          Position adjPos = adjacent.getGroup().getPositions().get(idx);

          DuellGroupCell prevGroupCell = (DuellGroupCell)previous.getParent();
          int pidx = prevGroupCell.getIndex(previous);
          Position prevPos = prevGroupCell.getGroup().getPosition(pidx);

          if (isSource) {
            if(adjPos.getTeam() != null) {
              Team t = adjPos.getTeam();
              adjPos.getGroup().getTeams().remove(t);
              adjPos.setTeam(null);
              if(adjPos.getPrev() == null)
                Organizer.getInstance().getCurrentTournament().getUnassignedteams().add(t);
              
            }
            prevPos.removeNext(adjPos);
            adjPos.setPrev(null);
            Calculator.calcTableEntries(adjPos.getGroup(), true);
          }
          else {
            if(prevPos.getTeam() != null) {
              Team t = prevPos.getTeam();
              prevPos.getGroup().getTeams().remove(t);
              if(prevPos.getPrev() == null)
                Organizer.getInstance().getCurrentTournament().getUnassignedteams().add(t);
              
            }
            prevPos.setPrev(null);
            adjPos.removeNext(prevPos);
            Calculator.calcTableEntries(prevPos.getGroup(), true);
          }

        }
        catch (ClassCastException cce) {/* do nothing */}
      }
    }

    return previous;
  }

  @Override
  protected Object parentForCellChanged(Object cell, Object parent, int index) {
    
    if (cell instanceof DuellGroupCell) {
      DuellGroupCell dgc = (DuellGroupCell)cell;
      Tournament t = Organizer.getInstance().getCurrentTournament();
      TournamentPlan pl = t.getPlan();
      if (parent == null) {
        for(Position p : dgc.getGroup().getPositions()){
          if(p.getPrev() == null && p.getTeam() != null)
            t.getUnassignedteams().add(p.getTeam());
        }
        pl.removeGroup(dgc.getGroup());
      }
      else {
        if (!pl.getGroups().contains(dgc.getGroup()))
          pl.addGroup(dgc.getGroup());
      }
    }

    return super.parentForCellChanged(cell, parent, index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.mxgraph.model.mxIGraphModel#setTerminal(Object, Object, boolean)
   */
  @Override
  public Object setTerminal(Object edge, Object terminal, boolean isSource) {
    boolean terminalChanged = terminal != getTerminal(edge, isSource);
    execute(new TerminalChange(this, edge, terminal, isSource));

    if (maintainEdgeParent && terminalChanged) {
      updateEdgeParent(edge, getRoot());
    }

    return terminal;
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.mxgraph.model.mxIGraphModel#add(Object, Object, int)
   */
  @Override
  public Object add(Object parent, Object child, int index) {
    if (child != parent && parent != null && child != null) {
      boolean parentChanged = parent != getParent(child);
      execute(new ChildChange(this, parent, child, index));

      // Maintains the edges parents by moving the edges
      // into the nearest common ancestor of its
      // terminals
      if (maintainEdgeParent && parentChanged) {
        updateEdgeParents(child);
      }
    }

    return child;
  }

  public class TerminalChange extends mxTerminalChange {

    protected DuellGroupCell group;
    protected int posIndex;

    public TerminalChange(mxGraphModel arg0, Object arg1, Object arg2,
        boolean arg3) {
      super(arg0, arg1, arg2, arg3);
    }

    @Override
    public void execute() {
      DuellGroupCell tmpDgc = null;
      int tmpIdx = -1;
      try {
        mxCell edge = (mxCell)cell;
        if (edge.getTerminal(this.source) != null) {
          mxICell term = edge.getTerminal(this.source);
          if (term.getParent() != null) {
            tmpDgc = (DuellGroupCell)term.getParent();
            tmpIdx = tmpDgc.getIndex(term);
          }
        }
      }
      catch (ClassCastException cce) {
        ErrorLogger.getLogger().throwing("TerminalChange", "execute", cce);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), cce.toString(), cce);
        ed.setVisible(true);
        cce.printStackTrace();
      }
      if (this.group == null) {
        super.execute();
      }
      else {
        terminal = previous;
        previous = ((GraphModel)model).terminalForCellChanged(cell,
            group.getChildAt(posIndex), source);
      }
      this.group = tmpDgc;
      this.posIndex = tmpIdx;
    }
  }

  public class ChildChange extends mxChildChange {
    protected DuellGroupCell sGroup;
    protected DuellGroupCell tGroup;
    protected int sPosIndex;
    protected int tPosIndex;

    public ChildChange(mxGraphModel arg0, Object arg1, Object arg2, int arg3) {
      super(arg0, arg1, arg2, arg3);
    }

    public ChildChange(mxGraphModel arg0, Object arg1, Object arg2) {
      super(arg0, arg1, arg2);
    }

    @Override
    protected void connect(Object cell, boolean isConnect) {
      Object source = getTerminal(cell, true);
      Object target = getTerminal(cell, false);

      DuellGroupCell sTmpDgc = null;
      DuellGroupCell tTmpDgc = null;
      int sTmpIdx = -1;
      int tTmpIdx = -1;
      try {
        if (source != null) {
          mxICell term = (mxICell)source;
          if (term.getParent() != null) {
            sTmpDgc = (DuellGroupCell)term.getParent();
            sTmpIdx = sTmpDgc.getIndex(term);
          }
        }
        if (target != null) {
          mxICell term = (mxICell)target;
          if (term.getParent() != null) {
            tTmpDgc = (DuellGroupCell)term.getParent();
            tTmpIdx = tTmpDgc.getIndex(term);
          }
        }
      }
      catch (ClassCastException cce) {
        ErrorLogger.getLogger().throwing("ChildChange", "connect", cce);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), cce.toString(), cce);
        ed.setVisible(true);
        cce.printStackTrace();
      }

      if (source != null) {
        if (isConnect) {
          if (sGroup == null)
            ((GraphModel)model).terminalForCellChanged(cell, source, true);
          else {

            ((GraphModel)model).terminalForCellChanged(cell,
                sGroup.getChildAt(sPosIndex), true);
          }
        }
        else {
          ((GraphModel)model).terminalForCellChanged(cell, null, true);
        }
      }

      if (target != null) {
        if (isConnect) {
          if (tGroup == null)
            ((GraphModel)model).terminalForCellChanged(cell, target, false);
          else {
            ((GraphModel)model).terminalForCellChanged(cell,
                tGroup.getChildAt(tPosIndex), false);
          }
        }
        else {
          ((GraphModel)model).terminalForCellChanged(cell, null, false);
        }
      }

      // Stores the previous terminals in the edge
      if (sGroup == null)
        setTerminal(cell, source, true);
      else
        setTerminal(cell, sGroup.getChildAt(sPosIndex), true);

      if (tGroup == null)
        setTerminal(cell, target, false);
      else
        setTerminal(cell, tGroup.getChildAt(tPosIndex), false);

      int childCount = model.getChildCount(cell);

      for (int i = 0; i < childCount; i++) {
        connect(model.getChildAt(cell, i), isConnect);
      }

      this.sGroup = sTmpDgc;
      this.tGroup = tTmpDgc;
      this.sPosIndex = sTmpIdx;
      this.tPosIndex = tTmpIdx;
    }

  }
}
