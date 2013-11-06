package com.easytournament.basic.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.easytournament.basic.model.wizard.TWizardTSelectionModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.tournamentwizard.TournamentSelector;
import com.easytournament.basic.tournamentwizard.TournamentType;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TWizardTSelectionPanel extends JPanel {

  private static final long serialVersionUID = 6108397937456041214L;
  JSpinner nTeamsSpinner;
  JCheckBox bronceMedalGameCheckbox;
  JComboBox<Integer> nGroupsCombobox;
  JComboBox<String> nKOStagesCombobox;

  private TWizardTSelectionModel model;
  private PresentationModel<Model> pm;

  /**
   * Default constructor
   */
  public TWizardTSelectionPanel(TWizardTSelectionModel model) {
    this.model = model;
    this.pm = new PresentationModel<Model>(model);
    initializePanel();
  }

  public JPanel createPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:10PX:NONE,FILL:MAX(150PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout1);

    JLabel nTeamsLabel = new JLabel(ResourceManager.getText(Text.NUM_TEAMS));
    panel.add(nTeamsLabel, cc.xy(1, 2));

    nTeamsSpinner = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(TWizardTSelectionModel.PROPERTY_NTEAMS), 16,
        TournamentSelector.getMinNumberOfTeams(),
        TournamentSelector.getMaxNumberOfTeams(), 1));
    panel.add(nTeamsSpinner, cc.xy(3, 2));
    
    if (this.model.getTournamentType() == TournamentType.GROUP_KNOCKOUT
        || this.model.getTournamentType() == TournamentType.KNOCKOUT) {

      bronceMedalGameCheckbox = BasicComponentFactory.createCheckBox(this.pm
          .getModel(TWizardTSelectionModel.PROPERTY_BRONCEMEDALGAMEENABLED),
          ResourceManager.getText(Text.ADD_BRONCEMEDAL_GAME));
      panel.add(bronceMedalGameCheckbox, cc.xywh(1, 4, 3, 1));
      
      if (this.model.getTournamentType() == TournamentType.GROUP_KNOCKOUT) {     
    
        JLabel nKnockoutStagesLabel = new JLabel(
            ResourceManager.getText(Text.NUM_KOSTAGES));
        panel.add(nKnockoutStagesLabel, cc.xy(1, 6));
  
        ComboBoxAdapter<Integer> nKOStagesAdapter = new ComboBoxAdapter<Integer>(
            this.model.getKnockoutstageslist(),
            this.pm.getModel(TWizardTSelectionModel.PROPERTY_NKNOCKOUTSTAGES));
  
        nKOStagesCombobox = new JComboBox<String>(nKOStagesAdapter);
        nKOStagesCombobox.setRenderer(new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(JList list, Object value,
              int index, boolean isSelected, boolean cellHasFocus) {
            Integer stage = (Integer)value;
            String label;
            switch (stage) {
              case 1:
                label = ResourceManager.getText(Text.FINAL);
                break;
              case 2:
                label = ResourceManager.getText(Text.SEMI_FINAL);
                break;
              case 3:
                label = ResourceManager.getText(Text.QUARTER_FINAL);
                break;
              case 4:
                label = ResourceManager.getText(Text.BEST16);
                break;
              case 5:
                label = ResourceManager.getText(Text.BEST32);
                break;
              default:
                label = "";
            }
  
            return super.getListCellRendererComponent(list, label, index,
                isSelected, cellHasFocus);
          }
        });
      
        panel.add(nKOStagesCombobox, cc.xy(3, 6));        

        JLabel jlabel3 = new JLabel(ResourceManager.getText(Text.NUM_GROUPS));
        panel.add(jlabel3, cc.xy(1, 8));

        ComboBoxAdapter<Integer> nGroupsAdapter = new ComboBoxAdapter<Integer>(
            this.model.getGrouplist(),
            this.pm.getModel(TWizardTSelectionModel.PROPERTY_NGROUPS));

        nGroupsCombobox = new JComboBox<Integer>(nGroupsAdapter);
        panel.add(nGroupsCombobox, cc.xy(3, 8)); 
      }
    }

    addFillComponents(panel, new int[] {1, 2, 3, 4, 5, 6, 7, 8}, new int[] {1,
        2, 3, 4, 5, 6, 7, 8, 9});
    return panel;
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
