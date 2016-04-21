package easytournament.basic.action;

import javax.swing.ImageIcon;

import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

/**
 * Enumerator of all main menu and toolbar actions
 * @author David Meier
 *
 */
public enum MainMenuAction {
  NEW, OPEN, SAVE, SAVEAS, PAGESETUP, PRINT, CLOSE, EXIT, IMPORT, EXPORT, COPY, PASTE, CUT, DELETE, UNDO, REDO, SELECTALL, HELP, INFO, GRID, 
  RULER, ZOOMIN, ZOOMOUT, ZOOM400, ZOOM200, ZOOM150, ZOOM100, ZOOM75, ZOOM50, ZOOMSELECT, ZOOMPAGE, ZOOMWITH, ZOOMREAL, CELL_FILLCOLOR, CELL_COLORGRADIENT, 
  CELL_OPACITY, FONT_COLOR, TEXT_ALIGN_TOP, TEXT_ALIGN_MIDDLE, TEXT_ALIGN_BOTTOM, TEXT_ALIGN_LEFT, TEXT_ALIGN_CENTER, TEXT_ALIGN_RIGHT, LINE_COLOR, LINE_DASHED, 
  LINE_WIDTH, CON_STRAIGHT, CON_HORIZ, CON_VERT, CON_ARROW, END_OPEN, END_CLASSIC, END_BLOCK, END_SIZE, SHAPE_UP, SHAPE_DOWN, SHAPE_ALIGN_LEFT, 
  SHAPE_ALIGN_CENTER, SHAPE_ALIGN_RIGHT, SHAPE_ALIGN_TOP, SHAPE_ALIGN_MIDDLE, SHAPE_ALIGN_BOTTOM, DIAG_FILLCOLOR, DIAG_PAGE_BACKCOLOR, GRID_SIZE, GRID_COLOR, 
  GRID_DASHED, GRID_POINT, GRID_LINE, GRID_CROSS, SETTINGS;

