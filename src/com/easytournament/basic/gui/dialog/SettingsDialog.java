/* SettingsDialog.java - Dialog for application settings
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.model.dialog.SettingsDialogPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;


/**
 * Dialog for application settings
 * This class is a singleton
 * @author David Meier
 *
 */
public class SettingsDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  /**
   * 
   */
  private static final Dimension DIALOG_SIZE = new Dimension(800, 600);
  /**
   * 
   */
  private static final Dimension TREE_SIZE = new Dimension(200, -1);
  /**
   * 
   */
  public static final Dimension PANEL_DIMENSION = new Dimension(570, 480);
  /**
   * 
   */
  private static SettingsDialog instance;
  /**
   * 
   */
  protected JTree tree;
  /**
   * 
   */
  private SettingsDialogPModel pm;
  /**
   * 
   */
  private JLabel titleLabel;
  /**
   * 
   */
  private JViewport jvp;

  /**
   * @param pm
   */
  private SettingsDialog(SettingsDialogPModel pm) {
    super(Organizer.getInstance().getMainFrame(), ResourceManager.getText(Text.SETTINGS_MENU), true);
    this.pm = pm;
    pm.addPropertyChangeListener(this);
    this.init();
    this.tree.setSelectionRow(0);
    this.setLocationRelativeTo(Organizer.getInstance().getMainFrame());
  }

  /**
   * 
   */
  private void init() {
    this.setSize(DIALOG_SIZE);
    Container pane = this.getContentPane();
    pane.setLayout(new BorderLayout());
    this.tree = new JTree(this.pm.getTreeModel());
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setOpenIcon(ResourceManager.getIcon(Icon.CONTRACT_ICON_SMALL));
    renderer.setClosedIcon(ResourceManager.getIcon(Icon.EXPAND_ICON_SMALL));
    renderer.setLeafIcon(ResourceManager.getIcon(Icon.TRANSPARENT_ICON_SMALL));
    this.tree.setCellRenderer(renderer);
    this.tree.setOpaque(false);
    JScrollPane spane = new JScrollPane(this.tree);
    //spane.getViewport().setBackground(Color.WHITE);
    spane.getViewport().setOpaque(false);
    spane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    this.tree.setPreferredSize(TREE_SIZE);
    this.add(spane, BorderLayout.WEST);
    this.tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    this.tree.setRootVisible(false);
    //tree.setBackground(Color.white);
    this.tree.addTreeSelectionListener(this.pm.getTreeSelectionListener());
    this.tree.addMouseListener(new MouseAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 1) {
          TreePath tp = SettingsDialog.this.tree.getPathForLocation(e.getX(), e.getY());
          if(SettingsDialog.this.tree.isCollapsed(tp))
            SettingsDialog.this.tree.expandPath(tp);
          else
            SettingsDialog.this.tree.collapsePath(tp);
        } else {
          super.mouseClicked(e);
        }
      }
      
    });
    this.add(getCenterPanel(), BorderLayout.CENTER);
    this.add(getButtonPanel(), BorderLayout.SOUTH);

  }

  /**
   * @return
   */
  private JComponent getCenterPanel() {
    Box box = Box.createVerticalBox();
    this.titleLabel = new JLabel();
    this.titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(Font.BOLD, this.titleLabel.getFont().getSize()+2));
    Box titleBox = Box.createHorizontalBox();
    titleBox.add(this.titleLabel);
    titleBox.add(Box.createHorizontalGlue());
    box.add(titleBox);
    JScrollPane pane = new JScrollPane();
    this.jvp = pane.getViewport();
    box.add(pane);
    box.add(Box.createHorizontalGlue());
    return box;
  }

  /**
   * @return
   */
  private JPanel getButtonPanel() {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(this.pm.getOKAction());
    buttonPanel.add(okBtn);
    JButton cancelBtn = new JButton(this.pm.getCancelAction());
    buttonPanel.add(cancelBtn);
    int width = Math.max(okBtn.getPreferredSize().width,
        cancelBtn.getPreferredSize().width);
    okBtn
        .setPreferredSize(new Dimension(width, okBtn.getPreferredSize().height));
    cancelBtn.setPreferredSize(new Dimension(width, cancelBtn
        .getPreferredSize().height));
    return buttonPanel;
  }


  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == SettingsDialogPModel.UPDATE_PANEL) {
      JComponent comp = (JComponent)evt.getNewValue();
      this.titleLabel.setText(comp.getName());
      this.jvp.setView((JComponent)evt.getNewValue());
    }
    if (evt.getPropertyName() == SettingsDialogPModel.DISPOSE) {
      this.dispose();
    }
  }

  /**
   * @param pm
   */
  public static void showPreferencesDialog(SettingsDialogPModel pm) {
    instance = new SettingsDialog(pm);
    instance.setVisible(true);
  }

  /**
   * @return
   */
  public static SettingsDialog getInstance() {
    return SettingsDialog.instance;
  }
}
