package com.easytournament.designer.gui.dialog;

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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.model.dialog.GroupDialogPModel;
import com.easytournament.designer.valueholder.GroupType;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class GroupDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private GroupDialogPModel gdpm;
  private PresentationModel<Model> pm;

  public GroupDialog(Frame f, boolean modal, GroupDialogPModel pm) {
    super(f, "", modal);
    this.gdpm = pm;
    this.gdpm.addPropertyChangeListener(this);
    this.pm = new PresentationModel<Model>(pm);
    this.init();
    this.setSize(800, 600);
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        GroupDialog.this.pm.removePropertyChangeListener(GroupDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);    
  }

  private void init() {
    this.setTitle(gdpm.getName());
   
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());    
    JTabbedPane tpane = new JTabbedPane();
    tpane.addTab(ResourceManager.getText(Text.GROUP_SETTINGS), this.createPanel());
    tpane.addTab(ResourceManager.getText(Text.GAME_DUR_POINTS), new GroupTSettingsPanel(gdpm));
    tpane.addTab(ResourceManager.getText(Text.RULES), new GroupRulesPanel(gdpm));
    cp.add(tpane, BorderLayout.CENTER);
    cp.add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

  public JPanel createPanel() {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);
    
    JLabel genderLabel = new JLabel();
    genderLabel.setText(ResourceManager.getText(Text.NAME));
    p.add(genderLabel, cc.xy(1, 2));
    
    JTextField nameTF = BasicComponentFactory.createTextField(pm
        .getModel(GroupDialogPModel.PROPERTY_NAME));
    p.add(nameTF, cc.xy(3, 2));
    
    if(gdpm.getGroupType().equals(GroupType.NORMAL)){

      JLabel firstNameLabel = new JLabel();
      firstNameLabel.setText(ResourceManager.getText(Text.NUM_TEAMS));
      p.add(firstNameLabel, cc.xy(1, 4));
      
      JSpinner numposSpinner = new JSpinner(SpinnerAdapterFactory
          .createNumberAdapter(pm.getModel(GroupDialogPModel.PROPERTY_NUM_POS),
              4, 2, 40, 1));
      p.add(numposSpinner, cc.xy(3, 4));
    }    
    
    addFillComponents(p, new int[] {1, 2, 3}, new int[] {1, 3});

    return p;
  }

  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.gdpm.getAction(GroupDialogPModel.OK_ACTION)); 
    JButton cancel = new JButton(this.gdpm.getAction(GroupDialogPModel.CANCEL_ACTION));
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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName() == GroupDialogPModel.PROPERTY_DISPOSE) {
      pm.removePropertyChangeListener(this);
      this.dispose();   
    }
  }
}
