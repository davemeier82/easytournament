package com.easytournament.designer.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.gui.jgraph.EditorRuler;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;


public class GraphActions {

  public static void toggleRuler() {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();

    if (graphComponent.getColumnHeader() != null) {
      graphComponent.setColumnHeader(null);
      graphComponent.setRowHeader(null);
    }
    else {
      graphComponent.setColumnHeaderView(new EditorRuler(graphComponent,
          EditorRuler.ORIENTATION_HORIZONTAL));
      graphComponent.setRowHeaderView(new EditorRuler(graphComponent,
          EditorRuler.ORIENTATION_VERTICAL));
    }
  }
  
  public static void setRulerVisible(boolean visible){
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();

    if (!visible && graphComponent.getColumnHeader() != null) {
      graphComponent.setColumnHeader(null);
      graphComponent.setRowHeader(null);
    }
    else if(visible && graphComponent.getColumnHeader() == null){
      graphComponent.setColumnHeaderView(new EditorRuler(graphComponent,
          EditorRuler.ORIENTATION_HORIZONTAL));
      graphComponent.setRowHeaderView(new EditorRuler(graphComponent,
          EditorRuler.ORIENTATION_VERTICAL));
    }
  }

  public static void toggleGrid() {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    mxGraph graph = graphComponent.getGraph();
    boolean enabled = !graph.isGridEnabled();

    graph.setGridEnabled(enabled);
    graphComponent.setGridVisible(enabled);
    graphComponent.repaint();
  }
  
  public static void setGridVisible(boolean visible){
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    mxGraph graph = graphComponent.getGraph();
    graph.setGridEnabled(visible);
    graphComponent.setGridVisible(visible);
    graphComponent.repaint();
  }

