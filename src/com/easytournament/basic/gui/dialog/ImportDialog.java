/* ImportDialog.java - Dialog to choose the item to import
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.easytournament.basic.model.dialog.ImportDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.jgoodies.binding.adapter.BasicComponentFactory;


public class ImportDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = -4399000176669371622L;
  /**
   * The presentation model
   */
  protected ImportDialogPModel pm;

  /**
   * Constructor
   * @param f
   * @param modal
   * @param pm
   */
  public ImportDialog(Frame f, boolean modal, ImportDialogPModel pm) {
    super(f, ResourceManager.getText(Text.SELECT_IMPORT_ITEM), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setMinimumSize(new Dimension(450,110));
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener if the window is closed
        ImportDialog.this.pm.removePropertyChangeListener(ImportDialog.this);
        super.windowClosed(e);
      }
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializes the dialog panels
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createMainPanel(), BorderLayout.CENTER);
    add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(ImportDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(ImportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  /**
   * Creates the main panel
   * @return The main panel
   */
  public Component createMainPanel() {
    Box vBox = Box.createVerticalBox();
    @SuppressWarnings("unchecked")
    JComboBox<String> exportCombo = BasicComponentFactory.createComboBox(this.pm
        .getSelectionInList(ImportDialogPModel.IMPORT_LIST));
    vBox.add(exportCombo);
    return vBox;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(ImportDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

}
