package com.easytournament.basic.gui.wizard;

import com.easytournament.basic.model.wizard.TournamentTypeWizardModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.tournamentwizard.TournamentType;
import com.easytournament.designer.model.settings.PageSetPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TournamentTypeWizardPanel extends JPanel {
  private JTextField jtextfield1;
  private JRadioButton jradiobutton1;
  private JRadioButton jradiobutton2;
  private JRadioButton jradiobutton3;
  
  private TournamentTypeWizardModel model;
  private PresentationModel<Model> pm;

  /**
   * Default constructor
   */
  public TournamentTypeWizardPanel(TournamentTypeWizardModel model) {
    this.model = model;
    this.pm = new PresentationModel<Model>(model);
    initializePanel();
  }

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:10PX:NONE,FILL:MAX(200PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel("Name");
    jpanel1.add(jlabel1, cc.xy(1, 2));

    jtextfield1 = BasicComponentFactory.createTextField(pm.getModel(TournamentTypeWizardModel.PROPERTY_NAME));
    jpanel1.add(jtextfield1, cc.xy(3, 2));

    jradiobutton1 = BasicComponentFactory.createRadioButton(
        pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE), TournamentType.NOCKOUT,
        "Nockout");
    jpanel1.add(jradiobutton1, cc.xy(1, 4));
    
    jradiobutton2 = BasicComponentFactory.createRadioButton(
        pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE), TournamentType.GROUP_NOCKOUT,
        "Group + Nockout");
    jpanel1.add(jradiobutton2, cc.xy(1, 6));

    jradiobutton3 = BasicComponentFactory.createRadioButton(
        pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE), TournamentType.GROUP,
        "One Group / League");
    jpanel1.add(jradiobutton3, cc.xy(1, 8));

    addFillComponents(jpanel1, new int[] {1, 2, 3}, new int[] {1, 2, 3,
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
