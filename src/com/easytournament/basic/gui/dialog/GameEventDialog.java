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

public class GameEventDialog extends JDialog implements PropertyChangeListener,
    ChangeListener {

  private static final long serialVersionUID = 4775688030231693269L;
  protected GEventDialogPModel pm;
  protected JTextField textTF;
  protected JCheckBox secondaryChB;

  /**
   * Default constructor
   */
  public GameEventDialog(Frame f, boolean modal, GEventDialogPModel pm) {
    super(f, ResourceManager.getText(Text.GAMEEVENT), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.pack();
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        GameEventDialog.this.pm
            .removePropertyChangeListener(GameEventDialog.this);
        super.windowClosed(e);
      }

    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
    add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

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

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NAME));
    jpanel1.add(jlabel1, cc.xy(1, 2));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.TYPE));
    jpanel1.add(jlabel2, cc.xy(1, 4));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.TEAM));
    jpanel1.add(jlabel3, cc.xy(3, 6));

    JLabel jlabel4 = new JLabel();
    jlabel4.setText(ResourceManager.getText(Text.OPPONENT));
    jpanel1.add(jlabel4, cc.xy(5, 6));

    JTextField nameTF = BasicComponentFactory.createTextField(pm
        .getGameEventValueModel(GameEvent.PROPERTY_NAME));
    nameTF.setEditable(pm.isNameEditable());
    jpanel1.add(nameTF, cc.xy(3, 2));

    JSpinner pointsTeamS = new JSpinner(
        SpinnerAdapterFactory.createNumberAdapter(
            pm.getGameEventValueModel(GameEvent.PROPERTY_POINTS_TEAM), 0,
            -1000, +1000, 1));
    jpanel1.add(pointsTeamS, cc.xy(3, 8));

    JSpinner pointsOppS = new JSpinner(
        SpinnerAdapterFactory.createNumberAdapter(
            pm.getGameEventValueModel(GameEvent.PROPERTY_POINTS_OPPONENT), 0,
            -1000, +1000, 1));
    jpanel1.add(pointsOppS, cc.xy(5, 8));

    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.POINTS));
    jpanel1.add(jlabel5, cc.xy(1, 8));

    JComboBox<String> typeCb = BasicComponentFactory.createComboBox(pm
        .getTypeSelectionInList());
    jpanel1.add(typeCb, cc.xy(3, 4));

    secondaryChB = BasicComponentFactory.createCheckBox(
        pm.getGameEventValueModel(GameEvent.PROPERTY_SECONDARY_PLAYER),
        ResourceManager.getText(Text.EVENT_SECOND_PLAYER));
    secondaryChB.setEnabled(pm.isNameEditable());
    jpanel1.add(secondaryChB, cc.xyw(1, 10, 5));
    secondaryChB.addChangeListener(this);

    JLabel jlabel6 = new JLabel();
    jlabel6.setText(ResourceManager.getText(Text.TEXT));
    jpanel1.add(jlabel6, cc.xy(1, 12));

    textTF = BasicComponentFactory.createTextField(pm
        .getGameEventValueModel(GameEvent.PROPERTY_SECONDARY_TEXT));
    textTF.setEditable(pm.isNameEditable() && secondaryChB.isSelected());
    jpanel1.add(textTF, cc.xyw(3, 12, 3));

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        6, 7, 9, 11});
    return jpanel1;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
      this.dispose();
    }

  }

  @Override
  public void stateChanged(ChangeEvent e) {
    textTF.setEditable(secondaryChB.isSelected());
  }

}
