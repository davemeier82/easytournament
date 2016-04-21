package easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.jgoodies.binding.adapter.BasicComponentFactory;

import easytournament.basic.model.dialog.ExportDialogPModel;
import easytournament.basic.model.dialog.GEventDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;


/**
 * Dialog to choose the item to export.
 * @author David Meier
 *
 */
public class ExportDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 2521568147694965233L;
  /**
   * The presentation model
   */
  protected ExportDialogPModel pm;

  /**
   * Constructor
   * @param frame The parent frame
   * @param modal True if the dialog should be modal
   * @param pm The presentation model
   */
  public ExportDialog(Frame frame, boolean modal, ExportDialogPModel pm) {
    super(frame, ResourceManager.getText(Text.SELECT_EXPORT_ITEM), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setMinimumSize(new Dimension(450,110));
    this.setLocationRelativeTo(frame);
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener if the window is closed
        ExportDialog.this.pm.removePropertyChangeListener(ExportDialog.this);
        super.windowClosed(e);
      }

    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializes the dialog panels
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createMainPanel(), BorderLayout.CENTER);
    add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(ExportDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(ExportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  /**
   * Creates the main panel
   * @return The main panel
   */
  public Component createMainPanel() {
    Box vBox = Box.createVerticalBox();
    @SuppressWarnings("unchecked")
    JComboBox<String> exportCombo = BasicComponentFactory.createComboBox(this.pm
        .getSelectionInList(ExportDialogPModel.EXPORT_LIST));
    vBox.add(exportCombo);
    return vBox;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }
}
