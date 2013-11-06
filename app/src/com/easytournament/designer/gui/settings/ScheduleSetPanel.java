package com.easytournament.designer.gui.settings;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.easytournament.basic.gui.dialog.SettingsDialog;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.model.settings.ScheduleSetPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScheduleSetPanel extends JPanel {

  private PresentationModel<Model> pm;
  private ScheduleSetPModel gppm;

  /**
   * Constructor
   * @param pm
   *          Presentation Model
   */
  public ScheduleSetPanel(ScheduleSetPModel pm) {
    this.gppm = pm;
    this.pm = new PresentationModel<Model>(gppm);
    this.init();

  }

  /**
   * 
   */
  private void init() {
    this.setName(ResourceManager.getText(Text.SCHEDULE));
    this.setPreferredSize(SettingsDialog.PANEL_DIMENSION);
    this.setMinimumSize(new Dimension(50, 50));
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.initComponents();
  }

  /**
   * 
   */
  private void initComponents() {
    JPanel p = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);

    JCheckBox refreeBox = BasicComponentFactory.createCheckBox(
        pm.getModel(ScheduleSetPModel.PROPERTY_SHOWREFREES),
        ResourceManager.getText(Text.SHOW_REFEREES));
    p.add(refreeBox, cc.xy(1, 1));

    addFillComponents(p, new int[] {2}, new int[] {2});

    this.add(p);
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
