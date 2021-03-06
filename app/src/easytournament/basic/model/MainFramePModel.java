package easytournament.basic.model;

import java.awt.Desktop;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdom.Document;
import org.jdom.Element;

import com.jgoodies.binding.beans.Model;

import easytournament.basic.MetaInfos;
import easytournament.basic.Organizer;
import easytournament.basic.action.MainMenuAction;
import easytournament.basic.gui.MainMenuObservable;
import easytournament.basic.gui.dialog.AboutDialog;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.gui.dialog.ExportDialog;
import easytournament.basic.gui.dialog.ImportDialog;
import easytournament.basic.gui.dialog.SettingsDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.model.dialog.ExportDialogPModel;
import easytournament.basic.model.dialog.ImportDialogPModel;
import easytournament.basic.model.dialog.SettingsDialogPModel;
import easytournament.basic.navigationitem.NavTreeActions;
import easytournament.basic.navigationitem.NavTreeItems;
import easytournament.basic.navigationitem.NaviNode;
import easytournament.basic.navigationitem.NavigationItem;
import easytournament.basic.resources.Help;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.SettingsRegistry;
import easytournament.basic.valueholder.HistoryFile;
import easytournament.basic.valueholder.Tournament;
import easytournament.basic.xml.HistoryXMLHandler;
import easytournament.basic.xml.XMLHandler;

