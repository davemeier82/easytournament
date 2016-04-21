package easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import easytournament.basic.gui.tablecellrenderer.CheckboxCellRenderer;
import easytournament.basic.model.dialog.GEventDialogPModel;
import easytournament.basic.model.dialog.ImportListDialogPModel;

/**
 * Dialog that shows a table of elements to import
 * @author David Meier
 * 
 */
public class ImportListDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 4775688030231693269L;
  /**
   * The presentation model
   */
  protected ImportListDialogPModel pm;

  /**
   * @param frame
   *          The parent frame
   * @param title
   *          The dialog title
   * @param modal
   *          True if the dialog is modal
   * @param pm
   *          The presentation model
   */
  public ImportListDialog(Frame frame, String title, boolean modal,
      ImportListDialogPModel pm) {
    super(frame, title, modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setSize(800, 600);
    this.setLocationRelativeTo(frame);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        // remove the property change listener when the window is closed
        ImportListDialog.this.pm
            .removePropertyChangeListener(ImportListDialog.this);
        super.windowClosed(e);
      }
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initialize the dialog panels
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
    JButton ok = new JButton(this.pm.getAction(GEventDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(GEventDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  /**
   * Creates the main panel
   * @return the main panel
   */
  public JPanel createMainPanel() {
    JPanel p = new JPanel(new BorderLayout());
    final JTable table = new JTable(this.pm.getTableModel());
    TableColumnModel tcm = table.getColumnModel();
    tcm.getColumn(0).setCellRenderer(new CheckboxCellRenderer());
    tcm.getColumn(0).setMaxWidth(30);

    table.addMouseListener(new MouseAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        getPresentationModel().toggleSelected(row);
      }
    });

    JScrollPane pane = new JScrollPane(table);
    p.add(pane);
    return p;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent
   * )
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

  /**
   * @return the presentation model
   */
  public ImportListDialogPModel getPresentationModel() {
    return this.pm;
  }

}
