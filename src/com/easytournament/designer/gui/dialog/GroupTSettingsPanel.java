package com.easytournament.designer.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.designer.model.dialog.GroupDialogPModel;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class GroupTSettingsPanel extends JPanel implements ChangeListener {
  protected GroupDialogPModel pm;
  
  protected JCheckBox useDefaultChB;
  protected JSpinner[] spinners = new JSpinner[20];
  protected JComboBox<AbstractGroup> importCB;
  protected JButton importBtn;

  public GroupTSettingsPanel(GroupDialogPModel pm) {
    this.pm = pm;
    initializePanel();
  }
  
  
  /**
   * Initializer
   */
  protected void initializePanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    p.add(getTopPanel(), BorderLayout.NORTH);
    p.add(createPanel(), BorderLayout.CENTER);
    setLayout(new BorderLayout());
    add(new JScrollPane(p), BorderLayout.CENTER);
    setCompsEnabled();
  }


  private Component getTopPanel() {
    JPanel p = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(110PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:220PX:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);
    
    useDefaultChB = BasicComponentFactory.createCheckBox(new PropertyAdapter<GroupDialogPModel>(pm,GroupDialogPModel.PROPERTY_DEFAULT_SET), ResourceManager.getText(Text.USE_DEFAULT_SETTINGS));
    useDefaultChB.addChangeListener(this);
    
    p.add(useDefaultChB, cc.xyw(1, 2, 3));
    
    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.IMPORT_FROM));
    p.add(firstNameLabel, cc.xy(1, 4));
    
    importCB  = new JComboBox<AbstractGroup>(new ComboBoxAdapter<ArrayListModel<AbstractGroup>>((SelectionInList<ArrayListModel<AbstractGroup>>) pm.getSelectionInList(pm.IMPORT_LIST)));
    p.add(importCB, cc.xy(3, 4));
    
    importBtn = new JButton(pm.getAction(GroupDialogPModel.IMPORT_TSET_ACTION));
    p.add(importBtn, cc.xy(5, 4));
    
    addFillComponents(p, new int[] {2, 4}, new int[] {1, 3, 5});
    
    return p;
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
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(160PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:80PX:NONE,FILL:DEFAULT:NONE,FILL:80PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NUM_PERIODS));
    jpanel1.add(jlabel1, cc.xywh(1, 2, 3, 1));

    spinners[16] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_NUMGAMETIMES),1,1,10,1));
    jpanel1.add(spinners[16], cc.xy(5, 2));

    spinners[17] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_MINPERGAMETIME),1,1,1000,1));
    jpanel1.add(spinners[17], cc.xy(5, 4));
    
    JLabel numOTL = new JLabel();
    numOTL.setText(ResourceManager.getText(Text.NUM_OVERTIMES));
    jpanel1.add(numOTL, cc.xywh(1, 6, 3, 1));
    
    JLabel durOTL = new JLabel();
    durOTL.setText(ResourceManager.getText(Text.DURATION_OVERTIME));
    jpanel1.add(durOTL, cc.xywh(1, 8, 3, 1));
 
    spinners[18] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_NUMOVERTIMES),1,1,10,1));
    jpanel1.add(spinners[18], cc.xy(5, 6));
  
    spinners[19] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_MINPEROVERTIME),1,1,1000,1));
    jpanel1.add(spinners[19], cc.xy(5, 8));
    

    JLabel jlabel13 = new JLabel();
    jlabel13.setText(ResourceManager.getText(Text.POINTS));
    jpanel1.add(jlabel13, cc.xy(1, 12));    

    JLabel jlabel12 = new JLabel();
    jlabel12.setText(ResourceManager.getText(Text.HOME));
    jpanel1.add(jlabel12, cc.xy(3, 12));

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

    spinners[0] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN),0,-1000,1000,1));
    jpanel1.add(spinners[0], cc.xy(3, 14));

    spinners[1] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DRAW),0,-1000,1000,1));
    jpanel1.add(spinners[1], cc.xy(3, 16));

    spinners[2] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT),0,-1000,1000,1));
    jpanel1.add(spinners[2], cc.xy(3, 18));

    spinners[3] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN_OT),0,-1000,1000,1));
    jpanel1.add(spinners[3], cc.xy(3, 20));

    spinners[4] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DRAW_OT),0,-1000,1000,1));
    jpanel1.add(spinners[4], cc.xy(3, 22));

    spinners[5] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT_OT),0,-1000,1000,1));
    jpanel1.add(spinners[5], cc.xy(3, 24));

    spinners[6] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_WIN_P),0,-1000,1000,1));
    jpanel1.add(spinners[6], cc.xy(3, 26));

    spinners[7] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_H_DEFEAT_P),0,-1000,1000,1));
    jpanel1.add(spinners[7], cc.xy(3, 28));

    JLabel jlabel10 = new JLabel();
    jlabel10.setText(ResourceManager.getText(Text.DURATION_PERIOD));
    jpanel1.add(jlabel10, cc.xywh(1, 4, 3, 1));

    JLabel jlabel11 = new JLabel();
    jlabel11.setText(ResourceManager.getText(Text.AWAY));
    jpanel1.add(jlabel11, cc.xy(5, 12));

    spinners[8] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN),0,-1000,1000,1));
    jpanel1.add(spinners[8], cc.xy(5, 14));

    spinners[9] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DRAW),0,-1000,1000,1));
    jpanel1.add(spinners[9], cc.xy(5, 16));

    spinners[10] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT),0,-1000,1000,1));
    jpanel1.add(spinners[10], cc.xy(5, 18));

    spinners[11] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN_OT),0,-1000,1000,1));
    jpanel1.add(spinners[11], cc.xy(5, 20));

    spinners[12] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DRAW_OT),0,-1000,1000,1));
    jpanel1.add(spinners[12], cc.xy(5, 22));

    spinners[13] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT_OT),0,-1000,1000,1));
    jpanel1.add(spinners[13], cc.xy(5, 24));

    spinners[14] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_WIN_P),0,-1000,1000,1));
    jpanel1.add(spinners[14], cc.xy(5, 26));

    spinners[15] = new JSpinner(SpinnerAdapterFactory
        .createNumberAdapter(pm
        .getSettingsValueModel(SportSettings.PROPERTY_POINTPER_A_DEFEAT_P),0,-1000,1000,1));
    jpanel1.add(spinners[15], cc.xy(5, 28));

    jpanel1.add(new JSeparator(), cc.xywh(1, 10, 5, 1));

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27});
    return jpanel1;
  }
  
  private void setCompsEnabled() {
    boolean enabled = !this.useDefaultChB.isSelected();
    for(int i = 0; i < spinners.length; i++){
      spinners[i].setEnabled(enabled);
    }
    this.importCB.setEnabled(enabled);
    this.importBtn.setEnabled(enabled);
  }


  @Override
  public void stateChanged(ChangeEvent e) {
    setCompsEnabled();    
  }

}
