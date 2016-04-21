package easytournament.designer.gui.jgraph;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

import easytournament.basic.resources.Path;
import easytournament.basic.resources.ResourceManager;
import easytournament.designer.settings.PageSettings;

import com.mxgraph.view.mxGraph;


public class TGraphComponent extends mxGraphComponent {

  private static final long serialVersionUID = 2255701931560878213L;

  public TGraphComponent(mxGraph graph) {
    super(graph);

    // Sets switches typically used in an editor
    setPageVisible(true);
    setGridVisible(true);
    setToolTips(true);
    // getConnectionHandler().setCreateTarget(true);

    // Loads the defalt stylesheet from an external file
    mxCodec codec = new mxCodec();
    Document doc = mxUtils.loadDocument(ResourceManager
        .getPath(Path.DEFAULT_GRAPH_STYLE));
    if(doc != null)
      codec.decode(doc.getDocumentElement(), graph.getStylesheet());

    // Sets the background to white
    getViewport().setOpaque(true);
    getViewport().setBackground(PageSettings.getInstance().getPageColor());
  }


  /**
   * 
   * @param state
   *          Cell state for which a handler should be created.
   * @return Returns the handler to be used for the given cell state.
   */
  public mxCellHandler createHandler(mxCellState state) {
    if (graph.getModel().isVertex(state.getCell())) {
      return new mxVertexHandler(this, state);
    }
    else if (graph.getModel().isEdge(state.getCell())) {
      mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state, null,
          null, null);

      if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
          || style == mxEdgeStyle.SideToSide
          || style == mxEdgeStyle.TopToBottom) {
        return new ElbowEdgeHandler(this, state);
      }

      return new EdgeHandler(this, state);
    }

    return new mxCellHandler(this, state);
  }

  protected mxConnectionHandler createConnectionHandler() {
    return new ConnectionHandler(this);
  }

  protected mxGraphHandler createGraphHandler() {
    return new GraphHandler(this);
  }

}
