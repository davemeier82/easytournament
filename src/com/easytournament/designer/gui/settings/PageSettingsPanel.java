package com.easytournament.designer.gui.settings;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.gui.ColorPickerButton;
import com.easytournament.designer.model.settings.PageSetPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;



public class PageSettingsPanel extends JPanel {

  protected PageSetPModel pspm;
  protected PresentationModel<Model> pm;
  /**
   * Default constructor
   */
  public PageSettingsPanel(PageSetPModel pspm) {
    this.pspm = pspm;
    this.pm = new PresentationModel<Model>(pspm);
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
     FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:30PX:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
         "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
     CellConstraints cc = new CellConstraints();
     jpanel1.setLayout(formlayout1);

     JRadioButton portrait = BasicComponentFactory.createRadioButton(
         pm.getModel(PageSetPModel.PROPERTY_PORTRAIT), true,
         ResourceManager.getText(Text.PORTRAIT));
     jpanel1.add(portrait,cc.xy(5,1));

     JRadioButton landscape = BasicComponentFactory.createRadioButton(
         pm.getModel(PageSetPModel.PROPERTY_PORTRAIT), false,
         ResourceManager.getText(Text.LANDSCAPE));
     jpanel1.add(landscape,cc.xy(7,1));

     JLabel jlabel1 = new JLabel();
     jlabel1.setText(ResourceManager.getText(Text.PAGE_ORIENT));
     jpanel1.add(jlabel1,cc.xy(1,1));

     JLabel jlabel2 = new JLabel();
     jlabel2.setText(ResourceManager.getText(Text.DIAG_FILLCOLOR_MENU));
     jpanel1.add(jlabel2,cc.xy(1,3));

     JLabel jlabel3 = new JLabel();
     jlabel3.setText(ResourceManager.getText(Text.DIAG_PAGE_BACKCOLOR_MENU));
     jpanel1.add(jlabel3,cc.xy(1,5));

     final JButton pageColor = new ColorPickerButton(
         pspm.getColorAction(PageSetPModel.PAGE_COLOR_ACTION));
     pageColor.setPreferredSize(new Dimension(30, 30));
     pageColor.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               pageColor.setBackground((Color)evt.getNewValue());
             }
           }
         });
     pageColor.setBackground((Color)pageColor.getAction().getValue("FG"));
     jpanel1.add(pageColor,cc.xy(3,3));

     final JButton bgColor = new ColorPickerButton(
         pspm.getColorAction(PageSetPModel.BACKGROUND_COLOR_ACTION));
     bgColor.setPreferredSize(new Dimension(30, 30));
     bgColor.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               bgColor.setBackground((Color)evt.getNewValue());
             }
           }
         });
     bgColor.setBackground((Color)bgColor.getAction().getValue("FG"));
     jpanel1.add(bgColor,cc.xy(3,5));

     addFillComponents(jpanel1,new int[]{ 2,4,6 },new int[]{ 2,4 });
     return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    this.setName(ResourceManager.getText(Text.PAGE));
    this.setMinimumSize(new Dimension(50,50));
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.add(createPanel());
  }
}