  public static void setGridStyle(int style) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    graphComponent.setGridStyle(style);
    graphComponent.repaint();
  }

  public static void setGridColor(Color newColor) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();

    if (newColor == null)
      newColor = JColorChooser.showDialog(graphComponent, ResourceManager.getText(Text.GRID_COLOR_MENU),
          graphComponent.getGridColor());

    if (newColor != null) {
      graphComponent.setGridColor(newColor);
      graphComponent.repaint();
    }
  }

  public static void setZoomPolicy(int zoomPolicy) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    graphComponent.setPageVisible(true);
    graphComponent.setZoomPolicy(zoomPolicy);
  }

  public static void setZoomIn() {
    TournamentViewer.getGraphComponent().zoomIn();
  }

  public static void setZoomOut() {
    TournamentViewer.getGraphComponent().zoomOut();
  }

  public static void setZoomActual() {
    TournamentViewer.getGraphComponent().zoomActual();
  }

  public static void setZoom(double scale) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    if (scale == 0) {
      String value = (String)JOptionPane.showInputDialog(graphComponent,
          ResourceManager.getText(Text.VALUE),
          ResourceManager.getText(Text.ZOOM_MENU) + " (%)", JOptionPane.PLAIN_MESSAGE, null,
          null, "");

      if (value != null) {
        scale = Double.parseDouble(value.replace("%", "")) / 100;
      }
    }

    if (scale > 0) {
      graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
    }
  }

  public static void setPageSetup() {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    PrinterJob pj = PrinterJob.getPrinterJob();
    PageFormat format = pj.pageDialog(graphComponent.getPageFormat());

    if (format != null) {
      graphComponent.setPageFormat(format);
      graphComponent.zoomAndCenter();
    }
  }

  public static void print() {
    PrinterJob pj = PrinterJob.getPrinterJob();
    pj.setJobName(ResourceManager.getText(Text.TOURNAMENT));

    if (pj.printDialog()) {
      mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
      PageFormat pf = graphComponent.getPageFormat();
      Paper paper = new Paper();
      double margin = 36;
      paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2,
          paper.getHeight() - margin * 2);
      pf.setPaper(paper);
      pj.setPrintable(graphComponent, pf);

      try {
        pj.print();
      }
      catch (PrinterException e) {
        ErrorLogger.getLogger().throwing("GraphActions", "print", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
  }

  public static void setLabelAlignment(String labelPosition, String alignment) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (graph != null && !graph.isSelectionEmpty()) {
      graph.getModel().beginUpdate();
      try {
        // Checks the orientation of the alignment to use the correct constants
        if (labelPosition.equals(mxConstants.ALIGN_LEFT)
            || labelPosition.equals(mxConstants.ALIGN_CENTER)
            || labelPosition.equals(mxConstants.ALIGN_RIGHT)) {
          graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, labelPosition);
          graph.setCellStyles(mxConstants.STYLE_ALIGN, alignment);
        }
        else {
          graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION,
              labelPosition);
          graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, alignment);
        }
      }
      finally {
        graph.getModel().endUpdate();
      }
    }
  }

  public static void setCellAlignment(String align) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (graph != null && !graph.isSelectionEmpty()) {
      graph.alignCells(align);
    }
  }

  public static void setCellColor(String name, String key) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    mxGraph graph = graphComponent.getGraph();

    if (!graph.isSelectionEmpty()) {
      Color newColor = JColorChooser.showDialog(graphComponent, name, null);

      if (newColor != null) {
        graph.setCellStyles(key, mxUtils.hexString(newColor));
        if (key == mxConstants.STYLE_FILLCOLOR) {
          graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR,
              mxUtils.hexString(newColor));
        }
      }
    }
  }

  public static void setFontColor() {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    mxGraph graph = graphComponent.getGraph();
    if (!graph.isSelectionEmpty()) {
      Color newColor = JColorChooser.showDialog(graphComponent, ResourceManager.getText(Text.FONTCOLOR_MENU),
          null);

      if (newColor != null) {
        graph.setCellStyles(mxConstants.STYLE_FONTCOLOR,
            mxUtils.hexString(newColor));
      }
    }
  }

  public static void setLineColor() {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    mxGraph graph = graphComponent.getGraph();
    if (!graph.isSelectionEmpty()) {
      Color newColor = JColorChooser.showDialog(graphComponent, ResourceManager.getText(Text.LINE_COLOR_MENU),
          null);

      if (newColor != null) {
        graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
            mxUtils.hexString(newColor));
      }
    }
  }

  public static void setPageColor(Color newColor) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    if(newColor == null)
      newColor = JColorChooser.showDialog(graphComponent,
          ResourceManager.getText(Text.DIAG_FILLCOLOR_MENU), graphComponent.getViewport().getBackground());

    if (newColor != null) {
      graphComponent.getViewport().setOpaque(true);
      graphComponent.getViewport().setBackground(newColor);
    }

    // Forces a repaint of the outline
    graphComponent.getGraph().repaint();
  }

  public static void setPageBackgroundColor(Color newColor) {
    mxGraphComponent graphComponent = TournamentViewer.getGraphComponent();
    if(newColor == null)
      newColor = JColorChooser.showDialog(graphComponent,
          mxResources.get(ResourceManager.getText(Text.DIAG_PAGE_BACKCOLOR_MENU)), null);

    if (newColor != null) {
      graphComponent.setPageBackgroundColor(newColor);
    }

    // Forces a repaint of the component
    graphComponent.repaint();
  }

  public static void cut() {
    if (!TournamentViewer.getGraphComponent().getGraph().isSelectionEmpty()) {
      Action a = TransferHandler.getCutAction();
      a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(),
          1, ""));
    }
  }

  public static void paste() {
    Action a = TransferHandler.getPasteAction();
    a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(), 1,
        ""));
  }

  public static void copy() {
    if (!TournamentViewer.getGraphComponent().getGraph().isSelectionEmpty()) {
      Action a = TransferHandler.getCopyAction();
      a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(),
          1, ""));
    }
  }

  public static void delete() {
    if (!TournamentViewer.getGraphComponent().getGraph().isSelectionEmpty()) {
      Action a = mxGraphActions.getDeleteAction();
      a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(),
          1, ""));
    }
  }

  public static void selectall() {
    Action a = mxGraphActions.getSelectAllAction();
    a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(), 1,
        ""));
  }

  public static void newGraph(TournamentViewer tviewer) {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    mxGraph graph = gcomp.getGraph();
    tviewer.getUndoManager().clear();

    // Check modified flag and display save dialog
    mxCell root = new mxCell();
    root.insert(new mxCell());
    graph.getModel().setRoot(root);

    tviewer.setModified(false);

    PrinterJob pj = PrinterJob.getPrinterJob();
    gcomp.setPageFormat(pj.defaultPage());
    gcomp.getPageFormat().setOrientation(PageFormat.LANDSCAPE);
    gcomp.zoomAndCenter();
  }

  public static void setCellOpacity() {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    String value = (String)JOptionPane.showInputDialog(gcomp, ResourceManager.getText(Text.VALUE),
        ResourceManager.getText(Text.OPACITY_MENU)+" (0-100)", JOptionPane.PLAIN_MESSAGE,
        null, null, "");
    setCellStyle(mxConstants.STYLE_OPACITY, value);
  }

  public static void setLineWidth() {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    String value = (String)JOptionPane.showInputDialog(gcomp, ResourceManager.getText(Text.VALUE),
        ResourceManager.getText(Text.LINE_WIDTH_MENU), JOptionPane.PLAIN_MESSAGE,
        null, null, "");
    setCellStyle(mxConstants.STYLE_STROKEWIDTH, value);
  }

  public static void setLineEndSize() {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    String value = (String)JOptionPane.showInputDialog(gcomp, ResourceManager.getText(Text.VALUE),
        ResourceManager.getText(Text.END_SIZE_MENU), JOptionPane.PLAIN_MESSAGE,
        null, null, "");
    setCellStyle(mxConstants.STYLE_ENDSIZE, value);
  }

  public static void setCellStyle(String key, String value) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (graph != null && !graph.isSelectionEmpty()) {
      if (value != null && value.equals(mxConstants.NONE))
        value = null;

      graph.setCellStyles(key, value);
    }
  }

  public static void setCellStyle(String value) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (graph != null && !graph.isSelectionEmpty()) {
      graph.setCellStyle(value);
    }
  }

  public static void toggleCellSytle(String key, boolean defaultValue) {
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (graph != null) {
      graph.toggleCellStyles(key, defaultValue);
    }
  }

  public static void toFront() {
    if (!TournamentViewer.getGraphComponent().getGraph().isSelectionEmpty()) {
      Action a = mxGraphActions.getToFrontAction();
      a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(),
          1, ""));
    }
  }

  public static void toBack() {
    if (!TournamentViewer.getGraphComponent().getGraph().isSelectionEmpty()) {
      Action a = mxGraphActions.getToBackAction();
      a.actionPerformed(new ActionEvent(TournamentViewer.getGraphComponent(),
          1, ""));
    }
  }

  public static void setGridSize(int value) {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    mxGraph graph = gcomp.getGraph();
    try {
      int size = graph.getGridSize();

      if (value < 0) {
        String v = (String)JOptionPane.showInputDialog(gcomp, ResourceManager.getText(Text.VALUE),
            ResourceManager.getText(Text.GRID_SIZE_MENU), JOptionPane.PLAIN_MESSAGE, null, null, size);
        value = Integer.parseInt(v);
      }
      graph.setGridSize(value);

    }
    catch (Exception ex) { // TODO message
    }
  }

}
