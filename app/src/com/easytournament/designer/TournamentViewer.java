package com.easytournament.designer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PrinterJob;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.gui.dialog.GroupDialog;
import com.easytournament.designer.gui.jgraph.DuellGroupCell;
import com.easytournament.designer.gui.jgraph.EditorPalette;
import com.easytournament.designer.gui.jgraph.EditorPopupMenu;
import com.easytournament.designer.gui.jgraph.GraphView;
import com.easytournament.designer.gui.jgraph.GroupCell;
import com.easytournament.designer.gui.jgraph.TGraph;
import com.easytournament.designer.gui.jgraph.TGraphComponent;
import com.easytournament.designer.model.dialog.GroupDialogPModel;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.DuellGroup;
import com.easytournament.designer.valueholder.Group;
import com.easytournament.designer.valueholder.GroupType;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class TournamentViewer extends JPanel {

  public static final String GROUP_IDENTIFIER = "group1";
  public static final String TEAM_IDENTIFIER = "team";
  private static final long serialVersionUID = 1L;
  public static final String PROPERTY_TEAMVIEW = "teamView";
  private static mxGraphComponent graphComponent;
  protected mxGraphOutline graphOutline;
  protected mxUndoManager undoManager;
  protected JTabbedPane libraryPane;
  protected JLabel statusBar;
  protected mxRubberband rubberband;
  protected EditorPalette shapesPalette;
  protected boolean teamView = false;
  /**
   * Flag indicating whether the current graph has been modified
   */
  protected boolean modified = false;
  /**
     * 
     */
  protected mxIEventListener undoHandler = new mxIEventListener() {
    public void invoke(Object source, mxEventObject evt) {
      undoManager.undoableEditHappened((mxUndoableEdit)evt.getProperty("edit"));
    }
  };

  /**
     * 
     */
  protected mxIEventListener changeTracker = new mxIEventListener() {
    public void invoke(Object source, mxEventObject evt) {
      setModified(true);
    }
  };

  public TournamentViewer() {
    init();
  }

  private void init() {
    this.setLayout(new BorderLayout());

    // Stores a reference to the graph and creates the command history
    final mxGraph graph = new TGraph();

    graphComponent = new TGraphComponent(graph);
    PrinterJob pj = PrinterJob.getPrinterJob();
    graphComponent.setPageFormat(pj.defaultPage());
    undoManager = createUndoManager();

    // Do not change the scale and translation after files have been loaded
    graph.setResetViewOnRootChange(false);

    // Updates the modified flag if the graph model changes
    graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

    // Adds the command history to the model and view
    graph.getModel().addListener(mxEvent.UNDO, undoHandler);
    graph.getView().addListener(mxEvent.UNDO, undoHandler);

    // Keeps the selection in sync with the command history
    mxIEventListener undoHandler = new mxIEventListener() {
      public void invoke(Object source, mxEventObject evt) {
        List<mxUndoableChange> changes = ((mxUndoableEdit)evt
            .getProperty("edit")).getChanges();
        graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
      }
    };

    undoManager.addListener(mxEvent.UNDO, undoHandler);
    undoManager.addListener(mxEvent.REDO, undoHandler);

    // Creates the graph outline component
    graphOutline = new mxGraphOutline(graphComponent);

    // Creates the library pane that contains the tabs with the palettes
    libraryPane = new JTabbedPane();

    // Creates the inner split pane that contains the library with the
    // palettes and the graph outline on the left side of the window
    JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT, libraryPane,
        graphOutline);
    inner.setDividerLocation(320);
    inner.setResizeWeight(1);
    inner.setDividerSize(6);
    inner.setBorder(null);

    // Creates the outer split pane that contains the inner split pane and
    // the graph component on the right side of the window
    JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inner,
        graphComponent);
    outer.setOneTouchExpandable(true);
    outer.setDividerLocation(200);
    outer.setDividerSize(6);
    outer.setBorder(null);

    outer.setPreferredSize(new Dimension(200, 300));
    outer.setMaximumSize(new Dimension(200, 300));
    outer.setSize(new Dimension(200, 300));

    // Creates the status bar
    statusBar = createStatusBar();

    // Puts everything together
    add(outer, BorderLayout.CENTER);
    add(statusBar, BorderLayout.SOUTH);
    status(" "); // without this the statusbar has no "size" at the beginning

    installHandlers();
    installListeners();

    // Creates the shapes palette
    shapesPalette = insertPalette(ResourceManager.getText(Text.GROUPS) + " ");

    graph.setView(new GraphView(graph) {
      public void updateVertexLabelOffset(mxCellState state) {
        super.updateVertexLabelOffset(state);
        try {
          mxCell cell = (mxCell)state.getCell();
          if (cell.getStyle() != null && cell.getStyle().contains(TEAM_IDENTIFIER)
              && cell.getGeometry().isRelative()) {
            Object parent = this.graph.getModel().getParent(cell);
            mxCellState parentState = this.getState(parent);
            if (parentState != null) {
              DuellGroupCell gc = (DuellGroupCell)parent;

              double posStartY = gc.getTitleHeigth()
                  / gc.getGeometry().getHeight();

              if (parent instanceof GroupCell) {
                double height = (gc.getGeometry().getHeight() * (1.0 - posStartY))
                    / gc.getGroup().getNumPositions();
                state.setY(parentState.getY()
                    + (gc.getTitleHeigth() + gc.getIndex(cell) * height)
                    * graph.getView().getScale());
                state.setHeight((parentState.getHeight() - gc.getTitleHeigth()
                    * graph.getView().getScale())
                    / gc.getGroup().getNumPositions());
                state.setWidth(parentState.getWidth());
              }
              else {
                state.setY(parentState.getY() + gc.getTitleHeigth()
                    * graph.getView().getScale());
                state.setWidth(parentState.getWidth() / 2.0);
                state.setHeight(parentState.getHeight() - gc.getTitleHeigth()
                    * graph.getView().getScale());
              }

            }
          }

        }
        catch (ClassCastException cce) {/* do nothing */}
      }
    });

    // Adds some template cells for dropping into the graph
    shapesPalette.addGroupTemplate(ResourceManager.getText(Text.GROUP),
        ResourceManager.getIcon(Icon.GROUP_PALETTE),
        new Group(ResourceManager.getText(Text.GROUP), 4), GroupType.NORMAL);

    shapesPalette.addGroupTemplate(ResourceManager.getText(Text.DUELLGROUP),
        ResourceManager.getIcon(Icon.DUELLGROUP_PALETTE), new DuellGroup(
            ResourceManager.getText(Text.GROUP), true), GroupType.DUELL);

  }

  protected void installHandlers() {
    rubberband = new mxRubberband(graphComponent);
  }

  public void status(String msg) {
    statusBar.setText(msg);
  }

  protected JLabel createStatusBar() {
    JLabel statusBar = new JLabel(mxResources.get("ready"));
    statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

    return statusBar;
  }

  protected mxUndoManager createUndoManager() {
    return new mxUndoManager();
  }

  public void setModified(boolean modified) {
    boolean oldValue = this.modified;
    this.modified = modified;
    firePropertyChange("modified", oldValue, modified);
  }

  public EditorPalette insertPalette(String title) {
    final EditorPalette palette = new EditorPalette();
    final JScrollPane scrollPane = new JScrollPane(palette);
    scrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    libraryPane.add(title, scrollPane);

    // Updates the widths of the palettes if the container size changes
    libraryPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        int w = scrollPane.getWidth()
            - scrollPane.getVerticalScrollBar().getWidth();
        palette.setPreferredWidth(w);
      }
    });

    return palette;
  }

  protected void installListeners() {
    // Installs mouse wheel listener for zooming
    MouseWheelListener wheelTracker = new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
          TournamentViewer.this.mouseWheelMoved(e);
        }
      }
    };

    // Handles mouse wheel events in the outline and graph component
    graphOutline.addMouseWheelListener(wheelTracker);
    graphComponent.addMouseWheelListener(wheelTracker);

    // Installs the popup menu in the outline
    graphOutline.addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        // Handles context menu on the Mac where the trigger is on mousepressed
        mouseReleased(e);
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showOutlinePopupMenu(e);
        }
      }
    });

    // Installs the popup menu in the graph component
    graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        // Handles context menu on the Mac where the trigger is on mousepressed
        mouseReleased(e);
      }

      public void mouseReleased(MouseEvent e) {
        if (e.getClickCount() > 1 && !e.isConsumed()) {
          try {
            mxCell cell = (mxCell)graphComponent.getCellAt(e.getX(), e.getY());
            if (cell != null) {
              if (cell.getStyle().contains(GROUP_IDENTIFIER)) {
                AbstractGroup g = (AbstractGroup)cell.getValue();
                if(g != null){
                  new GroupDialog(Organizer.getInstance().getMainFrame(), true,
                      new GroupDialogPModel(g));
                }
              }
              else if (cell.getStyle().contains(TEAM_IDENTIFIER)) {
                AbstractGroup g = (AbstractGroup)cell.getParent().getValue();
                if(g != null) {
                  new GroupDialog(Organizer.getInstance().getMainFrame(), true,
                      new GroupDialogPModel(g));
                }
              }
            }
          }
          catch (ClassCastException cce) {/* do nothing */}
          e.consume();
        }
        else if (e.isPopupTrigger()) {
          showGraphPopupMenu(e);
        }
        if (e.getButton() == 2)
          try {
            mxCell cell = (mxCell)graphComponent.getCellAt(e.getX(), e.getY());
            if (cell != null) {
              if (cell.getStyle().contains(GROUP_IDENTIFIER)) {
                for (int i = 0; i < cell.getChildCount(); i++) {
                  cell.getChildAt(i).setValue(i);
                }
              }
            }
          }
          catch (ClassCastException cce) {/* do nothing */}
      }
    });

    // Installs a mouse motion listener to display the mouse location
    graphComponent.getGraphControl().addMouseMotionListener(
        new MouseMotionListener() {

          public void mouseDragged(MouseEvent e) {
            mouseLocationChanged(e);
          }

          public void mouseMoved(MouseEvent e) {
            mouseDragged(e);
          }

        });
  }

  protected void mouseWheelMoved(MouseWheelEvent e) {
    if (e.getWheelRotation() < 0) {
      graphComponent.zoomIn();
    }
    else {
      graphComponent.zoomOut();
    }

    status(mxResources.get("scale") + ": "
        + (int)(100 * graphComponent.getGraph().getView().getScale()) + "%");
  }

  /**
   * 
   */
  protected void showOutlinePopupMenu(MouseEvent e) {
    Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
        graphComponent);
    JCheckBoxMenuItem item = new JCheckBoxMenuItem(
        mxResources.get("magnifyPage"));
    item.setSelected(graphOutline.isFitPage());

    item.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        graphOutline.setFitPage(!graphOutline.isFitPage());
        graphOutline.repaint();
      }
    });

    JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(
        mxResources.get("showLabels"));
    item2.setSelected(graphOutline.isDrawLabels());

    item2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
        graphOutline.repaint();
      }
    });

    JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(
        mxResources.get("buffering"));
    item3.setSelected(graphOutline.isTripleBuffered());

    item3.addActionListener(new ActionListener() {
      /**
           * 
           */
      public void actionPerformed(ActionEvent e) {
        graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
        graphOutline.repaint();
      }
    });

    JPopupMenu menu = new JPopupMenu();
    menu.add(item);
    menu.add(item2);
    menu.add(item3);
    menu.show(graphComponent, pt.x, pt.y);

    e.consume();
  }

  /**
   * 
   */
  protected void showGraphPopupMenu(MouseEvent e) {
    Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
        graphComponent);
    EditorPopupMenu menu = new EditorPopupMenu(TournamentViewer.this, e);
    menu.show(graphComponent, pt.x, pt.y);

    e.consume();
  }

  /**
   * 
   */
  protected void mouseLocationChanged(MouseEvent e) {
    status(e.getX() + ", " + e.getY());
  }

  public static mxGraphComponent getGraphComponent() {
    return graphComponent;
  }

  /**
   * 
   */
  public mxGraphOutline getGraphOutline() {
    return graphOutline;
  }

  /**
   * 
   */
  public JTabbedPane getLibraryPane() {
    return libraryPane;
  }

  /**
   * 
   */
  public mxUndoManager getUndoManager() {
    return undoManager;
  }

  /**
   * 
   * @param name
   * @param action
   * @return a new Action bound to the specified string name
   */
  public Action bind(String name, final Action action) {
    return bind(name, action, null);
  }

  /**
   * 
   * @param name
   * @param action
   * @return a new Action bound to the specified string name and icon
   */
  @SuppressWarnings("serial")
  public Action bind(String name, final Action action, Icon iconUrl) {
    return new AbstractAction(name,
        (iconUrl != null)? ResourceManager.getIcon(iconUrl) : null) {
      public void actionPerformed(ActionEvent e) {
        action.actionPerformed(new ActionEvent(getGraphComponent(), e.getID(),
            e.getActionCommand()));
      }
    };
  }

  public EditorPalette getShapesPalette() {
    return shapesPalette;
  }

  public boolean isTeamView() {
    return teamView;
  }

  public void setTeamView(boolean teamView) {
    this.teamView = teamView;
    mxCell root = (mxCell)graphComponent.getGraph().getModel().getRoot();
    updateLabels(root);
    graphComponent.refresh();
    graphComponent.repaint();
  }

  private void updateLabels(mxICell cell) {
    if (cell instanceof DuellGroupCell) {
      ((DuellGroupCell)cell).updateLabels();
    }
    for (int i = 0; i < cell.getChildCount(); i++)
      updateLabels(cell.getChildAt(i));
  }

  public void refreshLabels() {
    mxCell root = (mxCell)graphComponent.getGraph().getModel().getRoot();
    updateLabels(root);
    graphComponent.refresh();
    graphComponent.repaint();
  }

}
