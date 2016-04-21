package easytournament.designer.gui.settings;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.gui.ColorPickerButton;
import easytournament.designer.model.settings.GridSetPModel;


public class GridSettingsPanel extends JPanel {
  
  protected GridSetPModel gspm;
  protected PresentationModel<Model> pm;

  /**
   * Default constructor
   */
  public GridSettingsPanel(GridSetPModel gspm) {
    this.gspm = gspm;
    this.pm = new PresentationModel<Model>(gspm);
    initializePanel();
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

  public JPanel createPanel()
  {
     JPanel jpanel1 = new JPanel();
     FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:MAX(DEFAULT;30PX):NONE,FILL:DEFAULT:NONE,FILL:150PX:NONE",
         "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
     CellConstraints cc = new CellConstraints();
     jpanel1.setLayout(formlayout1);

     JCheckBox jcheckbox1 = BasicComponentFactory.createCheckBox(pm.getModel(GridSetPModel.PROPERTY_SHOWGRID), ResourceManager.getText(Text.SHOWGRID));
     jpanel1.add(jcheckbox1,cc.xywh(1,1,5,1));

     JLabel jlabel1 = new JLabel();
     jlabel1.setText(ResourceManager.getText(Text.GRID_SIZE_MENU));
     jpanel1.add(jlabel1,cc.xy(1,3));

     JLabel jlabel2 = new JLabel();
     jlabel2.setText(ResourceManager.getText(Text.GRID_COLOR_MENU));
     jpanel1.add(jlabel2,cc.xy(1,5));

     JLabel jlabel3 = new JLabel();
     jlabel3.setText(ResourceManager.getText(Text.GRID_TYPE));
     jpanel1.add(jlabel3,cc.xy(1,7));

     JCheckBox jcheckbox2 = BasicComponentFactory.createCheckBox(pm.getModel(GridSetPModel.PROPERTY_SHOWRULER), ResourceManager.getText(Text.SHOWRULER));
     jpanel1.add(jcheckbox2,cc.xywh(1,9,5,1));

     JComboBox<String> type = new JComboBox<String>(new ComboBoxAdapter(gspm.getGridTypes(),
         new PropertyAdapter<GridSetPModel>(gspm,
             GridSetPModel.PROPERTY_GROUP_GRIDTYPE)));
     jpanel1.add(type,cc.xywh(3,7,3,1));

     JSpinner jspinner1 = new JSpinner(SpinnerAdapterFactory
         .createNumberAdapter(pm.getModel(GridSetPModel.PROPERTY_GRIDSIZE),3,1,1000,1));
     jpanel1.add(jspinner1,cc.xywh(3,3,3,1));

     final JButton gridColor = new ColorPickerButton(
         gspm.getColorAction(GridSetPModel.GRID_COLOR_ACTION));
     gridColor.setPreferredSize(new Dimension(30, 30));
     gridColor.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               gridColor.setBackground((Color)evt.getNewValue());
             }
           }
         });
     gridColor.setBackground((Color)gridColor.getAction().getValue("FG"));
     jpanel1.add(gridColor,cc.xy(3,5));

     addFillComponents(jpanel1,new int[]{ 2,3,4,5 },new int[]{ 2,4,6,8 });
     return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    this.setName(ResourceManager.getText(Text.GRID_MENU));
    this.setMinimumSize(new Dimension(50,50));
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.add(createPanel());
  }
}
