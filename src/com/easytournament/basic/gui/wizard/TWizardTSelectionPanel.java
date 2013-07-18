package com.easytournament.basic.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListModel;

import com.easytournament.basic.model.settings.GeneralSetPModel;
import com.easytournament.basic.model.wizard.TWizardTSelectionModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.tournamentwizard.TournamentSelector;
import com.easytournament.designer.model.settings.GridSetPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TWizardTSelectionPanel extends JPanel {
  JSpinner jspinner1 = new JSpinner();
  JCheckBox jcheckbox1;
  JComboBox<Integer> jcombobox1;
  JComboBox<String> jcombobox2;

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
    JPanel jpanel1 = new JPanel();
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:10PX:NONE,FILL:MAX(150PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel("Number of teams");
    jpanel1.add(jlabel1, cc.xy(1, 2));

    jspinner1 = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(TWizardTSelectionModel.PROPERTY_NTEAMS), 16,
        TournamentSelector.getMinNumberOfTeams(),
        TournamentSelector.getMaxNumberOfTeams(), 1));
    jpanel1.add(jspinner1, cc.xy(3, 2));

    jcheckbox1 = BasicComponentFactory.createCheckBox(this.pm
        .getModel(TWizardTSelectionModel.PROPERTY_BRONCEMEDALGAMEENABLED),
        "Add Bronce Medal Game");
    jpanel1.add(jcheckbox1, cc.xywh(1, 4, 3, 1));

    ComboBoxAdapter<Integer> nGroupsAdapter = new ComboBoxAdapter<Integer>(
        this.model.getGrouplist(),
        this.pm.getModel(TWizardTSelectionModel.PROPERTY_NGROUPS));

    jcombobox1 = new JComboBox<Integer>(nGroupsAdapter);
    jpanel1.add(jcombobox1, cc.xy(3, 6));

    JLabel jlabel2 = new JLabel("Number of Nockout stages");
    jpanel1.add(jlabel2, cc.xy(1, 8));
    
    ComboBoxAdapter<Integer> nNOStagesAdapter = new ComboBoxAdapter<Integer>(
        this.model.getNockoutstageslist(),
        this.pm.getModel(TWizardTSelectionModel.PROPERTY_NNOCKOUTSTAGES));
    
    jcombobox2 = new JComboBox<String>(nNOStagesAdapter);
    jcombobox2.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        Integer stage = (Integer)value;
        String label;
        switch (stage) {
          case 1:
            label = "Final";
            break;
          case 2:
            label = "Semi final";
            break;
          case 3:
            label = "Quarter final";
            break;
          case 4:
            label = "Best 16";
            break;
          case 5:
            label = "Best 32";
            break;
          default:
            label = "";
        }

        return super.getListCellRendererComponent(list, label, index,
            isSelected, cellHasFocus);
      }
    });

    jpanel1.add(jcombobox2, cc.xy(3, 8));

    JLabel jlabel3 = new JLabel("Number of Groups");
    jpanel1.add(jlabel3, cc.xy(1, 6));

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5, 6, 7, 8}, new int[] {
        1, 2, 3, 4, 5, 6, 7, 8, 9});
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
