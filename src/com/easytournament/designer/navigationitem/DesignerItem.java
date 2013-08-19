package com.easytournament.designer.navigationitem;

import java.awt.print.PageFormat;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;

import org.jdom.Element;



import com.easytournament.basic.Organizer;
import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.export.ExportRegistry;
import com.easytournament.basic.export.ImportRegistry;
import com.easytournament.basic.gui.MainMenuObservable;
import com.easytournament.basic.model.MainMenuPModel;
import com.easytournament.basic.navigationitem.NavTreeActions;
import com.easytournament.basic.navigationitem.NaviNode;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.settings.SettingsRegistry;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.action.GraphActions;
import com.easytournament.designer.export.PlanExportable;
import com.easytournament.designer.gui.toolbar.DesignerToolBar;
import com.easytournament.designer.importable.PlanImportable;
import com.easytournament.designer.model.DesignerToolBarModel;
import com.easytournament.designer.settings.DesignerSettings;
import com.easytournament.designer.settings.GridSettings;
import com.easytournament.designer.settings.PageSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.xml.DesignerXMLHandler;
import com.easytournament.tournament.calc.Calculator;
import com.jgoodies.common.collect.ArrayListModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;


public class DesignerItem extends NavigationItem implements mxIEventListener {

  private static TournamentViewer tournamentViewer;

  public void init() {
    tournamentViewer = new TournamentViewer();
    tournamentViewer.getUndoManager().addListener(mxEvent.ADD, this);
    tournamentViewer.getUndoManager().addListener(mxEvent.REDO, this);
    tournamentViewer.getUndoManager().addListener(mxEvent.UNDO, this);
    toolbar = new DesignerToolBar(DesignerToolBarModel.getInstance());
    SettingsRegistry.register(DesignerSettings.getInstance());
    ExportRegistry.register(ResourceManager.getText(Text.TOURNAMENT_DIAG),
        new PlanExportable(this));
    ImportRegistry.register(ResourceManager.getText(Text.TOURNAMENT_DIAG),
        new PlanImportable());
    setSettings();
  }

  public void setSettings() {
    GridSettings gset = GridSettings.getInstance();
    GraphActions.setGridStyle(gset.getGridType());
    GraphActions.setGridColor(gset.getGridColor());
    GraphActions.setGridSize(gset.getGridSize());
    GraphActions.setGridVisible(gset.isShowGrid());
    GraphActions.setRulerVisible(gset.isShowRuler());
    PageSettings pset = PageSettings.getInstance();
    GraphActions.setPageColor(pset.getPageColor());
    GraphActions.setPageBackgroundColor(pset.getBackgroundColor());
    TournamentViewer
        .getGraphComponent()
        .getPageFormat()
        .setOrientation(
            PageSettings.getInstance().isPortrait()? PageFormat.PORTRAIT
                : PageFormat.LANDSCAPE);
  }

  public void activate() {
    this.active = true; // we do not call "super" because of menu disabling
    MainMenuPModel.getInstance().enableAllItems();
    ArrayList<MainMenuAction> disable = new ArrayList<MainMenuAction>();
    if (!tournamentViewer.getUndoManager().canUndo())
      disable.add(MainMenuAction.UNDO);
    if (!tournamentViewer.getUndoManager().canRedo())
      disable.add(MainMenuAction.REDO);
    if (disable.size() > 0)
      MainMenuPModel.getInstance().disableItems(disable);
    if (tournamentViewer.isTeamView())
      tournamentViewer.setTeamView(true); // update possible changes
  }

