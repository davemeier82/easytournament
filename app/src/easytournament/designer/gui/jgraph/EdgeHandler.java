package easytournament.designer.gui.jgraph;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.view.mxCellState;

public class EdgeHandler extends mxEdgeHandler {
	
  public EdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
    super(graphComponent, state);
  }
  
	/**
	 * Returns the error message or an empty string if the connection for the
	 * given source target pair is not valid. Otherwise it returns null.
	 */
    @Override
	public String validateConnection(Object source, Object target)
	{
        //no system out as in super class
		return graphComponent.getGraph().getEdgeValidationError(
				state.getCell(), source, target);
	}
  
}
