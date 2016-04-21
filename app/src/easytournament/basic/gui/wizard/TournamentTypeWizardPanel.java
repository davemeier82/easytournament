package easytournament.basic.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import easytournament.basic.model.wizard.TournamentTypeWizardModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.tournamentwizard.TournamentType;

public class TournamentTypeWizardPanel extends JPanel {

  private static final long serialVersionUID = -3447721006176258082L;
  private JTextField nameTextfield;
  private JRadioButton nockoutRadiobutton;
  private JRadioButton groupNockoutRadiobutton;
  private JRadioButton leagueRadiobutton;

  private PresentationModel<Model> pm;
  private TournamentTypeWizardModel model;

  /**
   * Default constructor
   */
  public TournamentTypeWizardPanel(TournamentTypeWizardModel model) {
    this.model = model;
    this.pm = new PresentationModel<Model>(model);
    initializePanel();
  }

  public JPanel createPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:10PX:NONE,FILL:MAX(200PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout1);

    if (this.model.isnewTournament()) {
      JLabel nameLabel = new JLabel(ResourceManager.getText(Text.NAME));
      panel.add(nameLabel, cc.xy(1, 2));

      this.nameTextfield = BasicComponentFactory.createTextField(this.pm
          .getModel(TournamentTypeWizardModel.PROPERTY_NAME));
      panel.add(this.nameTextfield, cc.xy(3, 2));
    }

    this.nockoutRadiobutton = BasicComponentFactory.createRadioButton(
        this.pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE),
        TournamentType.KNOCKOUT, ResourceManager.getText(Text.KNOCKOUT));
    panel.add(this.nockoutRadiobutton, cc.xy(1, 4));

    this.groupNockoutRadiobutton = BasicComponentFactory.createRadioButton(
        this.pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE),
        TournamentType.GROUP_KNOCKOUT,
        ResourceManager.getText(Text.GROUP_AND_KNOCKOUT));
    panel.add(this.groupNockoutRadiobutton, cc.xy(1, 6));

    this.leagueRadiobutton = BasicComponentFactory.createRadioButton(
        this.pm.getModel(TournamentTypeWizardModel.PROPERTY_TOURNAMENTTYPE),
        TournamentType.GROUP, ResourceManager.getText(Text.GROUP_LEAGUE));
    panel.add(this.leagueRadiobutton, cc.xy(1, 8));

    addFillComponents(panel, new int[] {1, 2, 3}, new int[] {1, 2, 3, 4, 5, 6,
        7, 8, 9});
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