  public boolean deactivate() {
    super.deactivate();
    ArrayListModel<AbstractGroup> groups = Organizer.getInstance()
        .getCurrentTournament().getPlan().getOrderedGroups();
    boolean recalc = false;
    for (AbstractGroup ag : groups) {
      if (ag.isTableOutOfDate()) {
        recalc = true;
        break;
      }
    }
    if (recalc) {
      for (AbstractGroup g : groups) {
        Calculator.calcTableEntries(g, false);
      }
    }
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.DESIGNER), this);
  }

  public JComponent getPanel() {
    return tournamentViewer;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.SAVE.name())) {
      DesignerXMLHandler.save((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      openPlan((Element)evt.getNewValue(), false);
      setSettings();
    }
    else if (evt.getPropertyName().equals(NavTreeActions.NEW.name())) {
      GraphActions.newGraph(tournamentViewer);
      tournamentViewer.getUndoManager().clear();
      setSettings();
      ArrayList<MainMenuAction> disable = new ArrayList<MainMenuAction>();
      disable.add(MainMenuAction.UNDO);
      disable.add(MainMenuAction.REDO);
      MainMenuPModel.getInstance().disableItems(disable);
      Position.CURRENT_MAX_ID = 0;
      AbstractGroup.CURRENT_MAX_ID = 0;
    }
  }

  public static void openPlan(Element xml, boolean importing) {
    GraphActions.newGraph(tournamentViewer);
    DesignerXMLHandler.open(xml, importing);
    clearUndoManager();
  }

  public static void clearUndoManager() {
    tournamentViewer.getUndoManager().clear();
    ArrayList<MainMenuAction> disable = new ArrayList<MainMenuAction>();
    disable.add(MainMenuAction.UNDO);
    disable.add(MainMenuAction.REDO);
    MainMenuPModel.getInstance().disableItems(disable);
  }

  @Override
  public void update(Observable o, Object val) {
    if (o instanceof MainMenuObservable) {
      if (val instanceof MainMenuAction) {
        MainMenuAction action = (MainMenuAction)val;
        switch (action) {
          case CUT:
            GraphActions.cut();
            break;
          case PASTE:
            GraphActions.paste();
            break;
          case COPY:
            GraphActions.copy();
            break;
          case DELETE:
            GraphActions.delete();
            break;
          case SELECTALL:
            GraphActions.selectall();
            break;
          case RULER:
            GraphActions.toggleRuler();
            break;
          case GRID:
            GraphActions.toggleGrid();
            break;
          case ZOOMPAGE:
            GraphActions.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
            break;
          case ZOOMWITH:
            GraphActions.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
            break;
          case ZOOMREAL:
            GraphActions.setZoomActual();
            break;
          case ZOOMIN:
            GraphActions.setZoomIn();
            break;
          case ZOOMOUT:
            GraphActions.setZoomOut();
            break;
          case ZOOM400:
            GraphActions.setZoom(4);
            break;
          case ZOOM200:
            GraphActions.setZoom(2);
            break;
          case ZOOM150:
            GraphActions.setZoom(1.5);
            break;
          case ZOOM100:
            GraphActions.setZoom(1.0);
            break;
          case ZOOM75:
            GraphActions.setZoom(0.75);
            break;
          case ZOOM50:
            GraphActions.setZoom(0.5);
            break;
          case ZOOMSELECT:
            GraphActions.setZoom(0.0);
            break;
          case GRID_CROSS:
            GraphActions.setGridStyle(mxGraphComponent.GRID_STYLE_CROSS);
            break;
          case GRID_DASHED:
            GraphActions.setGridStyle(mxGraphComponent.GRID_STYLE_DASHED);
            break;
          case GRID_LINE:
            GraphActions.setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
            break;
          case GRID_POINT:
            GraphActions.setGridStyle(mxGraphComponent.GRID_STYLE_DOT);
            break;
          case GRID_COLOR:
            GraphActions.setGridColor(null);
            break;
          case PAGESETUP:
            GraphActions.setPageSetup();
            break;
          case PRINT:
            GraphActions.print();
            break;
          case UNDO: {
            tournamentViewer.getUndoManager().undo();
            ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
            ArrayList<MainMenuAction> disable = new ArrayList<MainMenuAction>();
            enable.add(MainMenuAction.REDO);
            if (tournamentViewer.getUndoManager().canUndo())
              enable.add(MainMenuAction.UNDO);
            else
              disable.add(MainMenuAction.UNDO);
            MainMenuPModel.getInstance().enableItems(enable);
            if (disable.size() > 0)
              MainMenuPModel.getInstance().disableItems(disable);
            break;
          }
          case REDO:
            tournamentViewer.getUndoManager().redo();
            ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
            ArrayList<MainMenuAction> disable = new ArrayList<MainMenuAction>();
            enable.add(MainMenuAction.UNDO);
            if (tournamentViewer.getUndoManager().canRedo())
              enable.add(MainMenuAction.REDO);
            else
              disable.add(MainMenuAction.REDO);
            MainMenuPModel.getInstance().enableItems(enable);
            if (disable.size() > 0)
              MainMenuPModel.getInstance().disableItems(disable);
            break;
          case TEXT_ALIGN_LEFT:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_CENTER,
                mxConstants.ALIGN_LEFT);
            break;
          case TEXT_ALIGN_RIGHT:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_CENTER,
                mxConstants.ALIGN_RIGHT);
            break;
          case TEXT_ALIGN_CENTER:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_CENTER,
                mxConstants.ALIGN_CENTER);
            break;
          case TEXT_ALIGN_TOP:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_MIDDLE,
                mxConstants.ALIGN_TOP);
            break;
          case TEXT_ALIGN_MIDDLE:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_MIDDLE,
                mxConstants.ALIGN_MIDDLE);
            break;
          case TEXT_ALIGN_BOTTOM:
            GraphActions.setLabelAlignment(mxConstants.ALIGN_MIDDLE,
                mxConstants.ALIGN_BOTTOM);
            break;
          case SHAPE_ALIGN_LEFT:
            GraphActions.setCellAlignment(mxConstants.ALIGN_LEFT);
            break;
          case SHAPE_ALIGN_RIGHT:
            GraphActions.setCellAlignment(mxConstants.ALIGN_RIGHT);
            break;
          case SHAPE_ALIGN_CENTER:
            GraphActions.setCellAlignment(mxConstants.ALIGN_CENTER);
            break;
          case SHAPE_ALIGN_TOP:
            GraphActions.setCellAlignment(mxConstants.ALIGN_TOP);
            break;
          case SHAPE_ALIGN_MIDDLE:
            GraphActions.setCellAlignment(mxConstants.ALIGN_MIDDLE);
            break;
          case SHAPE_ALIGN_BOTTOM:
            GraphActions.setCellAlignment(mxConstants.ALIGN_BOTTOM);
            break;
          case CELL_FILLCOLOR:
            GraphActions.setCellColor(
                ResourceManager.getText(Text.FILLCOLOR_MENU),
                mxConstants.STYLE_FILLCOLOR);
            break;
          case CELL_COLORGRADIENT:
            GraphActions.setCellColor(
                ResourceManager.getText(Text.COLORGRADIENT_MENU),
                mxConstants.STYLE_GRADIENTCOLOR);
            break;
          case DIAG_FILLCOLOR:
            GraphActions.setPageColor(null);
            break;
          case DIAG_PAGE_BACKCOLOR:
            GraphActions.setPageBackgroundColor(null);
            break;
          case CELL_OPACITY:
            GraphActions.setCellOpacity();
            break;
          case FONT_COLOR:
            GraphActions.setFontColor();
            break;
          case LINE_COLOR:
            GraphActions.setLineColor();
            break;
          case LINE_DASHED:
            GraphActions.toggleCellSytle(mxConstants.STYLE_DASHED, false);
            break;
          case LINE_WIDTH:
            GraphActions.setLineWidth();
            break;
          case CON_STRAIGHT:
            GraphActions.setCellStyle("straight");
            break;
          case CON_HORIZ:
            GraphActions.setCellStyle("");
            break;
          case CON_VERT:
            GraphActions.setCellStyle("vertical");
            break;
          case CON_ARROW:
            GraphActions.setCellStyle("arrow");
            break;
          case END_BLOCK:
            GraphActions.setCellStyle(mxConstants.STYLE_ENDARROW,
                mxConstants.ARROW_BLOCK);
            break;
          case END_OPEN:
            GraphActions.setCellStyle(mxConstants.STYLE_ENDARROW,
                mxConstants.ARROW_OPEN);
            break;
          case END_CLASSIC:
            GraphActions.setCellStyle(mxConstants.STYLE_ENDARROW,
                mxConstants.ARROW_CLASSIC);
            break;
          case END_SIZE:
            GraphActions.setLineEndSize();
            break;
          case SHAPE_DOWN:
            GraphActions.toBack();
            break;
          case SHAPE_UP:
            GraphActions.toFront();
            break;
          case GRID_SIZE:
            GraphActions.setGridSize(-1);
            break;
          default:
        }
      }
    }

  }

  @Override
  public void invoke(Object sender, mxEventObject evt) {
    if (active) {
      if (evt.getName().equals(mxEvent.ADD)) {
        ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
        enable.add(MainMenuAction.UNDO);
        MainMenuPModel.getInstance().enableItems(enable);
      }
      Organizer.getInstance().setSaved(false);
      tournamentViewer.refreshLabels();
    }    
  }

  public static TournamentViewer getTournamentViewer() {
    return tournamentViewer;
  }

}
