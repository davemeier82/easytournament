package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jgoodies.binding.beans.Model;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.Settings;
import easytournament.basic.settings.SettingsRegistry;
import easytournament.basic.settings.SettingsTreeNode;

public class SettingsDialogPModel extends Model {

  public static final String UPDATE_PANEL = "updatepanel";
  public static final String DISPOSE = "dispose";
  public static final String PROPERTY_OK = "ok";
  public static final String PROPERTY_CANCEL = "cancel";
  private static DefaultMutableTreeNode root;
  private DefaultTreeModel treeModel;

  /**
   * Constructor
   */
  public SettingsDialogPModel() {
    initTree();
  }

  /**
   * initializes the tree of all preferences
   */
  private void initTree() {
    root = new DefaultMutableTreeNode();
    for (Settings s : SettingsRegistry.getSettings()) {
      root.add(s.getNode());
    }

    treeModel = new DefaultTreeModel(root);
  }

  /**
   * Tree Model
   * @return
   */
  public TreeModel getTreeModel() {
    return treeModel;
  }

  /**
   * Listener for Window Events
   * @return
   */
  public WindowListener getDialogListener() {
    return new DialogListener();
  }

  /**
   * Action to fire PROPERTY_OK and DISPOSE
   * @return
   */
  public Action getOKAction() {
    return new OKAction(ResourceManager.getText(Text.OK));
  }

  /**
   * Action to fire PROPERTY_CANCEL and DISPOSE
   * @return
   */
  public Action getCancelAction() {
    return new CancelAction(ResourceManager.getText(Text.CANCEL));
  }

  /**
   * Selection linstener of Preferences-Tree
   * @return
   */
  public TreeSelectionListener getTreeSelectionListener() {
    return new TreeListener();
  }

  /**
   * Action to fire PROPERTY_OK and DISPOSE
   * @author D. Meier
   */
  class OKAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param name
     *          text for action (JButton)
     */
    public OKAction(String name) {
      super(name);
    }

    /*
     * (Kein Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
      SettingsDialogPModel.this.firePropertyChange(PROPERTY_OK, false, true);
      SettingsDialogPModel.this.firePropertyChange(DISPOSE, false, true);
    }
  }

  /**
   * Action to fire PROPERTY_CANCEL and DISPOSE
   * @author D. Meier
   */
  class CancelAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param name
     *          text for action (JButton)
     */
    public CancelAction(String name) {
      super(name);
    }

    /*
     * (Kein Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
      SettingsDialogPModel.this
          .firePropertyChange(PROPERTY_CANCEL, false, true);
      SettingsDialogPModel.this.firePropertyChange(DISPOSE, false, true);
    }
  }

  /**
   * Action to fire PROPERTY_CANCEL
   * @author D. Meier
   */
  class DialogListener extends WindowAdapter {
    /*
     * (Kein Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
      SettingsDialogPModel.this
          .firePropertyChange(PROPERTY_CANCEL, false, true);
    }
  }

  /**
   * Preferences-Tree selection listener
   * @author D. Meier
   */
  class TreeListener implements TreeSelectionListener {

    /*
     * (Kein Javadoc)
     * 
     * @see
     * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
     * .TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
      TreePath ptn = e.getNewLeadSelectionPath();
      if (ptn != null) {
        SettingsTreeNode node = (SettingsTreeNode)ptn.getLastPathComponent();
        SettingsDialogPModel.this.addPropertyChangeListener(node
            .getPreferences());
        SettingsDialogPModel.this.firePropertyChange(UPDATE_PANEL, null, node
            .getPreferences().getPanel());
      }
    }
  }
}
