package com.easytournament.basic.settings;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.jdom.Element;

public interface Settings extends PropertyChangeListener {

  /**
   * @return TreeNode for the tree in the Preferences-Dialog
   */
  public SettingsTreeNode getNode();

  /**
   * @return Panel which will be shown in the Preferences-Dialog if the Node is
   *         selected
   */
  public JComponent getPanel();

  public void read(Element xml);

  public void save(Element xml);
}
