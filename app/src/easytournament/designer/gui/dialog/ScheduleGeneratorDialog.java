package easytournament.designer.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import easytournament.basic.gui.tablecellrenderer.CheckboxCellRenderer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.model.dialog.SGeneratorPModel;

public class ScheduleGeneratorDialog extends JDialog implements
    PropertyChangeListener, ChangeListener {

  protected SGeneratorPModel sgpm;
  protected PresentationModel<SGeneratorPModel> pm;
  protected JSpinner breakDaysSp, breakHoursSp, breakMinSp, numCGamesSp;
  JTable jtable1;
  JCheckBox jcheckbox1;

  /**
   * Default constructor
   */
  public ScheduleGeneratorDialog(Frame f, boolean modal, SGeneratorPModel pm) {
    super(f, ResourceManager.getText(Text.GENERATE_GAMES), modal);
    this.sgpm = pm;
    this.pm = new PresentationModel<SGeneratorPModel>(pm);
    this.initializePanel();
    this.pack();
    this.setLocationRelativeTo(f);
    pm.addPropertyChangeListener(this);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        ScheduleGeneratorDialog.this.pm
            .removePropertyChangeListener(ScheduleGeneratorDialog.this);
        super.windowClosed(e);
      }

    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
    add(getButtonPanel(), BorderLayout.SOUTH);
  }

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:MAX(50PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:MAX(50PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:MAX(50PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JScrollPane jscrollpane1 = new JScrollPane();
    jtable1 = new JTable(sgpm.getTableModel());
    TableColumnModel tcm = jtable1.getColumnModel();
    tcm.getColumn(0).setCellRenderer(new CheckboxCellRenderer());
    tcm.getColumn(0).setMaxWidth(30);

    jtable1.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        int row = jtable1.rowAtPoint(e.getPoint());
        sgpm.toggleSelected(row);
      }

    });
    jscrollpane1.setViewportView(jtable1);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(1, 2, 13, 1));

    jcheckbox1 = BasicComponentFactory.createCheckBox(
        pm.getModel(SGeneratorPModel.PROPERTY_MAXCG_SELECTED),
        ResourceManager.getText(Text.MAX_CONCURRENT_GAMES));
    jcheckbox1.addChangeListener(this);

    jpanel1.add(jcheckbox1, cc.xywh(1, 10, 5, 1));

    numCGamesSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(SGeneratorPModel.PROPERTY_MAX_CONCURRENT_GAMES), 1, 1,
        1000, 1));
    numCGamesSp.setEnabled(false);
    jpanel1.add(numCGamesSp, cc.xy(7, 10));

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NUMBEROF_ENCOUNTERS));
    jpanel1.add(jlabel1, cc.xy(1, 4));

    JLabel startDateL = new JLabel(ResourceManager.getText(Text.BEGINNING_DATE_TIME));
    jpanel1.add(startDateL, cc.xy(1, 6));

    JDateChooser beginDateChooser = new JDateChooser();
    Bindings.bind(beginDateChooser, "date", new PropertyAdapter<SGeneratorPModel>(sgpm, SGeneratorPModel.PROPERTY_STARTDATE, true));
    beginDateChooser.setLocale(ResourceManager.getLocale());
    jpanel1.add(beginDateChooser, cc.xy(3, 6));

    JFormattedTextField beginTimeFTF = BasicComponentFactory
        .createFormattedTextField(
            pm.getModel(SGeneratorPModel.PROPERTY_STARTTIME),
            DateFormat.getTimeInstance(DateFormat.SHORT,
                ResourceManager.getLocale()));
    jpanel1.add(beginTimeFTF, cc.xy(7, 6));

    JLabel breakLabel = new JLabel(ResourceManager.getText(Text.BREAK_BETWEEN_GAMES));
    jpanel1.add(breakLabel, cc.xy(1, 8));

    JSpinner numGamesSp = new JSpinner(
        SpinnerAdapterFactory.createNumberAdapter(
            pm.getModel(SGeneratorPModel.PROPERTY_NUMGAMES), 1, 1, 1000, 1));
    jpanel1.add(numGamesSp, cc.xy(3, 4));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.SELECT_GROUPS));
    jpanel1.add(jlabel2, cc.xy(1, 1));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.DAYS_COMMA));
    jpanel1.add(jlabel3, cc.xy(5, 8));

    breakDaysSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(SGeneratorPModel.PROPERTY_BREAK_DAYS), 0, 0, 1000, 1));
    jpanel1.add(breakDaysSp, cc.xy(3, 8));

    JLabel jlabel4 = new JLabel();
    jlabel4.setText(ResourceManager.getText(Text.HOURS_COMMA));
    jpanel1.add(jlabel4, cc.xy(9, 8));

    breakHoursSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(SGeneratorPModel.PROPERTY_BREAK_HOURS), 0, 0, 23, 1));
    jpanel1.add(breakHoursSp, cc.xy(7, 8));

    breakMinSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getModel(SGeneratorPModel.PROPERTY_BREAK_MIN), 1, 0, 59, 1));
    jpanel1.add(breakMinSp, cc.xy(11, 8));

    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.MINUTES));
    jpanel1.add(jlabel5, cc.xy(13, 8));
    
    JCheckBox concurrentGamesCB = BasicComponentFactory.createCheckBox(
        pm.getModel(SGeneratorPModel.PROPERTY_CONSECUTIVE_GAMES_ALLOWED),
        ResourceManager.getText(Text.ALLOW_CONSECUTIVE_GAMES));
    jcheckbox1.addChangeListener(this);
    jpanel1.add(concurrentGamesCB, cc.xyw(1, 12, 13));

    addFillComponents(jpanel1, new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
        13}, new int[] {3, 5, 7, 9, 11});
    return jpanel1;
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

  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.sgpm.getAction(SGeneratorPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.sgpm.getAction(SGeneratorPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == SGeneratorPModel.PROPERTY_DISPOSE) {
      sgpm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if(e.getSource().equals(jcheckbox1)){
      numCGamesSp.setEnabled(jcheckbox1.isSelected());
    } 
    
  }

}
