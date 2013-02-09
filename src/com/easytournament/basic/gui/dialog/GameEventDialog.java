/* GameEventDialog.java - Dialog to create or edit game events
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.easytournament.basic.model.dialog.GEventDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This dialog allows to create/edit game events
 * @author David Meier
 *
 */
public class GameEventDialog extends JDialog implements PropertyChangeListener,
    ChangeListener {

  private static final long serialVersionUID = 4775688030231693269L;
  /**
   * The presentation model
   */
  protected GEventDialogPModel pm;
  /**
   * Text field for the secondary player (assist,...)
   */
  protected JTextField secondaryTF;
  /**
   * Checkbox to activate the secondary player
   */
  protected JCheckBox secondaryChB;

  /**
   * @param frame The parent frame
   * @param modal True if the dialog is modal
   * @param pm The presentation model
   */
  public GameEventDialog(Frame frame, boolean modal, GEventDialogPModel pm) {
    super(frame, ResourceManager.getText(Text.GAMEEVENT), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.pack();
    this.setLocationRelativeTo(frame);
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener if the dialog is closed
        GameEventDialog.this.pm
            .removePropertyChangeListener(GameEventDialog.this);
        super.windowClosed(e);
      }
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initialize the dialog panels
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createMainPanel(), BorderLayout.CENTER);
    add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

  /**
   * Creates the panel with the OK and cancel buttons
   * @return the button panel
   */
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(GEventDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(GEventDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  /**
   * Creates the main panel
   * @return the main panel
   */
  public JPanel createMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    mainPanel.setLayout(formlayout1);

    JLabel nameLabel = new JLabel();
    nameLabel.setText(ResourceManager.getText(Text.NAME));
    mainPanel.add(nameLabel, cc.xy(1, 2));    

    JTextField nameTF = BasicComponentFactory.createTextField(this.pm
        .getGameEventValueModel(GameEvent.PROPERTY_NAME));
    nameTF.setEditable(this.pm.isNameEditable());
    mainPanel.add(nameTF, cc.xy(3, 2));

    JLabel typeLabel = new JLabel();
    typeLabel.setText(ResourceManager.getText(Text.TYPE));
    mainPanel.add(typeLabel, cc.xy(1, 4));

    JLabel teamLabel = new JLabel();
    teamLabel.setText(ResourceManager.getText(Text.TEAM));
    mainPanel.add(teamLabel, cc.xy(3, 6));

    JLabel opponentLabel = new JLabel();
    opponentLabel.setText(ResourceManager.getText(Text.OPPONENT));
    mainPanel.add(opponentLabel, cc.xy(5, 6));

    JSpinner pointsTeamS = new JSpinner(
        SpinnerAdapterFactory.createNumberAdapter(
            this.pm.getGameEventValueModel(GameEvent.PROPERTY_POINTS_TEAM), 0,
            -1000, +1000, 1));
    mainPanel.add(pointsTeamS, cc.xy(3, 8));

    JSpinner pointsOppS = new JSpinner(
        SpinnerAdapterFactory.createNumberAdapter(
            this.pm.getGameEventValueModel(GameEvent.PROPERTY_POINTS_OPPONENT), 0,
            -1000, +1000, 1));
    mainPanel.add(pointsOppS, cc.xy(5, 8));

    JLabel pointsLabel = new JLabel();
    pointsLabel.setText(ResourceManager.getText(Text.POINTS));
    mainPanel.add(pointsLabel, cc.xy(1, 8));

    @SuppressWarnings("unchecked")
    JComboBox<String> typeCb = BasicComponentFactory.createComboBox(this.pm
        .getTypeSelectionInList());
    mainPanel.add(typeCb, cc.xy(3, 4));

    this.secondaryChB = BasicComponentFactory.createCheckBox(
        this.pm.getGameEventValueModel(GameEvent.PROPERTY_SECONDARY_PLAYER),
        ResourceManager.getText(Text.EVENT_SECOND_PLAYER));
    this.secondaryChB.setEnabled(this.pm.isNameEditable());
    mainPanel.add(this.secondaryChB, cc.xyw(1, 10, 5));
    this.secondaryChB.addChangeListener(this);

    JLabel textLabel = new JLabel();
    textLabel.setText(ResourceManager.getText(Text.TEXT));
    mainPanel.add(textLabel, cc.xy(1, 12));

    this.secondaryTF = BasicComponentFactory.createTextField(this.pm
        .getGameEventValueModel(GameEvent.PROPERTY_SECONDARY_TEXT));
    this.secondaryTF.setEditable(this.pm.isNameEditable() && this.secondaryChB.isSelected());
    mainPanel.add(this.secondaryTF, cc.xyw(3, 12, 3));

    addFillComponents(mainPanel, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        6, 7, 9, 11});
    return mainPanel;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    this.secondaryTF.setEditable(this.secondaryChB.isSelected());
  }

  /**
   * Adds fill components to empty cells in the first row and first column of
   * the grid. This ensures that the grid spacing will be the same as shown in
   * the designer.
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
