package com.easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;


import com.easytournament.basic.model.TSSettingsPanelPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.SportSettings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TSportsSettingsPanel extends JPanel {
  protected TSSettingsPanelPModel pm;

  public TSportsSettingsPanel(TSSettingsPanelPModel pm) {
    this.pm = pm;
    initializePanel();
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(createPanel(), BorderLayout.CENTER);
  }

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(160PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:80PX:NONE,FILL:DEFAULT:NONE,FILL:80PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);
  
    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NUM_PERIODS));
    jpanel1.add(jlabel1, cc.xywh(1, 2, 3, 1));
    
    JLabel jlabel10 = new JLabel();
    jlabel10.setText(ResourceManager.getText(Text.DURATION_PERIOD));
    jpanel1.add(jlabel10, cc.xywh(1, 4, 3, 1));
    
    JSpinner jtextfield17 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_NUMGAMETIMES),1,1,10,1));
    jpanel1.add(jtextfield17, cc.xy(5, 2));
  
    JSpinner jtextfield18 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_MINPERGAMETIME),1,1,1000,1));
    jpanel1.add(jtextfield18, cc.xy(5, 4));
    
    JLabel numOTL = new JLabel();
    numOTL.setText(ResourceManager.getText(Text.NUM_OVERTIMES));
    jpanel1.add(numOTL, cc.xywh(1, 6, 3, 1));
    
    JLabel durOTL = new JLabel();
    durOTL.setText(ResourceManager.getText(Text.DURATION_OVERTIME));
    jpanel1.add(durOTL, cc.xywh(1, 8, 3, 1));
 
    JSpinner numOTSp = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_NUMOVERTIMES),1,1,10,1));
    jpanel1.add(numOTSp, cc.xy(5, 6));
  
    JSpinner durOTSp = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_MINPEROVERTIME),1,1,1000,1));
    jpanel1.add(durOTSp, cc.xy(5, 8));
  
    JLabel jlabel13 = new JLabel();
    jlabel13.setText(ResourceManager.getText(Text.POINTS));
    jpanel1.add(jlabel13, cc.xy(1, 12));
  
    jpanel1.add(new JSeparator(), cc.xywh(1, 10, 5, 1));
  
    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.WIN));
    jpanel1.add(jlabel2, cc.xy(1, 14));
  
    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.DRAW));
    jpanel1.add(jlabel3, cc.xy(1, 16));
  
    JLabel jlabel4 = new JLabel();
    jlabel4.setText(ResourceManager.getText(Text.DEFEAT));
    jpanel1.add(jlabel4, cc.xy(1, 18));
  
    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.WIN_OVERTIME));
    jpanel1.add(jlabel5, cc.xy(1, 20));
  
    JLabel jlabel6 = new JLabel();
    jlabel6.setText(ResourceManager.getText(Text.DRAW_OVERTIME));
    jpanel1.add(jlabel6, cc.xy(1, 22));
  
    JLabel jlabel7 = new JLabel();
    jlabel7.setText(ResourceManager.getText(Text.DEFEAT_OVERTIME));
    jpanel1.add(jlabel7, cc.xy(1, 24));
  
    JLabel jlabel8 = new JLabel();
    jlabel8.setText(ResourceManager.getText(Text.WIN_PENALTY));
    jpanel1.add(jlabel8, cc.xy(1, 26));
  
    JLabel jlabel9 = new JLabel();
    jlabel9.setText(ResourceManager.getText(Text.DEFEAT_PENALTY));
    jpanel1.add(jlabel9, cc.xy(1, 28));    
    
    JLabel jlabel12 = new JLabel();
    jlabel12.setText(ResourceManager.getText(Text.HOME));
    jpanel1.add(jlabel12, cc.xy(3, 12));
  
    JSpinner jtextfield1 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN),0,-1000,1000,1));
    jpanel1.add(jtextfield1, cc.xy(3, 14));
  
    JSpinner jtextfield2 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DRAW),0,-1000,1000,1));
    jpanel1.add(jtextfield2, cc.xy(3, 16));
  
    JSpinner jtextfield3 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT),0,-1000,1000,1));
    jpanel1.add(jtextfield3, cc.xy(3, 18));
  
    JSpinner jtextfield4 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield4, cc.xy(3, 20));
  
    JSpinner jtextfield5 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DRAW_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield5, cc.xy(3, 22));
  
    JSpinner jtextfield6 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield6, cc.xy(3, 24));
  
    JSpinner jtextfield7 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN_P),0,-1000,1000,1));
    jpanel1.add(jtextfield7, cc.xy(3, 26));
  
    JSpinner jtextfield8 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT_P),0,-1000,1000,1));
    jpanel1.add(jtextfield8, cc.xy(3, 28));
  
    JLabel jlabel11 = new JLabel();
    jlabel11.setText(ResourceManager.getText(Text.AWAY));
    jpanel1.add(jlabel11, cc.xy(5, 12));
  
    JSpinner jtextfield9 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN),0,-1000,1000,1));
    jpanel1.add(jtextfield9, cc.xy(5, 14));
  
    JSpinner jtextfield10 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DRAW),0,-1000,1000,1));
    jpanel1.add(jtextfield10, cc.xy(5, 16));
  
    JSpinner jtextfield11 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT),0,-1000,1000,1));
    jpanel1.add(jtextfield11, cc.xy(5, 18));
  
    JSpinner jtextfield12 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield12, cc.xy(5, 20));
  
    JSpinner jtextfield13 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DRAW_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield13, cc.xy(5, 22));
  
    JSpinner jtextfield14 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT_OT),0,-1000,1000,1));
    jpanel1.add(jtextfield14, cc.xy(5, 24));
  
    JSpinner jtextfield15 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN_P),0,-1000,1000,1));
    jpanel1.add(jtextfield15, cc.xy(5, 26));
  
    JSpinner jtextfield16 = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT_P),0,-1000,1000,1));
    jpanel1.add(jtextfield16, cc.xy(5, 28));

  
    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27});
    return jpanel1;
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
