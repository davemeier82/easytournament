/* PlayerDialog.java - Dialog to create or edit a player
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

import com.easytournament.basic.model.dialog.PlayerDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Player;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Dialog to add and edit a player
 * @author David Meier
 *
 */
public class PlayerDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = -5127725018049651082L;
  /**
   * The presentation model
   */
  protected PlayerDialogPModel pm;
  
  /**
   * Constructor
   * @param owner The owner of the dialog
   * @param pm The presentation model
   * @param modal True if the dialog is modal
   */
  public PlayerDialog(Dialog owner, PlayerDialogPModel pm, boolean modal) {
    super(owner, ResourceManager.getText(Text.PLAYER), modal);
    this.pm = pm;
    init();
    this.setLocationRelativeTo(owner);
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        PlayerDialog.this.pm.removePropertyChangeListener(PlayerDialog.this);
        super.windowClosed(e);
      }
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
    // the first tab contains the person panel
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO),
        new PersonPanel(this.pm.getPersonPanelPModel()));
    // the second tab is used for player specific information
    tabbedPane.addTab(ResourceManager.getText(Text.PLAYER), getPlayerPanel());

    cpane.add(tabbedPane, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
    this.pack();
  }

  /**
   * Creates the panel for player specific information
   * @return The player panel
   */
  private Component getPlayerPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout1);

    JLabel genderLabel = new JLabel();
    genderLabel.setText(ResourceManager.getText(Text.NUMBER));
    panel.add(genderLabel, cc.xy(1, 2));

    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.POSITION));
    panel.add(firstNameLabel, cc.xy(1, 4));

    JTextField numberTF = BasicComponentFactory.createTextField(this.pm
        .getPlayerValueModel(Player.PROPERTY_NR));
    panel.add(numberTF, cc.xy(3, 2));

    JTextField positionTF = BasicComponentFactory.createTextField(this.pm
        .getPlayerValueModel(Player.PROPERTY_POSITION));
    panel.add(positionTF, cc.xy(3, 4));

    addFillComponents(panel, new int[] {1, 2, 3}, new int[] {1, 3});

    return panel;
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private Component getButtonPanel() {
    JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(this.pm.getAction(PlayerDialogPModel.OK_ACTION));
    JButton cancelBtn = new JButton(
        this.pm.getAction(PlayerDialogPModel.CANCEL_ACTION));
    bPanel.add(okBtn);
    bPanel.add(cancelBtn);
    return bPanel;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(PlayerDialogPModel.DISPOSE)) {
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
