/* StaffDialog.java - Dialog to create and edit the staff of a team
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
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.easytournament.basic.model.dialog.StaffDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Dialog to create and edit the staff of a team
 * @author David Meier
 *
 */
public class StaffDialog extends JDialog implements PropertyChangeListener{

  private static final long serialVersionUID = -7144655054971045528L;
  /**
   * The presentation model
   */
  protected StaffDialogPModel pm;

  /**
   * @param dialog The parent dialog
   * @param pm The presentation model
   * @param modal True if the dialog is modal
   */
  public StaffDialog(Dialog dialog, StaffDialogPModel pm, boolean modal) {
    super(dialog, ResourceManager.getText(Text.STAFF), modal);
    this.pm = pm;
    init();
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        StaffDialog.this.pm.removePropertyChangeListener(StaffDialog.this);
        super.windowClosed(e);
      }      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setLocationRelativeTo(dialog);
    this.setVisible(true);
  }

  /**
   * Initializes the dialog
   */
  private void init() {
    this.pm.addPropertyChangeListener(this);
    Container cpane = this.getContentPane();
    cpane.setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();
    // The first tab contains general information about the person
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO), new PersonPanel(this.pm.getPersonPanelPModel()));
    // The second tab contains staff specific information
    tabbedPane.addTab(ResourceManager.getText(Text.JOB), getStaffPanel());
    
    cpane.add(tabbedPane, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
  }
  
  /**
   * Creates and returns the panel with staff specific information
   * @return The staff panel
   */
  private Component getStaffPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout);
    
    JLabel functionLabel = new JLabel();
    functionLabel.setText(ResourceManager.getText(Text.FUNCTION));
    panel.add(functionLabel, cc.xy(1, 2));
    
    JTextField functionTF = BasicComponentFactory.createTextField(this.pm.getStaffValueModel(Refree.PROPERTY_FUNCTION));
    panel.add(functionTF, cc.xy(3, 2));
    
    addFillComponents(panel, new int[] {1, 2}, new int[] {1, 3});
    
    return panel;
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private Component getButtonPanel() {
    JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(this.pm.getAction(StaffDialogPModel.OK_ACTION));
    JButton cancelBtn = new JButton(this.pm.getAction(StaffDialogPModel.CANCEL_ACTION));
    bPanel.add(okBtn);
    bPanel.add(cancelBtn);
    return bPanel;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(StaffDialogPModel.DISPOSE)){
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }    
  }
  
  /**
   * Adds fill components to empty cells in the first row and first column of
   * the grid. This ensures that the grid spacing will be the same as shown in
   * the designer.
   * @param panel
   * @param cols
   *          an array of column indices in the first row where fill components
   *          should be added.
   * @param rows
   *          an array of row indices in the first column where fill components
   *          should be added.
   */
  void addFillComponents(Container panel, int[] cols, int[] rows) {
    Dimension filler = new Dimension(10, 10);

    boolean filled_cell_11 = false;
    CellConstraints cc = new CellConstraints();
    if (cols.length > 0 && rows.length > 0) {
      if (cols[0] == 1 && rows[0] == 1) {
        /** add a rigid area */
        panel.add(Box.createRigidArea(filler), cc.xy(1, 1));
        filled_cell_11 = true;
      }
    }

    for (int index = 0; index < cols.length; index++) {
      if (cols[index] == 1 && filled_cell_11) {
        continue;
      }
      panel.add(Box.createRigidArea(filler), cc.xy(cols[index], 1));
    }

    for (int index = 0; index < rows.length; index++) {
      if (rows[index] == 1 && filled_cell_11) {
        continue;
      }
      panel.add(Box.createRigidArea(filler), cc.xy(1, rows[index]));
    }
  }
}
