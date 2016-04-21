package easytournament.designer.model.settings;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;

import com.jgoodies.binding.beans.Model;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.settings.GridSettings;


public class GridSetPModel extends Model {

  public static final String PROPERTY_SHOWGRID = "showGrid";
  public static final String PROPERTY_SHOWRULER = "showRuler";
  public static final String PROPERTY_GRIDSIZE = "gridSize";
  public static final String PROPERTY_GROUP_GRIDTYPE = "gridType";
  public static final int GRID_COLOR_ACTION = 0;

  protected boolean showGrid;
  protected boolean showRuler;
  protected int gridSize;
  protected Color gridColor;
  protected String gridType;
  protected ArrayList<String> gridTypes = new ArrayList<String>();
  protected GridSettings settings = GridSettings.getInstance();

  public GridSetPModel() {
    init();
  }

  private void init() {
    gridTypes.add(ResourceManager.getText(Text.GRID_POINTS_MENU));
    gridTypes.add(ResourceManager.getText(Text.GRID_CROSS_MENU));
    gridTypes.add(ResourceManager.getText(Text.GRID_LINE_MENU));
    gridTypes.add(ResourceManager.getText(Text.GRID_DASHED_MENU));
    gridType = gridTypes.get(settings.getGridType());

    showRuler = settings.isShowRuler();
    showGrid = settings.isShowGrid();
    gridSize = settings.getGridSize();

  }

  public void save() {
    settings.setGridColor(gridColor);
    settings.setGridSize(gridSize);
    settings.setShowGrid(showGrid);
    settings.setShowRuler(showRuler);
    if (gridType.equals(gridTypes.get(0)))
      settings.setGridType(0);
    else if (gridType.equals(gridTypes.get(1)))
      settings.setGridType(1);
    else if (gridType.equals(gridTypes.get(2)))
      settings.setGridType(2);
    else
      settings.setGridType(3);

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

  public Action getColorAction(int action) {
    AbstractAction a = null;

    switch (action) {
      case GRID_COLOR_ACTION: {
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.FONTCOLOR_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              gridColor = newColor;
            }

          }
        };
        gridColor = settings.getGridColor();
        a.putValue("FG", gridColor);
        break;
      }
    }
    return a;
  }

  public String getGridType() {
    return gridType;
  }

  public void setGridType(String gridType) {
    this.gridType = gridType;
  }

  public List<String> getGridTypes() {
    return gridTypes;
  }

}
