package easytournament.basic.gui.settings;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import easytournament.basic.gui.dialog.SettingsDialog;
import easytournament.basic.model.settings.GeneralSetPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

/**
 * Panel for the general settings
 * @author David Meier
 *
 */
public class GeneralSetPanel extends JPanel {

  private static final long serialVersionUID = 3392434268251975085L;
  /**
   * The jgoodies presentation model
   */
  private PresentationModel<Model> pm;
  /**
   * The presentation model
   */
  private GeneralSetPModel gppm;

  /**
   * Constructor
   * @param pm
   *          Presentation Model
   */
  public GeneralSetPanel(GeneralSetPModel pm) {
    this.gppm = pm;
    this.pm = new PresentationModel<Model>(this.gppm);
    this.init();
  }

  /**
   * Initializes the panel
   */
  private void init() {
    this.setName(ResourceManager.getText(Text.GENERAL));
    this.setPreferredSize(SettingsDialog.PANEL_DIMENSION);
    this.setMinimumSize(new Dimension(50, 50));
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.initComponents();
  }

  /**
   * Initializes the setting components
   */
  private void initComponents() {
    FormLayout layout = new FormLayout("FILL:MAX(100PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE", // columns
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE"); // rows

    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();

    // Language
    builder.addLabel(ResourceManager.getText(Text.LANGUAGE), cc.xy(1, 1));
    ComboBoxAdapter<String> langAdapter = new ComboBoxAdapter<String>(
        (String[])this.pm.getValue(GeneralSetPModel.PROPERTY_LANGLABELS),
        this.pm.getModel(GeneralSetPModel.PROPERTY_LANG));
    @SuppressWarnings("unchecked")
    JComboBox<String> langBox = new JComboBox<String>(langAdapter);
    builder.add(langBox, cc.xy(3, 1));
    
    // Checkbox to enable the update check on startup
    JCheckBox updateCB = BasicComponentFactory.createCheckBox(
        this.pm.getModel(GeneralSetPModel.PROPERTY_CHECKUPDATE),
        ResourceManager.getText(Text.CHECK_UPDATES));
    builder.add(updateCB, cc.xyw(1, 3, 5));

    JPanel panel = builder.getPanel();    
    addFillComponents(panel,new int[]{ 2,4},new int[]{ 2,4});
    
    this.add(panel);
  }

  /**
   * Adds fill components to empty cells in the first row and first column of
   * the grid. This ensures that the grid spacing will be the same as shown in
   * the designer.
   * @param panel the panel
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
