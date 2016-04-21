package easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

import easytournament.basic.MetaInfos;
import easytournament.basic.model.HomeScreenPModel;
import easytournament.basic.model.MainFramePModel;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.model.MainToolBarPModel;
import easytournament.basic.navigationitem.NavTreeActions;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;


public class MainFrame extends JFrame implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private JTree navi;
  private Component current;
  private MainFramePModel pm;
  private JToolBar variableToolbar = new JToolBar();
  private JPanel p;

  public MainFrame(MainFramePModel pm) {
    super(MetaInfos.APP_NAME);
    this.pm = pm;
    init();
  }

  private void init() {
    this.setIconImage(ResourceManager.getIcon(Icon.APP_ICON).getImage());
    this.pm.addPropertyChangeListener(this);
    MainMenu bar = new MainMenu(MainMenuPModel.getInstance());
    this.setJMenuBar(bar);
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    p = new JPanel(new BorderLayout());

    p.add(
        new MainToolBar(MainToolBarPModel.getInstance(), MainMenuPModel
            .getInstance()), BorderLayout.WEST);
    variableToolbar.setFloatable(false);
    p.add(variableToolbar, BorderLayout.CENTER);
    
    cp.add(p, BorderLayout.NORTH);
    this.current = new JScrollPane(new HomeScreen(new HomeScreenPModel()));
    cp.add(current, BorderLayout.CENTER);

  }

  private Component getNavigationTree() {

    navi = new JTree(pm.getTreeModel());
    navi.setRootVisible(false);
    navi.setSelectionModel(pm.getTreeSelectionModel());
    navi.addTreeSelectionListener(pm.getTreeSelectionistener());
    return navi;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == NavTreeActions.CHANGEPANEL.name()) {
      if (current != null)
        this.remove(current);
      current = (JComponent)evt.getNewValue();
      this.add(current, BorderLayout.CENTER);
      this.validate();
      this.repaint();
    }
    else if (evt.getPropertyName() == MainFramePModel.PROPERTY_TITLE) {
      this.setTitle((String)evt.getNewValue());
    }
    else if (evt.getPropertyName() == MainFramePModel.SET_VAR_TOOLBAR) {      
      if(this.p != null) {
        this.variableToolbar = ((JToolBar)evt.getNewValue());
        if(this.variableToolbar != null) {
          this.p.add(this.variableToolbar, BorderLayout.CENTER);
          this.validate();
          this.repaint();
        }
      }
    }
    else if (evt.getPropertyName() == MainFramePModel.REMOVE_VAR_TOOLBAR) {
      if(this.p != null && this.variableToolbar != null) {
        this.p.remove(this.variableToolbar);
        this.variableToolbar = null;
        this.validate();
        this.repaint();
      }
    }
    else if (evt.getPropertyName() == MainFramePModel.NAVTREE_VISIBLE) {
      if((Boolean) evt.getNewValue())
        this.getContentPane().add(this.getNavigationTree(), BorderLayout.WEST);
      else
        this.getContentPane().remove(navi);
      this.validate();
      this.repaint();

    }
    else if (evt.getPropertyName() == MainFramePModel.SHOW_HOMESCREEN) {
      if (current != null)
        this.remove(current);
      current = new JScrollPane(new HomeScreen(new HomeScreenPModel()));
      this.add(current, BorderLayout.CENTER);
      this.validate();
      this.repaint();
    }

  }
}
