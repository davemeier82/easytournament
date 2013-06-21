package com.easytournament.basic.gui.wizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TournamentTypeWizardPanel extends JPanel {
  JTextField jtextfield1 = new JTextField();
  JRadioButton jradiobutton1 = new JRadioButton();
  ButtonGroup buttongroup1 = new ButtonGroup();
  JRadioButton jradiobutton2 = new JRadioButton();
  JRadioButton jradiobutton3 = new JRadioButton();

  /**
   * Default constructor
   */
  public TournamentTypeWizardPanel() {
    initializePanel();
  }

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:MIN(100DLU;DEFAULT):NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText("Name");
    jpanel1.add(jlabel1, cc.xy(2, 2));

    jpanel1.add(jtextfield1, cc.xy(4, 2));

    jradiobutton1.setActionCommand("Nockout");
    jradiobutton1.setRolloverEnabled(true);
    jradiobutton1.setText("Nockout");
    buttongroup1.add(jradiobutton1);
    jpanel1.add(jradiobutton1, cc.xy(2, 4));

    jradiobutton2.setActionCommand("Group + Nockout");
    jradiobutton2.setRolloverEnabled(true);
    jradiobutton2.setText("Group + Nockout");
    buttongroup1.add(jradiobutton2);
    jpanel1.add(jradiobutton2, cc.xy(2, 6));

    jradiobutton3.setActionCommand("One Group / League");
    jradiobutton3.setRolloverEnabled(true);
    jradiobutton3.setText("One Group / League");
    buttongroup1.add(jradiobutton3);
    jpanel1.add(jradiobutton3, cc.xy(2, 8));

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3,
        4, 5, 6, 7, 8, 9});
    return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
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
