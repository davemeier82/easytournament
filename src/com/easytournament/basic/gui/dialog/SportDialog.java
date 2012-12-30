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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import com.easytournament.basic.model.dialog.SportDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Sport;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SportDialog extends JDialog implements PropertyChangeListener {

  private SportDialogPModel pm;

  public SportDialog(Frame f, boolean modal, SportDialogPModel pm) {
    super(f, ResourceManager.getText(Text.SPORT), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    this.init();
    this.pack();
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        SportDialog.this.pm.removePropertyChangeListener(SportDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  private void init() {
    setLayout(new BorderLayout());
    add(getCenterPanel(), BorderLayout.CENTER);
    add(getButtonPanel(), BorderLayout.SOUTH);
  }
  
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(SportDialogPModel.OK_ACTION)); 
    JButton cancel = new JButton(this.pm.getAction(SportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  private Component getCenterPanel() {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(DEFAULT;90PX):NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);

    JLabel nameLabel = new JLabel(ResourceManager.getText(Text.NAME));
    p.add(nameLabel, cc.xy(1, 2));

    JTextField nameTf = BasicComponentFactory
        .createTextField(new PropertyAdapter<SportDialogPModel>(pm,
            SportDialogPModel.PROPERTY_NAME));
    nameTf.setEditable(pm.isNameEditable());
    p.add(nameTf, cc.xy(3, 2));

    JLabel fromLabel = new JLabel(ResourceManager.getText(Text.IMPORTFROM));
    p.add(fromLabel, cc.xy(3, 4));

    JLabel settingsLabel = new JLabel(ResourceManager.getText(Text.GAME_DUR_POINTS));
    p.add(settingsLabel, cc.xy(1, 6));

    JComboBox<Sport> settCB = new JComboBox<Sport>(new ComboBoxAdapter<Sport>(
        pm.getList(), new PropertyAdapter<SportDialogPModel>(pm,
            SportDialogPModel.PROPERTY_SETTINGS_IMPORT)));
    p.add(settCB, cc.xy(3, 6));

    JLabel rulesLabels = new JLabel(ResourceManager.getText(Text.RULES));
    p.add(rulesLabels, cc.xy(1, 8));

    JComboBox<Sport> rulesCB = new JComboBox<Sport>(new ComboBoxAdapter<Sport>(
        pm.getList(), new PropertyAdapter<SportDialogPModel>(pm,
            SportDialogPModel.PROPERTY_RULES_IMPORT)));
    p.add(rulesCB, cc.xy(3, 8));

    JLabel eventsLabel = new JLabel(ResourceManager.getText(Text.EVENTS));
    p.add(eventsLabel, cc.xy(1, 10));

    JComboBox<Sport> eventsCB = new JComboBox<Sport>(
        new ComboBoxAdapter<Sport>(pm.getList(),
            new PropertyAdapter<SportDialogPModel>(pm,
                SportDialogPModel.PROPERTY_EVENTS_IMPORT)));
    p.add(eventsCB, cc.xy(3, 10));

    addFillComponents(p, new int[] {1, 2, 3}, new int[] {1, 3, 5, 7, 9});

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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(SportDialogPModel.DISPOSE)){
      pm.removePropertyChangeListener(this);
      this.dispose();
    }
    
  }
}
