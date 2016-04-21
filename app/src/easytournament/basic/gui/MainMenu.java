package easytournament.basic.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.EnumMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import easytournament.basic.action.MainMenuAction;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;


public class MainMenu extends JMenuBar implements PropertyChangeListener {
  private static final long serialVersionUID = 1L;
  private MainMenuPModel pm;

  private JMenu filemenu;

  private EnumMap<MainMenuAction,JMenuItem> menuItems = new EnumMap<MainMenuAction,JMenuItem>(
      MainMenuAction.class);

  public MainMenu(MainMenuPModel pm) {
    this.pm = pm;
    pm.addPropertyChangeListener(this);
    createMenu();
  }

  /**
   * 
   */
  private void createMenu() {
    this.add(createFileMenu());
    this.add(createEditMenu());
    this.add(createViewMenu());
    this.add(createFormatMenu());
    this.add(createShapeMenu());
    this.add(createDiagramMenu());
    this.add(createAboutMenu());
    //Disable all
    for (JMenuItem jmi : menuItems.values()) {
      jmi.setEnabled(false);
    }
  }

  private JMenu createFileMenu() {
    filemenu = new JMenu(ResourceManager.getText(Text.FILE_MENU));
    JMenuItem fileNew = new JMenuItem(pm.getAction(MainMenuAction.NEW));
    fileNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
        java.awt.Event.CTRL_MASK));
    JMenuItem fileOpen = new JMenuItem(pm.getAction(MainMenuAction.OPEN));
    fileOpen.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
    JMenuItem fileSave = new JMenuItem(pm.getAction(MainMenuAction.SAVE));
    fileSave.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
    JMenuItem fileSaveAs = new JMenuItem(pm.getAction(MainMenuAction.SAVEAS));
    JMenuItem fileClose = new JMenuItem(pm.getAction(MainMenuAction.CLOSE));
    fileClose.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_W, java.awt.Event.CTRL_MASK));
    JMenuItem fileImport = new JMenuItem(pm.getAction(MainMenuAction.IMPORT));
    JMenuItem fileExport = new JMenuItem(pm.getAction(MainMenuAction.EXPORT));
    JMenuItem fileSettings = new JMenuItem(pm.getAction(MainMenuAction.SETTINGS));
    JMenuItem filePrintSetting = new JMenuItem(
        pm.getAction(MainMenuAction.PAGESETUP));
    JMenuItem filePrint = new JMenuItem(pm.getAction(MainMenuAction.PRINT));
    filePrint.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_P, java.awt.Event.CTRL_MASK));
    filemenu.add(fileNew);
    filemenu.add(fileOpen);
    filemenu.add(fileSave);
    filemenu.add(fileSaveAs);
    filemenu.add(fileClose);
    filemenu.addSeparator();
    filemenu.add(fileImport);
    filemenu.add(fileExport);
    filemenu.addSeparator();
    filemenu.add(fileSettings);
    filemenu.addSeparator();
    filemenu.add(filePrintSetting);
    filemenu.add(filePrint);
    
    fileSave.setEnabled(false);
    fileSaveAs.setEnabled(false);
    fileClose.setEnabled(false);
    fileImport.setEnabled(false);
    fileExport.setEnabled(false);
    filePrint.setEnabled(false);
    filePrintSetting.setEnabled(false);
    
    menuItems.put(MainMenuAction.SAVE, fileSave);
    menuItems.put(MainMenuAction.SAVEAS, fileSaveAs);
    menuItems.put(MainMenuAction.CLOSE, fileClose);
    menuItems.put(MainMenuAction.EXPORT, fileExport);
    menuItems.put(MainMenuAction.IMPORT, fileImport);
    menuItems.put(MainMenuAction.PRINT, filePrint);
    menuItems.put(MainMenuAction.PAGESETUP, filePrintSetting);

    JMenuItem fileQuit = new JMenuItem(pm.getAction(MainMenuAction.EXIT));
    fileQuit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4,
        java.awt.Event.ALT_MASK));
    
    filemenu.addSeparator();
    filemenu.add(fileQuit);

    return filemenu;
  }

  private JMenu createEditMenu() {
    JMenu editmenu = new JMenu(ResourceManager.getText(Text.EDIT_MENU));

    JMenuItem editDelete = new JMenuItem(pm.getAction(MainMenuAction.DELETE));
    editDelete.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_DELETE, 0));
    JMenuItem editCut = new JMenuItem(pm.getAction(MainMenuAction.CUT));
    editCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
        java.awt.Event.CTRL_MASK));
    JMenuItem editCopy = new JMenuItem(pm.getAction(MainMenuAction.COPY));
    editCopy.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
    JMenuItem editPaste = new JMenuItem(pm.getAction(MainMenuAction.PASTE));
    editPaste.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
    JMenuItem editSelectAll = new JMenuItem(
        pm.getAction(MainMenuAction.SELECTALL));
    editSelectAll.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_A, java.awt.Event.CTRL_MASK));
    JMenuItem editUndo = new JMenuItem(pm.getAction(MainMenuAction.UNDO));
    editUndo.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_Z, java.awt.Event.CTRL_MASK));
    JMenuItem editRedo = new JMenuItem(pm.getAction(MainMenuAction.REDO));
    editRedo.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_Y, java.awt.Event.CTRL_MASK));

    editmenu.add(editUndo);
    editmenu.add(editRedo);
    editmenu.addSeparator();
    editmenu.add(editDelete);
    editmenu.add(editCut);
    editmenu.add(editCopy);
    editmenu.add(editPaste);
    editmenu.add(editSelectAll);

    menuItems.put(MainMenuAction.UNDO, editUndo);
    menuItems.put(MainMenuAction.REDO, editRedo);
    menuItems.put(MainMenuAction.DELETE, editDelete);
    menuItems.put(MainMenuAction.CUT, editCut);
    menuItems.put(MainMenuAction.COPY, editCopy);
    menuItems.put(MainMenuAction.PASTE, editPaste);
    menuItems.put(MainMenuAction.SELECTALL, editSelectAll);

    return editmenu;
  }

  private JMenu createViewMenu() {
    JMenu viewMenu = new JMenu(ResourceManager.getText(Text.VIEW_MENU));
    JMenuItem viewGrid = new JMenuItem(pm.getAction(MainMenuAction.GRID));
    JMenuItem viewRuler = new JMenuItem(pm.getAction(MainMenuAction.RULER));
    JMenu zoomMenu = new JMenu(ResourceManager.getText(Text.ZOOM_MENU));
    JMenuItem zoom400 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM400));
    JMenuItem zoom200 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM200));
    JMenuItem zoom150 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM150));
    JMenuItem zoom100 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM100));
    JMenuItem zoom75 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM75));
    JMenuItem zoom50 = new JMenuItem(pm.getAction(MainMenuAction.ZOOM50));
    JMenuItem zoomSelect = new JMenuItem(
        pm.getAction(MainMenuAction.ZOOMSELECT));
    JMenuItem viewZoomIn = new JMenuItem(pm.getAction(MainMenuAction.ZOOMIN));
    JMenuItem viewZoomOut = new JMenuItem(pm.getAction(MainMenuAction.ZOOMOUT));
    JMenuItem viewZoomPage = new JMenuItem(
        pm.getAction(MainMenuAction.ZOOMPAGE));
    JMenuItem viewZoomWidth = new JMenuItem(
        pm.getAction(MainMenuAction.ZOOMWITH));
    JMenuItem viewZoomReal = new JMenuItem(
        pm.getAction(MainMenuAction.ZOOMREAL));

    zoomMenu.add(zoom400);
    zoomMenu.add(zoom200);
    zoomMenu.add(zoom150);
    zoomMenu.add(zoom100);
    zoomMenu.add(zoom75);
    zoomMenu.add(zoom50);
    zoomMenu.addSeparator();
    zoomMenu.add(zoomSelect);

    viewMenu.add(viewGrid);
    viewMenu.add(viewRuler);
    viewMenu.addSeparator();
    viewMenu.add(zoomMenu);
    viewMenu.addSeparator();
    viewMenu.add(viewZoomIn);
    viewMenu.add(viewZoomOut);
    viewMenu.addSeparator();
    viewMenu.add(viewZoomPage);
    viewMenu.add(viewZoomWidth);
    viewMenu.add(viewZoomReal);

    menuItems.put(MainMenuAction.ZOOM400, zoom400);
    menuItems.put(MainMenuAction.ZOOM200, zoom200);
    menuItems.put(MainMenuAction.ZOOM150, zoom150);
    menuItems.put(MainMenuAction.ZOOM100, zoom100);
    menuItems.put(MainMenuAction.ZOOM75, zoom75);
    menuItems.put(MainMenuAction.ZOOM50, zoom50);
    menuItems.put(MainMenuAction.ZOOMSELECT, zoomSelect);

    menuItems.put(MainMenuAction.GRID, viewGrid);
    menuItems.put(MainMenuAction.RULER, viewRuler);
    menuItems.put(MainMenuAction.ZOOMIN, viewZoomIn);
    menuItems.put(MainMenuAction.ZOOMOUT, viewZoomOut);
    menuItems.put(MainMenuAction.ZOOMPAGE, viewZoomPage);
    menuItems.put(MainMenuAction.ZOOMWITH, viewZoomWidth);
    menuItems.put(MainMenuAction.ZOOMREAL, viewZoomReal);

    return viewMenu;
  }

  private JMenu createFormatMenu() {
    JMenu formatmenu = new JMenu(ResourceManager.getText(Text.FORMAT_MENU));

    JMenu backgroundmenu = new JMenu(ResourceManager.getText(Text.SHAPE_MENU));
    JMenuItem backgroundfill = new JMenuItem(
        pm.getAction(MainMenuAction.CELL_FILLCOLOR));
    JMenuItem backgroundgrad = new JMenuItem(
        pm.getAction(MainMenuAction.CELL_COLORGRADIENT));
    JMenuItem backgroundopac = new JMenuItem(
        pm.getAction(MainMenuAction.CELL_OPACITY));
    backgroundmenu.add(backgroundfill);
    backgroundmenu.add(backgroundgrad);
    backgroundmenu.add(backgroundopac);
    menuItems.put(MainMenuAction.CELL_FILLCOLOR, backgroundfill);
    menuItems.put(MainMenuAction.CELL_COLORGRADIENT, backgroundgrad);
    menuItems.put(MainMenuAction.CELL_OPACITY, backgroundopac);

    JMenu labelmenu = new JMenu(ResourceManager.getText(Text.TEXT_MENU));
    JMenuItem labelcolor = new JMenuItem(
        pm.getAction(MainMenuAction.FONT_COLOR));
    JMenu alignmenu = new JMenu(ResourceManager.getText(Text.ALIGNMENT_MENU));
    JMenuItem aligntop = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_TOP));
    JMenuItem alignmiddle = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_MIDDLE));
    JMenuItem alignbottom = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_BOTTOM));
    JMenuItem alignleft = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_LEFT));
    JMenuItem aligncenter = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_CENTER));
    JMenuItem alignright = new JMenuItem(
        pm.getAction(MainMenuAction.TEXT_ALIGN_RIGHT));
    alignmenu.add(aligntop);
    alignmenu.add(alignmiddle);
    alignmenu.add(alignbottom);
    alignmenu.addSeparator();
    alignmenu.add(alignleft);
    alignmenu.add(aligncenter);
    alignmenu.add(alignright);
    labelmenu.add(labelcolor);
    labelmenu.add(alignmenu);

    menuItems.put(MainMenuAction.FONT_COLOR, labelcolor);
    menuItems.put(MainMenuAction.TEXT_ALIGN_TOP, aligntop);
    menuItems.put(MainMenuAction.TEXT_ALIGN_MIDDLE, alignmiddle);
    menuItems.put(MainMenuAction.TEXT_ALIGN_BOTTOM, alignbottom);
    menuItems.put(MainMenuAction.TEXT_ALIGN_LEFT, alignleft);
    menuItems.put(MainMenuAction.TEXT_ALIGN_CENTER, aligncenter);
    menuItems.put(MainMenuAction.TEXT_ALIGN_RIGHT, alignright);

    JMenu linemenu = new JMenu(ResourceManager.getText(Text.LINE_MENU));
    JMenuItem linecolor = new JMenuItem(pm.getAction(MainMenuAction.LINE_COLOR));
    JMenuItem linedashed = new JMenuItem(
        pm.getAction(MainMenuAction.LINE_DASHED));
    JMenuItem linewidth = new JMenuItem(pm.getAction(MainMenuAction.LINE_WIDTH));
    JMenuItem linestraight = new JMenuItem(
        pm.getAction(MainMenuAction.CON_STRAIGHT));
    JMenuItem linehoriz = new JMenuItem(pm.getAction(MainMenuAction.CON_HORIZ));
    JMenuItem linevert = new JMenuItem(pm.getAction(MainMenuAction.CON_VERT));
    JMenuItem linearrow = new JMenuItem(pm.getAction(MainMenuAction.CON_ARROW));
    linemenu.add(linecolor);
    linemenu.add(linedashed);
    linemenu.add(linewidth);
    linemenu.addSeparator();
    linemenu.add(linestraight);
    linemenu.add(linehoriz);
    linemenu.add(linevert);
    linemenu.add(linearrow);

    menuItems.put(MainMenuAction.LINE_COLOR, linecolor);
    menuItems.put(MainMenuAction.LINE_DASHED, linedashed);
    menuItems.put(MainMenuAction.LINE_WIDTH, linewidth);
    menuItems.put(MainMenuAction.CON_STRAIGHT, linestraight);
    menuItems.put(MainMenuAction.CON_HORIZ, linehoriz);
    menuItems.put(MainMenuAction.CON_VERT, linevert);
    menuItems.put(MainMenuAction.CON_ARROW, linearrow);

    JMenu lineendmenu = new JMenu(ResourceManager.getText(Text.LINEEND_MENU));
    JMenuItem endopen = new JMenuItem(pm.getAction(MainMenuAction.END_OPEN));
    JMenuItem endclassic = new JMenuItem(
        pm.getAction(MainMenuAction.END_CLASSIC));
    JMenuItem endblock = new JMenuItem(pm.getAction(MainMenuAction.END_BLOCK));
    JMenuItem endarrow = new JMenuItem(pm.getAction(MainMenuAction.END_SIZE));
    lineendmenu.add(endopen);
    lineendmenu.add(endclassic);
    lineendmenu.add(endblock);
    lineendmenu.addSeparator();
    lineendmenu.add(endarrow);

    menuItems.put(MainMenuAction.END_OPEN, endopen);
    menuItems.put(MainMenuAction.END_CLASSIC, endclassic);
    menuItems.put(MainMenuAction.END_BLOCK, endblock);
    menuItems.put(MainMenuAction.END_SIZE, endarrow);

    formatmenu.add(backgroundmenu);
    formatmenu.add(labelmenu);
    formatmenu.add(linemenu);
    formatmenu.add(lineendmenu);
    return formatmenu;
  }

  private JMenu createShapeMenu() {
    JMenu shapemenu = new JMenu(ResourceManager.getText(Text.SHAPE_MENU)); 
    JMenuItem shapeUp = new JMenuItem(pm.getAction(MainMenuAction.SHAPE_UP));
    JMenuItem shapeDown = new JMenuItem(pm.getAction(MainMenuAction.SHAPE_DOWN));

    JMenu alignmenu = new JMenu(ResourceManager.getText(Text.ALIGNMENT_MENU));
    JMenuItem alignTop = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_TOP));
    JMenuItem alignMiddle = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_MIDDLE));
    JMenuItem alignBottom = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_BOTTOM));
    JMenuItem alignLeft = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_LEFT));
    JMenuItem alignCenter = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_CENTER));
    JMenuItem alignRight = new JMenuItem(
        pm.getAction(MainMenuAction.SHAPE_ALIGN_RIGHT));
    alignmenu.add(alignTop);
    alignmenu.add(alignMiddle);
    alignmenu.add(alignBottom);
    alignmenu.addSeparator();
    alignmenu.add(alignLeft);
    alignmenu.add(alignCenter);
    alignmenu.add(alignRight);

    shapemenu.add(shapeUp);
    shapemenu.add(shapeDown);
    shapemenu.add(alignmenu);

    menuItems.put(MainMenuAction.SHAPE_UP, shapeUp);
    menuItems.put(MainMenuAction.SHAPE_DOWN, shapeDown);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_TOP, alignTop);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_MIDDLE, alignMiddle);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_BOTTOM, alignBottom);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_LEFT, alignLeft);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_CENTER, alignCenter);
    menuItems.put(MainMenuAction.SHAPE_ALIGN_RIGHT, alignRight);
    return shapemenu;
  }

  private JMenu createDiagramMenu() {
    JMenu diagmenu = new JMenu(ResourceManager.getText(Text.PAGE_MENU));

    JMenu bgmenu = new JMenu(ResourceManager.getText(Text.BACKGROUND_MENU));
    JMenuItem bgfillcol = new JMenuItem(
        pm.getAction(MainMenuAction.DIAG_FILLCOLOR));
    JMenuItem bgpagebg = new JMenuItem(
        pm.getAction(MainMenuAction.DIAG_PAGE_BACKCOLOR));
    bgmenu.add(bgfillcol);
    bgmenu.add(bgpagebg);

    menuItems.put(MainMenuAction.DIAG_FILLCOLOR, bgfillcol);
    menuItems.put(MainMenuAction.DIAG_PAGE_BACKCOLOR, bgpagebg);

    JMenu gridmenu = new JMenu(ResourceManager.getText(Text.GRID_MENU));
    gridmenu.setIcon(MainMenuAction.getIcon(MainMenuAction.GRID));
    JMenuItem gridsize = new JMenuItem(pm.getAction(MainMenuAction.GRID_SIZE));
    JMenuItem gridcolor = new JMenuItem(pm.getAction(MainMenuAction.GRID_COLOR));
    JMenuItem griddashed = new JMenuItem(
        pm.getAction(MainMenuAction.GRID_DASHED));
    JMenuItem gridpoint = new JMenuItem(pm.getAction(MainMenuAction.GRID_POINT));
    JMenuItem gridline = new JMenuItem(pm.getAction(MainMenuAction.GRID_LINE));
    JMenuItem gridcross = new JMenuItem(pm.getAction(MainMenuAction.GRID_CROSS));
    gridmenu.add(gridsize);
    gridmenu.add(gridcolor);
    gridmenu.addSeparator();
    gridmenu.add(griddashed);
    gridmenu.add(gridpoint);
    gridmenu.add(gridline);
    gridmenu.add(gridcross);

    menuItems.put(MainMenuAction.GRID_SIZE, gridsize);
    menuItems.put(MainMenuAction.GRID_COLOR, gridcolor);
    menuItems.put(MainMenuAction.GRID_DASHED, griddashed);
    menuItems.put(MainMenuAction.GRID_POINT, gridpoint);
    menuItems.put(MainMenuAction.GRID_LINE, gridline);
    menuItems.put(MainMenuAction.GRID_CROSS, gridcross);

    diagmenu.add(bgmenu);
    diagmenu.add(gridmenu);
    return diagmenu;
  }

  private JMenu createAboutMenu() {
    JMenu aboutmenu = new JMenu(ResourceManager.getText(Text.HELP_MENU));
    JMenuItem aboutInfo = new JMenuItem(pm.getAction(MainMenuAction.INFO));
    JMenuItem aboutHelp = new JMenuItem(pm.getAction(MainMenuAction.HELP));
    aboutmenu.add(aboutHelp);
    aboutmenu.add(aboutInfo);
    return aboutmenu;
  }

  /*
   * (Kein Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(MainMenuPModel.ENABLE_ALL)) {
      for (JMenuItem jmi : menuItems.values()) {
        jmi.setEnabled(true);
      }
    }
    else if (evt.getPropertyName().equals(MainMenuPModel.DISABLE_ALL)) {
      for (JMenuItem jmi : menuItems.values()) {
        jmi.setEnabled(false);
      }
    }
    else if (evt.getPropertyName().equals(MainMenuPModel.ENABLE)) {
      Collection<MainMenuAction> actions = (Collection<MainMenuAction>)evt
          .getNewValue();
      for (MainMenuAction mma : actions) {
        menuItems.get(mma).setEnabled(true);
      }

    }
    else if (evt.getPropertyName().equals(MainMenuPModel.DISABLE)) {
      Collection<MainMenuAction> actions = (Collection<MainMenuAction>)evt
          .getNewValue();
      for (MainMenuAction mma : actions) {
        menuItems.get(mma).setEnabled(false);
      }
    }
  }
}