  /**
   * Returns the translated text of a main menu item
   * @param action the action to get the text for
   * @return the translated text of a main menu item
   */
  public static String getText(MainMenuAction action) {
    switch (action) {
      case NEW:
        return ResourceManager.getText(Text.NEW_MENU);
      case OPEN:
        return ResourceManager.getText(Text.OPEN_MENU);
      case SAVE:
        return ResourceManager.getText(Text.SAVE_MENU);
      case SAVEAS:
        return ResourceManager.getText(Text.SAVEAS_MENU);
      case CLOSE:
        return ResourceManager.getText(Text.CLOSE_MENU);
      case PAGESETUP:
        return ResourceManager.getText(Text.PAGESETUP_MENU);
      case PRINT:
        return ResourceManager.getText(Text.PRINT_MENU);
      case COPY:
        return ResourceManager.getText(Text.COPY_MENU);
      case CUT:
        return ResourceManager.getText(Text.CUT_MENU);
      case DELETE:
        return ResourceManager.getText(Text.DELETE_MENU);
      case REDO:
        return ResourceManager.getText(Text.REDO_MENU);
      case UNDO:
        return ResourceManager.getText(Text.UNDO_MENU);
      case PASTE:
        return ResourceManager.getText(Text.PASTE_MENU);
      case IMPORT:
        return ResourceManager.getText(Text.IMPORT_MENU);
      case EXPORT:
        return ResourceManager.getText(Text.EXPORT_MENU);
      case EXIT:
        return ResourceManager.getText(Text.EXIT_MENU);
      case SELECTALL:
        return ResourceManager.getText(Text.SELECTALL_MENU);
      case HELP:
        return ResourceManager.getText(Text.HELP_MENU);
      case INFO:
        return ResourceManager.getText(Text.INFO_MENU);
      case GRID:
        return ResourceManager.getText(Text.GRID_MENU);
      case RULER:
        return ResourceManager.getText(Text.RULER_MENU);
      case ZOOMIN:
        return ResourceManager.getText(Text.ZOOMIN_MENU);
      case ZOOMOUT:
        return ResourceManager.getText(Text.ZOOMOUT_MENU);
      case ZOOM400:
        return ResourceManager.getText(Text.ZOOM400_MENU);
      case ZOOM200:
        return ResourceManager.getText(Text.ZOOM200_MENU);
      case ZOOM150:
        return ResourceManager.getText(Text.ZOOM150_MENU);
      case ZOOM100:
        return ResourceManager.getText(Text.ZOOM100_MENU);
      case ZOOM75:
        return ResourceManager.getText(Text.ZOOM75_MENU);
      case ZOOM50:
        return ResourceManager.getText(Text.ZOOM50_MENU);
      case ZOOMSELECT:
        return ResourceManager.getText(Text.ZOOMSELECT_MENU);
      case ZOOMPAGE:
        return ResourceManager.getText(Text.ZOOMPAGE_MENU);
      case ZOOMWITH:
        return ResourceManager.getText(Text.ZOOMWIDTH_MENU);
      case ZOOMREAL:
        return ResourceManager.getText(Text.ZOOMREAL_MENU);
      case CELL_FILLCOLOR:
        return ResourceManager.getText(Text.FILLCOLOR_MENU);
      case CELL_COLORGRADIENT:
        return ResourceManager.getText(Text.COLORGRADIENT_MENU);
      case CELL_OPACITY:
        return ResourceManager.getText(Text.OPACITY_MENU);
      case FONT_COLOR:
        return ResourceManager.getText(Text.FONTCOLOR_MENU);
      case TEXT_ALIGN_TOP:
        return ResourceManager.getText(Text.TEXT_ALIGN_TOP_MENU);
      case TEXT_ALIGN_MIDDLE:
        return ResourceManager.getText(Text.TEXT_ALIGN_MIDDLE_MENU);
      case TEXT_ALIGN_BOTTOM:
        return ResourceManager.getText(Text.TEXT_ALIGN_BOTTOM_MENU);
      case TEXT_ALIGN_LEFT:
        return ResourceManager.getText(Text.TEXT_ALIGN_LEFT_MENU);
      case TEXT_ALIGN_CENTER:
        return ResourceManager.getText(Text.TEXT_ALIGN_CENTER_MENU);
      case TEXT_ALIGN_RIGHT:
        return ResourceManager.getText(Text.TEXT_ALIGN_RIGHT_MENU);
      case LINE_COLOR:
        return ResourceManager.getText(Text.LINE_COLOR_MENU);
      case LINE_DASHED:
        return ResourceManager.getText(Text.LINE_DASHED_MENU);
      case LINE_WIDTH:
        return ResourceManager.getText(Text.LINE_WIDTH_MENU);
      case CON_STRAIGHT:
        return ResourceManager.getText(Text.CON_STRAIGHT_MENU);
      case CON_HORIZ:
        return ResourceManager.getText(Text.CON_HORIZ_MENU);
      case CON_VERT:
        return ResourceManager.getText(Text.CON_VERT_MENU);
      case CON_ARROW:
        return ResourceManager.getText(Text.CON_ARROW_MENU);
      case END_OPEN:
        return ResourceManager.getText(Text.END_OPEN_MENU);
      case END_CLASSIC:
        return ResourceManager.getText(Text.END_CLASSIC_MENU);
      case END_BLOCK:
        return ResourceManager.getText(Text.END_BLOCK_MENU);
      case END_SIZE:
        return ResourceManager.getText(Text.END_SIZE_MENU);
      case SHAPE_UP:
        return ResourceManager.getText(Text.SHAPE_UP_MENU);
      case SHAPE_DOWN:
        return ResourceManager.getText(Text.SHAPE_DOWN_MENU);
      case SHAPE_ALIGN_LEFT:
        return ResourceManager.getText(Text.SHAPE_ALIGN_LEFT_MENU);
      case SHAPE_ALIGN_CENTER:
        return ResourceManager.getText(Text.SHAPE_ALIGN_CENTER_MENU);
      case SHAPE_ALIGN_RIGHT:
        return ResourceManager.getText(Text.SHAPE_ALIGN_RIGHT_MENU);
      case SHAPE_ALIGN_TOP:
        return ResourceManager.getText(Text.SHAPE_ALIGN_TOP_MENU);
      case SHAPE_ALIGN_MIDDLE:
        return ResourceManager.getText(Text.SHAPE_ALIGN_MIDDLE_MENU);
      case SHAPE_ALIGN_BOTTOM:
        return ResourceManager.getText(Text.SHAPE_ALIGN_BOTTOM_MENU);
      case DIAG_FILLCOLOR:
        return ResourceManager.getText(Text.DIAG_FILLCOLOR_MENU);
      case DIAG_PAGE_BACKCOLOR:
        return ResourceManager.getText(Text.DIAG_PAGE_BACKCOLOR_MENU);
      case GRID_SIZE:
        return ResourceManager.getText(Text.GRID_SIZE_MENU);
      case GRID_COLOR:
        return ResourceManager.getText(Text.GRID_COLOR_MENU);
      case GRID_DASHED:
        return ResourceManager.getText(Text.GRID_DASHED_MENU);
      case GRID_POINT:
        return ResourceManager.getText(Text.GRID_POINTS_MENU);
      case GRID_LINE:
        return ResourceManager.getText(Text.GRID_LINE_MENU);
      case GRID_CROSS:
        return ResourceManager.getText(Text.GRID_CROSS_MENU);
      case SETTINGS:
        return ResourceManager.getText(Text.SETTINGS_MENU);
      default:
        break;
    }
    return null;
  }

