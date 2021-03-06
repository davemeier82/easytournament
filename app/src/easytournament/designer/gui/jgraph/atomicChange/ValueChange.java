package easytournament.designer.gui.jgraph.atomicChange;

import java.util.ArrayList;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Rule;
import easytournament.designer.TournamentViewer;
import easytournament.designer.gui.jgraph.DuellGroupCell;
import easytournament.designer.gui.jgraph.EdgeValueHolder;
import easytournament.designer.gui.jgraph.GroupCell;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.Group;


public class ValueChange extends mxAtomicGraphModelChange {

  /**
  *
  */
  protected DuellGroupCell cell;
  protected AbstractGroup value, previous;
  protected ArrayList<EdgeValueHolder> deletedEdges = null;

  /**
  * 
  */
  public ValueChange() {
    this(null, null, null);
  }

  /**
  * 
  */
  public ValueChange(mxGraphModel model, DuellGroupCell cell,
      AbstractGroup oldvalue) {
    super(model);
    this.cell = cell;
    this.value = oldvalue;
    if (oldvalue instanceof Group) {
      Group g = (Group)cell.getGroup();
      this.previous = new Group(g.getName(), g.getNumPositions());
      setSettings(g);
    }
    else {
      DuellGroup g = (DuellGroup)cell.getGroup();
      this.previous = new DuellGroup(g.getName());
      setSettings(g);
    }
  }

  private void setSettings(AbstractGroup g) {
    this.previous.setDefaultRules(g.isDefaultRules());
    this.previous.setDefaultSettings(g.isDefaultSettings());
    if(g.isDefaultRules()){
      this.previous.setRules(null);
    } else {
      try {
        for (Rule r : g.getRules()) {
          this.previous.getRules().add((Rule)r.clone());
        }
      }
      catch (CloneNotSupportedException cce) {
        ErrorLogger.getLogger().throwing("ValueChange", "setSettings", cce);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), cce.toString(), cce);
        ed.setVisible(true);
        cce.printStackTrace();
      }
    }
    if(g.isDefaultSettings()){
      this.previous.setSettings(null);
    } else {
      this.previous.getSettings().setSportSettings(g.getSettings());
    }
  }

  /**
  * 
  */
  public void setCell(DuellGroupCell value) {
    cell = value;
  }

  /**
   * @return the cell
   */
  public Object getCell() {
    return cell;
  }

  /**
  * 
  */
  public void setValue(AbstractGroup value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
  * 
  */
  public void setPrevious(AbstractGroup value) {
    previous = value;
  }

  /**
   * @return the previous
   */
  public Object getPrevious() {
    return previous;
  }

  /**
   * Changes the root of the model.
   */
  public void execute() {

    if (value instanceof Group) {
      ((Group)cell.getGroup()).setGroup((Group)this.previous);
      ArrayList<EdgeValueHolder> tmpDelEdges = null;
      if (previous.getNumPositions() != value.getNumPositions()){
        tmpDelEdges = ((GroupCell)cell).setPostions();
      }
      if(deletedEdges != null){
        ((GroupCell)cell).addEdges(deletedEdges);
      }
      deletedEdges = tmpDelEdges;
    }
    else {
      ((DuellGroup)cell.getGroup()).setGroup((DuellGroup)this.previous);      
    }
    AbstractGroup tmp = this.previous;
    this.previous = this.value;
    this.value = tmp;
    cell.updateLabel();
    TournamentViewer.getGraphComponent().refresh();
    TournamentViewer.getGraphComponent().getGraph().repaint();
  }

}
