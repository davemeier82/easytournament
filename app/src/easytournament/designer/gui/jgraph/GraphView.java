package easytournament.designer.gui.jgraph;

import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class GraphView extends mxGraphView {

  public GraphView(mxGraph graph) {
    super(graph);
  }

  /**
   * Updates the label bounds in the given state.
   */
  public void updateLabelBounds(mxCellState state)
  {
      Object cell = state.getCell();
      Map<String, Object> style = state.getStyle();

      if (mxUtils.getString(style, mxConstants.STYLE_OVERFLOW, "").equals(
              "fill"))
      {
          state.setLabelBounds(new mxRectangle(state));
      }
      else if (state.getLabel() != null)
      {
          mxRectangle vertexBounds = (!graph.getModel().isEdge(cell)) ? state
                  : null;
          state.setLabelBounds(getLabelPaintBounds(state.getLabel(),
                  style, graph.isHtmlLabel(cell), state.getAbsoluteOffset(),
                  vertexBounds, scale, cell));
      }
  }
  
  /**
   * extension of the mxUtil.getLabelPaintBounds function
   * 
   * Returns the paint bounds for the given label.
   */
  public static mxRectangle getLabelPaintBounds(String label,
          Map<String, Object> style, boolean isHtml, mxPoint offset,
          mxRectangle vertexBounds, double scale, Object cell)
  {
      mxRectangle size = mxUtils.getLabelSize(label, style, isHtml, scale);

      // Measures font with full scale and scales back
      size.setWidth(size.getWidth() / scale);
      size.setHeight(size.getHeight() / scale);

      double x = offset.getX();
      double y = offset.getY();
      double width = 0;
      double height = 0;

      if (vertexBounds != null)
      {
          x += vertexBounds.getX();
          y += vertexBounds.getY();

          if (mxUtils.getString(style, mxConstants.STYLE_SHAPE, "").equals(
                  mxConstants.SHAPE_SWIMLANE))
          {
              // Limits the label to the swimlane title
              boolean horizontal = mxUtils.isTrue(style,
                      mxConstants.STYLE_HORIZONTAL, true);
              double start = mxUtils.getDouble(style,
                      mxConstants.STYLE_STARTSIZE,
                      mxConstants.DEFAULT_STARTSIZE)
                      * scale;

              if (horizontal)
              {
                  width += vertexBounds.getWidth();
                  height += start;
              }
              else
              {
                  width += start;
                  height += vertexBounds.getHeight();
              }
          }
          else
          {
              width += vertexBounds.getWidth();
              /**
               * changed part
               */
              if(cell instanceof DuellGroupCell){
                height += ((DuellGroupCell) cell).getTitleHeigth()*scale;
              } else {
                height += vertexBounds.getHeight();
              }                
          }
      }

      return mxUtils.getScaledLabelBounds(x, y, size, width, height, style,
              scale);
  }
}