public class MainFramePModel extends Model implements TreeSelectionListener,
    Observer, WindowListener {

  private static final long serialVersionUID = 1L;
  private static File USERDIR = null;
  private static File HISTORY_PATH = null;
  public static final String PROPERTY_TITLE = "title";
  public static final String SET_VAR_TOOLBAR = "setvartoolbar";
  public static final String REMOVE_VAR_TOOLBAR = "removevartoolbar";
  public static final String NAVTREE_VISIBLE = "navTreeVisible";
  public static final String SHOW_HOMESCREEN = "showHomeScreen";
  private DefaultMutableTreeNode root = new DefaultMutableTreeNode();
  private DefaultTreeModel tModel = new DefaultTreeModel(root);
  private DefaultTreeSelectionModel sModel;
  private NavigationItem currentItem;
  private File currentFile = null;
  private static MainFramePModel instance;
  private Organizer organizer = Organizer.getInstance();
  private boolean homeScreenVisible = true;

  private MainFramePModel() {
    MainMenuObservable.getInstance().addObserver(this);
    this.retrieveData();
  }

  public static MainFramePModel getInstance() {
    if (instance == null)
      instance = new MainFramePModel();
    return instance;
  }

  private void retrieveData() {
   if(organizer.hasWriteAccess()){
     HISTORY_PATH = new File("history.xml");
   }
   else
   {
     USERDIR = new File(new JFileChooser()
     .getFileSystemView().getDefaultDirectory(), "/EasyTournament");
     HISTORY_PATH = new File(USERDIR, "history.xml");
   }
    
    
    for (NavigationItem item : NavTreeItems.NAVMENU_ITEMS) {
      MainMenuPModel.getInstance().addPropertyChangeListener(item);
      this.addPropertyChangeListener(item);
      item.init();
      root.add(item.getNode());
    }
    if (HISTORY_PATH.exists())
      try {
        organizer.setHistory(HistoryXMLHandler.readHistory(XMLHandler
            .openXMLDoc(HISTORY_PATH)));
      }
      catch (FileNotFoundException e) {
        ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
        ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    else if (!organizer.hasWriteAccess() && !USERDIR.exists())
      USERDIR.mkdirs();
  }

  public TreeModel getTreeModel() {
    return tModel;
  }

  public TreeSelectionModel getTreeSelectionModel() {
    sModel = new DefaultTreeSelectionModel();
    sModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    return sModel;
  }

  public TreeSelectionListener getTreeSelectionistener() {
    return this;
  }

  public void valueChanged(TreeSelectionEvent arg0) {
    changePanel();
  }

  private void changePanel() {
    NaviNode node = (NaviNode)sModel.getSelectionPath().getLastPathComponent();
    if (currentItem != null) {
      MainMenuObservable.getInstance().deleteObserver(currentItem);
      if (!currentItem.deactivate()) {
        return; // TODO: what can we do if deactivating fails?
      }
    }
    this.removeVariableToolbar();
    currentItem = node.getItem();
    MainMenuPModel.getInstance().disableAllItems();
    this.firePropertyChange(NavTreeActions.CHANGEPANEL.name(), null,
        currentItem.getPanel());
    MainMenuObservable.getInstance().addObserver(currentItem);
    currentItem.activate();
    this.setVariableToolbar(currentItem.getToolBar());
  }

  @Override
  public void update(Observable o, Object val) {
    if (o instanceof MainMenuObservable) {
      if (val instanceof MainMenuAction) {
        MainMenuAction action = (MainMenuAction)val;
        switch (action) {
          case OPEN: {
            if (organizer.isSaved()) {
              open();
            }
            else {
              int answer = JOptionPane.showConfirmDialog(
                  organizer.getMainFrame(),
                  ResourceManager.getText(Text.SAVE_TOURNAMENT));
              switch (answer) {
                case JOptionPane.OK_OPTION:
                  if (this.currentFile == null) {
                    if(!saveAs()) return;
                  }
                  else
                    save(this.currentFile);
                  // break not needed!!
                  //$FALL-THROUGH$
                case JOptionPane.NO_OPTION:
                  open();
                  break;
                default:
                  break;
              }
            }
            break;
          }
          case SAVEAS:
            saveAs();
            break;
          case SAVE:
            if (this.currentFile == null)
              saveAs();
            else
              save(this.currentFile);
            break;
          case NEW: {
            newTournament();
            break;
          }
          case CLOSE:
            if (organizer.isSaved()) {
              this.close();
            }
            else {
              int answer = JOptionPane.showConfirmDialog(
                  organizer.getMainFrame(),
                  ResourceManager.getText(Text.SAVE_TOURNAMENT));
              switch (answer) {
                case JOptionPane.OK_OPTION:
                  if (this.currentFile == null) {
                    if(!saveAs()) return;
                  }
                  else
                    save(this.currentFile);
                  // break not needed!!
                  //$FALL-THROUGH$
                case JOptionPane.NO_OPTION:
                  this.close();
                  break;
                default:
                  break;
              }
            }
            break;
          case HELP:
            try {
              if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(ResourceManager.getHelp(Help.INDEX));
              }
              else {
                JOptionPane.showMessageDialog(organizer.getMainFrame(),
                    ResourceManager.getText(Text.OPEN_FILE_BROWSER)+"\n"
                        + ResourceManager.getHelp(Help.INDEX).getPath().substring(1),
                    ResourceManager.getText(Text.ERROR),
                    JOptionPane.ERROR_MESSAGE);
              }
            }
            catch (Exception e) {
              try {
                if (Desktop.isDesktopSupported()) {
                  Desktop desktop = Desktop.getDesktop();
                  desktop.browse(new URI(ResourceManager.getText(Text.ONLINEHELPLINK)));
                }
                else {
                  JOptionPane.showMessageDialog(organizer.getMainFrame(),
                      ResourceManager.getText(Text.OPEN_FILE_BROWSER)+"\n"
                          + ResourceManager.getText(Text.ONLINEHELPLINK),
                      ResourceManager.getText(Text.ERROR),
                      JOptionPane.ERROR_MESSAGE);
                }
              }
              catch (Exception ex) {
                ErrorLogger.getLogger().throwing("MainFramePModel", "update", ex);
                ErrorDialog ed = new ErrorDialog(Organizer.getInstance()
                    .getMainFrame(), ResourceManager.getText(Text.ERROR),
                    ex.toString(), ex);
                ed.setVisible(true);
                ex.printStackTrace();
              }
            }
            break;
          case IMPORT: {
            new ImportDialog(organizer.getMainFrame(), true,
                new ImportDialogPModel());
            break;
          }
          case EXPORT: {
            new ExportDialog(organizer.getMainFrame(), true,
                new ExportDialogPModel(currentItem));
            break;
          }
          case EXIT: {
            exit();
          }
            break;
          case SETTINGS:
            SettingsDialogPModel pm = new SettingsDialogPModel();
            SettingsDialog.showPreferencesDialog(pm);
            break;
          case INFO:
            AboutDialog ad = new AboutDialog(organizer.getMainFrame(), true);
            ad.setVisible(true);
            break;
          default:
        }
      }
    }
  }

  public void newTournament() {
    if (organizer.isSaved()) {
      this.firePropertyChange(NavTreeActions.NEW.name(), "1", "2");
      if (homeScreenVisible) {
        this.firePropertyChange(NAVTREE_VISIBLE, false, true);
        this.homeScreenVisible = false;
        sModel.setSelectionPath(new TreePath(
            ((DefaultMutableTreeNode)root.getFirstChild()).getPath()));

      }              
    }
    else {
      int answer = JOptionPane.showConfirmDialog(
          organizer.getMainFrame(),
          ResourceManager.getText(Text.SAVE_TOURNAMENT));
      switch (answer) {
        case JOptionPane.OK_OPTION:
        	if (this.currentFile == null) {
        	  if(!saveAs()) return;
        	}
            else
              save(this.currentFile);
          // break not needed!!
          //$FALL-THROUGH$
        case JOptionPane.NO_OPTION:
          this.firePropertyChange(NavTreeActions.NEW.name(), "1", "2");
          if (homeScreenVisible) {
            this.firePropertyChange(NAVTREE_VISIBLE, false, true);
            this.homeScreenVisible = false;
            sModel.setSelectionPath(new TreePath(
                ((DefaultMutableTreeNode)root.getFirstChild())
                    .getPath()));
          }
          this.currentFile = null;
          firePropertyChange(PROPERTY_TITLE, "", MetaInfos.APP_NAME);
          return;
        default:
          return;
      }
    }
  }

  private void exit() {
    SettingsRegistry.saveSettings();
    if (organizer.isSaved()) {
      saveHistory();
      System.exit(0);
    }
    else {
      int answer = JOptionPane.showConfirmDialog(organizer.getMainFrame(),
          ResourceManager.getText(Text.SAVE_TOURNAMENT));
      switch (answer) {
        case JOptionPane.OK_OPTION:
          if (this.currentFile == null) {
            if(!saveAs()) return;
          }
          else
            save(this.currentFile);
          // break not needed!!
          //$FALL-THROUGH$
        case JOptionPane.NO_OPTION:
          saveHistory();
          System.exit(0);
          break;
        default:
          break;
      }
    }
  }

  private void saveHistory() {
    XMLHandler.saveXMLDoc(
        new Document(HistoryXMLHandler.writeHistory(organizer.getHistory())),
        HISTORY_PATH);
  }

  private boolean saveAs() {

    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".ett") || f.isDirectory();
      }

      public String getDescription() {
        return "ETT (*.ett)";
      }
    });
    FileFilter xmlfilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "XML (*.xml)";
      }

      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
      }
    };
    chooser.addChoosableFileFilter(xmlfilter);
    int answer = chooser.showSaveDialog(Organizer.getInstance().getMainFrame());
    if (answer == JFileChooser.APPROVE_OPTION) {
      File filename = chooser.getSelectedFile();

      if (filename != null) {
        if (!filename.getPath().toLowerCase().endsWith(".xml")
            && !filename.getPath().toLowerCase().endsWith(".ett")) {
          if (chooser.getFileFilter().equals(xmlfilter))
            filename = new File(filename.getPath() + ".xml");
          else
            filename = new File(filename.getPath() + ".ett");
        }
        save(filename);
      }
    }
    return answer == JFileChooser.APPROVE_OPTION;
  }

  private void save(File filename) {
    Element filetype = new Element("filetype");
    filetype.setAttribute("application", MetaInfos.FILE_APPLICATION);
    filetype.setAttribute("type", MetaInfos.FILE_MAINFILETYPE);
    filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

    this.firePropertyChange(NavTreeActions.SAVE.name(), null, filetype);

    XMLHandler.saveXMLDoc(new Document(filetype), filename);
    firePropertyChange(PROPERTY_TITLE, "", MetaInfos.APP_NAME.concat(" - ")
        .concat(filename.getName()));
    currentFile = filename;
    organizer.setSaved(true);
    addFileToHistory(filename);
  }

  private void addFileToHistory(File filename) {
    HistoryFile hf = new HistoryFile();
    hf.setPath(filename);
    organizer.getHistory().remove(hf);
    while (organizer.getHistory().size() > 10)
      organizer.getHistory().remove(0);

    Tournament tourn = organizer.getCurrentTournament();
    hf.setName(tourn.getName());
    hf.setLastModified(new Date(filename.lastModified()));
    hf.setSport(tourn.getSport().getName());
    hf.setSportid(tourn.getSport().getId());
    organizer.getHistory().add(hf);

  }

  private void open() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".xml")
            || f.getName().toLowerCase().endsWith(".ett") || f.isDirectory();
      }

      public String getDescription() {
        return "ETT (*.ett), XML (*.xml)";
      }
    });
    int answer = chooser.showOpenDialog(Organizer.getInstance().getMainFrame());

    if (answer == JFileChooser.APPROVE_OPTION) {
      open(chooser.getSelectedFile());
    }
  }

  public void open(File filename) {

    Document doc;
    try {
      doc = XMLHandler.openXMLDoc(filename);
    }
    catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(Organizer.getInstance()
          .getMainFrame(), ResourceManager
          .getText(Text.FILE_NOT_FOUND), ResourceManager
          .getText(Text.FILE_NOT_FOUND), JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (doc == null) {
      JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    Element filetype = doc.getRootElement();
    String app = filetype.getAttributeValue("application");
    if (app == null || !app.equals(MetaInfos.FILE_APPLICATION)) {
      JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    String type = filetype.getAttributeValue("type");
    if (type == null || !type.equals(MetaInfos.FILE_MAINFILETYPE)) {
      JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    String version = filetype.getAttributeValue("version");
    if (MetaInfos.compareVersionNr(MetaInfos.getXMLFileVersion(), version) < 0) {
      JOptionPane.showMessageDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.NEW_VERSION1) + MetaInfos.APP_NAME
              + ResourceManager.getText(Text.NEW_VERSION2)
              + MetaInfos.APP_WEBSITE
              + ResourceManager.getText(Text.NEW_VERSION3),
          ResourceManager.getText(Text.NEW_VERSION), JOptionPane.ERROR_MESSAGE);
      return;
    }

    this.firePropertyChange(NavTreeActions.OPEN.name(), null,
        doc.getRootElement());
    firePropertyChange(PROPERTY_TITLE, "", MetaInfos.APP_NAME.concat(" - ")
        .concat(filename.getName()));
    currentFile = filename;
    organizer.setSaved(true);

    addFileToHistory(filename);

    if (homeScreenVisible) {
      this.firePropertyChange(NAVTREE_VISIBLE, false, true);
      this.homeScreenVisible = false;
      sModel.setSelectionPath(new TreePath(((DefaultMutableTreeNode)root
          .getFirstChild()).getPath()));
    }

  }

  private void close() {
    if (!homeScreenVisible) {
      this.firePropertyChange(NAVTREE_VISIBLE, true, false);
      this.firePropertyChange(SHOW_HOMESCREEN, false, true);
      this.homeScreenVisible = true;
      this.organizer.resetTournament();      
      MainMenuPModel.getInstance().disableAllItems();
      firePropertyChange(PROPERTY_TITLE, "", MetaInfos.APP_NAME);
      this.currentFile = null;
    }
  }

  public void setVariableToolbar(JToolBar tb) {
    this.firePropertyChange(SET_VAR_TOOLBAR, null, tb);
  }

  public void removeVariableToolbar() {
    this.firePropertyChange(REMOVE_VAR_TOOLBAR, null, "1");
  }

  @Override
  public void windowActivated(WindowEvent arg0) {}

  @Override
  public void windowClosed(WindowEvent arg0) {}

  @Override
  public void windowClosing(WindowEvent arg0) {
    exit();
  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {}

  @Override
  public void windowDeiconified(WindowEvent arg0) {}

  @Override
  public void windowIconified(WindowEvent arg0) {}

  @Override
  public void windowOpened(WindowEvent arg0) {}
}
