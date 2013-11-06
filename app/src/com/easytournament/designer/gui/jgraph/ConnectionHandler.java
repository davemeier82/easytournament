package com.easytournament.designer.gui.jgraph;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class ConnectionHandler extends mxConnectionHandler {

	public ConnectionHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
		marker = new mxCellMarker(graphComponent)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 103433247310526381L;

			// Overrides to return cell at location only if valid (so that
			// there is no highlight for invalid cells that have no error
			// message when the mouse is released)
			protected Object getCell(MouseEvent e)
			{
				Object cell = super.getCell(e);

				if (isConnecting())
				{
					if (source != null)
					{
						error = validateConnection(source.getCell(), cell);

						if (error != null && error.length() == 0)
						{
							// Enables create target inside groups
							if (createTarget)
							{
								error = null;
							}
						}
					}
				}
				else if (!isValidSource(cell))
				{
					cell = null;
				}

				return cell;
			}

			// Sets the highlight color according to isValidConnection
			protected boolean isValidState(mxCellState state)
			{
				if (isConnecting())
				{
					return error == null;
				}
				return super.isValidState(state);					
				
			}

			// Overrides to use marker color only in highlight mode or for
			// target selection
			protected Color getMarkerColor(MouseEvent e, mxCellState state,
					boolean isValid)
			{
				return (isHighlighting() || isConnecting()) ? super
						.getMarkerColor(e, state, isValid) : null;
			}

			// Overrides to use hotspot only for source selection otherwise
			// intersects always returns true when over a cell
			protected boolean intersects(mxCellState state, MouseEvent e)
			{
//				if (!isHighlighting() || isConnecting())
//				{
//					return true;
//				}
//
//				return super.intersects(state, e);

		        if (isHotspotEnabled()) {
		          mxCell cell = (mxCell)state.getCell();

		          if (cell.getStyle() != null && cell.getStyle().contains("team")) {
		            return mxUtils.intersectsHotspot(state, e.getX(), e.getY(), 1.0,
		                mxConstants.MIN_HOTSPOT_SIZE, mxConstants.MAX_HOTSPOT_SIZE);
		          }

		          return mxUtils.intersectsHotspot(state, e.getX(), e.getY(), hotspot,
		              mxConstants.MIN_HOTSPOT_SIZE, mxConstants.MAX_HOTSPOT_SIZE);
		        }

		        return true;
		      }


		};

		marker.setHotspotEnabled(true);
	}

}