  /**
   * Returns the icon to a main menu item
   * @param action the action to get the icon for
   * @return icon to a main menu item
   */
  public static ImageIcon getIcon(MainMenuAction action) {
    switch (action) {
      case NEW:
        return ResourceManager.getIcon(Icon.NEW_ICON_SMALL);
      case OPEN:
        return ResourceManager.getIcon(Icon.OPEN_ICON_SMALL);
      case SAVE:
        return ResourceManager.getIcon(Icon.SAVE_ICON_SMALL);
      case PRINT:
        return ResourceManager.getIcon(Icon.PRINT_ICON_SMALL);
      case COPY:
        return ResourceManager.getIcon(Icon.COPY_ICON_SMALL);
      case CUT:
        return ResourceManager.getIcon(Icon.CUT_ICON_SMALL);
      case DELETE:
        return ResourceManager.getIcon(Icon.DELETE_ICON_SMALL);
      case REDO:
        return ResourceManager.getIcon(Icon.REDO_ICON_SMALL);
      case UNDO:
        return ResourceManager.getIcon(Icon.UNDO_ICON_SMALL);
      case PASTE:
        return ResourceManager.getIcon(Icon.PASTE_ICON_SMALL);
      case TEXT_ALIGN_CENTER:
        return ResourceManager.getIcon(Icon.ALIGNMENT_CENTER_ICON_SMALL);
      case TEXT_ALIGN_RIGHT:
        return ResourceManager.getIcon(Icon.ALIGNMENT_RIGHT_ICON_SMALL);
      case TEXT_ALIGN_LEFT:
        return ResourceManager.getIcon(Icon.ALIGNMENT_LEFT_ICON_SMALL);
      case TEXT_ALIGN_TOP:
        return ResourceManager.getIcon(Icon.ALIGNMENT_TOP_ICON_SMALL);
      case TEXT_ALIGN_MIDDLE:
        return ResourceManager.getIcon(Icon.ALIGNMENT_MIDDLE_ICON_SMALL);
      case TEXT_ALIGN_BOTTOM:
        return ResourceManager.getIcon(Icon.ALIGNMENT_BOTTOM_ICON_SMALL);
      case EXIT:
        return ResourceManager.getIcon(Icon.EXIT_ICON_SMALL);
      case FONT_COLOR:
        return ResourceManager.getIcon(Icon.TEXTCOLOR_ICON_SMALL);
      case CELL_FILLCOLOR:
        return ResourceManager.getIcon(Icon.FILLCOLOR_ICON_SMALL);
      case CELL_COLORGRADIENT:
        return ResourceManager.getIcon(Icon.GRADIENT_ICON_SMALL);
      case LINE_WIDTH:
        return ResourceManager.getIcon(Icon.LINEWIDTH_ICON_SMALL);
      case SETTINGS:
        return ResourceManager.getIcon(Icon.SETTINGS_ICON_SMALL);
      case SHAPE_ALIGN_LEFT:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_LEFT_ICON_SMALL);
      case SHAPE_ALIGN_CENTER:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_CENTER_ICON_SMALL);
      case SHAPE_ALIGN_RIGHT:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_RIGHT_ICON_SMALL);
      case SHAPE_ALIGN_TOP:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_TOP_ICON_SMALL);
      case SHAPE_ALIGN_MIDDLE:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_MIDDLE_ICON_SMALL);
      case SHAPE_ALIGN_BOTTOM:
        return ResourceManager
            .getIcon(Icon.SHAPE_ALIGNMENT_BOTTOM_ICON_SMALL);
      case GRID_POINT:
        return ResourceManager.getIcon(Icon.VIEW_GRID_POINT_ICON_SMALL);
      case GRID:
        return ResourceManager.getIcon(Icon.VIEW_GRID_LINE_ICON_SMALL);
      case GRID_LINE:
        return ResourceManager.getIcon(Icon.VIEW_GRID_LINE_ICON_SMALL);
      case RULER:
        return ResourceManager.getIcon(Icon.VIEW_RULER_ICON_SMALL);
      case ZOOMIN:
        return ResourceManager.getIcon(Icon.VIEW_ZOOMIN_ICON_SMALL);
      case ZOOMOUT:
        return ResourceManager.getIcon(Icon.VIEW_ZOOMOUT_ICON_SMALL);
      case SHAPE_UP: 
        return ResourceManager.getIcon(Icon.SHAPE_TOFRONT_ICON_SMALL);
      case SHAPE_DOWN: 
        return ResourceManager.getIcon(Icon.SHAPE_TOBACK_ICON_SMALL);
      case LINE_COLOR: 
        return ResourceManager.getIcon(Icon.LINECOLOR_ICON_SMALL);
      default:
        break;        
    }
    return null;
  }
}
