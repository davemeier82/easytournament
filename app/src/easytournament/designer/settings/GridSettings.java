package easytournament.designer.settings;

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import org.jdom.Element;

import com.mxgraph.swing.mxGraphComponent;

import easytournament.basic.model.dialog.SettingsDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.Settings;
import easytournament.basic.settings.SettingsTreeNode;
import easytournament.designer.gui.settings.GridSettingsPanel;
import easytournament.designer.model.settings.GridSetPModel;
import easytournament.designer.util.comperator.Utils;


public class GridSettings implements Settings {

  private GridSetPModel pm;
  private GridSettingsPanel panel;
  private static GridSettings instance;

  protected boolean showGrid;
  protected boolean showRuler;
  protected int gridSize;
  protected Color gridColor;
  protected int gridType;

  private GridSettings() {}

  public static GridSettings getInstance() {
    if (instance == null)
      instance = new GridSettings();
    return instance;
  }

  private void init() {
    showGrid = true;
    showRuler = false;
    gridSize = 10;
    gridColor = Color.GRAY;
    gridType = mxGraphComponent.GRID_STYLE_DOT;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (pm != null && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_OK) {
      pm.save();
      pm = null;
      panel = null;
    } if (pm != null && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_CANCEL) { 
      pm = null;
      panel = null;
    }

  }

  @Override
  public SettingsTreeNode getNode() {
    SettingsTreeNode node = new SettingsTreeNode(ResourceManager.getText(Text.GRID_MENU), this);
    return node;
  }

  @Override
  public JComponent getPanel() {

    if (panel == null) {
      pm = new GridSetPModel();
      panel = new GridSettingsPanel(pm);
    }
    return panel;
  }

  @Override
  public void read(Element xml) {
    if (xml == null) {
      init();
    }
    else {
      Element gridEl = xml.getChild("grid");
      showGrid = gridEl.getAttributeValue("show").equals("1");
      gridColor = Color.decode(gridEl.getAttributeValue("color"));
      gridType = Integer.parseInt(gridEl.getAttributeValue("type"));
      gridSize = Integer.parseInt(gridEl.getAttributeValue("size"));

      Element rulerEl = xml.getChild("ruler");
      showRuler = rulerEl.getAttributeValue("show").equals("1");
    }

  }

  @Override
  public void save(Element xml) {
    Element e = new Element("grid");
    e.setAttribute("show", showGrid? "1" : "0");
    e.setAttribute("type", gridType + "");
    e.setAttribute("color", Utils.toHex(gridColor));
    e.setAttribute("size", gridSize + "");

    xml.addContent(e);
    Element r = new Element("ruler");
    r.setAttribute("show", showRuler? "1" : "0");
    xml.addContent(r);
  }

  public boolean isShowGrid() {
    return showGrid;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }

  public boolean isShowRuler() {
    return showRuler;
  }

  public void setShowRuler(boolean showRuler) {
    this.showRuler = showRuler;
  }

  public int getGridSize() {
    return gridSize;
  }

  public void setGridSize(int gridSize) {
    this.gridSize = gridSize;
  }

  public Color getGridColor() {
    return gridColor;
  }

  public void setGridColor(Color gridColor) {
    this.gridColor = gridColor;
  }

  public int getGridType() {
    return gridType;
  }

  public void setGridType(int gridType) {
    this.gridType = gridType;
  }

}
