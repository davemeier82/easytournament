package easytournament.basic.gui.dialog;

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

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import easytournament.basic.model.dialog.SportDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Sport;

/**
 * Dialog to create and modify sports
 * @author David Meier
 *
 */
public class SportDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 7802697607687750066L;
  /**
   * The presentation model
   */
  protected SportDialogPModel pm;

  /**
   * Constructor that creates and shows the sport dialog
   * @param owner The owner of the dialog
   * @param modal True if the dialog is modal
   * @param pm The presentation model
   */
  public SportDialog(Frame owner, boolean modal, SportDialogPModel pm) {
    super(owner, ResourceManager.getText(Text.SPORT), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    this.init();
    this.pack();
    this.setLocationRelativeTo(owner);
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        SportDialog.this.pm.removePropertyChangeListener(SportDialog.this);
        super.windowClosed(e);
      }      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializes the dialog
   */
  private void init() {
    setLayout(new BorderLayout());
    add(getCenterPanel(), BorderLayout.CENTER);
    add(getButtonPanel(), BorderLayout.SOUTH);
  }
  
  /**
   * Creates the button dialog
   * @return The button dialog
   */
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(SportDialogPModel.OK_ACTION)); 
    JButton cancel = new JButton(this.pm.getAction(SportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  /**
   * Creates the main panel of the dialog
   * @return The center panel
   */
  private Component getCenterPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(DEFAULT;90PX):NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout1);

    JLabel nameLabel = new JLabel(ResourceManager.getText(Text.NAME));
    panel.add(nameLabel, cc.xy(1, 2));

    JTextField nameTf = BasicComponentFactory
        .createTextField(new PropertyAdapter<SportDialogPModel>(this.pm,
            SportDialogPModel.PROPERTY_NAME));
    nameTf.setEditable(this.pm.isNameEditable());
    panel.add(nameTf, cc.xy(3, 2));

    JLabel fromLabel = new JLabel(ResourceManager.getText(Text.IMPORTFROM));
    panel.add(fromLabel, cc.xy(3, 4));

    JLabel settingsLabel = new JLabel(ResourceManager.getText(Text.GAME_DUR_POINTS));
    panel.add(settingsLabel, cc.xy(1, 6));

    @SuppressWarnings("unchecked")
    JComboBox<Sport> settCB = new JComboBox<Sport>(new ComboBoxAdapter<Sport>(
        this.pm.getList(), new PropertyAdapter<SportDialogPModel>(this.pm,
            SportDialogPModel.PROPERTY_SETTINGS_IMPORT)));
    panel.add(settCB, cc.xy(3, 6));

    JLabel rulesLabels = new JLabel(ResourceManager.getText(Text.RULES));
    panel.add(rulesLabels, cc.xy(1, 8));

    @SuppressWarnings("unchecked")
    JComboBox<Sport> rulesCB = new JComboBox<Sport>(new ComboBoxAdapter<Sport>(
        this.pm.getList(), new PropertyAdapter<SportDialogPModel>(this.pm,
            SportDialogPModel.PROPERTY_RULES_IMPORT)));
    panel.add(rulesCB, cc.xy(3, 8));

    JLabel eventsLabel = new JLabel(ResourceManager.getText(Text.EVENTS));
    panel.add(eventsLabel, cc.xy(1, 10));

    @SuppressWarnings("unchecked")
    JComboBox<Sport> eventsCB = new JComboBox<Sport>(
        new ComboBoxAdapter<Sport>(this.pm.getList(),
            new PropertyAdapter<SportDialogPModel>(this.pm,
                SportDialogPModel.PROPERTY_EVENTS_IMPORT)));
    panel.add(eventsCB, cc.xy(3, 10));

    addFillComponents(panel, new int[] {1, 2, 3}, new int[] {1, 3, 5, 7, 9});
    return panel;
  }

  /**
   * Adds fill components to empty cells in the first row and first column of
   * the grid. This ensures that the grid spacing will be the same as shown in
   * the designer.
   * @param panel
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

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(SportDialogPModel.DISPOSE)){
      SportDialog.this.pm.removePropertyChangeListener(this);
      this.dispose();
    }    
  }
}
