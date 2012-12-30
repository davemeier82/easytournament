package com.easytournament.designer.gui.settings;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;


import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.gui.ColorPickerButton;
import com.easytournament.designer.model.settings.DesignerSetPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class DesignerSettingsPanel extends JPanel {


  protected DesignerSetPModel dspm;
  protected PresentationModel<Model> pm;

  /**
   * Default constructor
   */
  public DesignerSettingsPanel(DesignerSetPModel dspm) {
    this.dspm = dspm;
    this.pm = new PresentationModel<Model>(dspm);
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
     FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:30PX:NONE,FILL:DEFAULT:NONE,FILL:160PX:NONE,FILL:DEFAULT:NONE,FILL:80PX:NONE",
         "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
     CellConstraints cc = new CellConstraints();
     jpanel1.setLayout(formlayout1);

     JLabel jlabel1 = new JLabel(ResourceManager.getText(Text.GROUP_SETTINGS));
     jlabel1.setFont(jlabel1.getFont().deriveFont(Font.BOLD, jlabel1.getFont().getSize()));
     jpanel1.add(jlabel1,cc.xy(1,1));

     JLabel jlabel2 = new JLabel();
     jlabel2.setText(ResourceManager.getText(Text.FONT));
     jpanel1.add(jlabel2,cc.xy(1,3));

     JComboBox<?> sizeComboGroup = new JComboBox(new ComboBoxAdapter(
         dspm.getFontSizes(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_GROUP_FONTSIZE)));
     sizeComboGroup.setEditable(true);
     jpanel1.add(sizeComboGroup,cc.xy(7,3));

     JLabel jlabel3 = new JLabel();
     jlabel3.setText(ResourceManager.getText(Text.FONTCOLOR_MENU));
     jpanel1.add(jlabel3,cc.xy(1,5));

     JLabel jlabel4 = new JLabel();
     jlabel4.setText(ResourceManager.getText(Text.HORIZ_TEXT_ALIGN));
     jpanel1.add(jlabel4,cc.xy(1,7));

     JLabel jlabel5 = new JLabel();
     jlabel5.setText(ResourceManager.getText(Text.VERT_TEXT_ALIGN));
     jpanel1.add(jlabel5,cc.xy(1,9));

     JLabel jlabel6 = new JLabel();
     jlabel6.setText(ResourceManager.getText(Text.COLORGRADIENT_MENU));
     jpanel1.add(jlabel6,cc.xy(1,13));

     JLabel jlabel7 = new JLabel();
     jlabel7.setText(ResourceManager.getText(Text.BACKGROUND_COLOR));
     jpanel1.add(jlabel7,cc.xy(1,11));

     JLabel jlabel8 = new JLabel();
     jlabel8.setText(ResourceManager.getText(Text.BORDER_WITH));
     jpanel1.add(jlabel8,cc.xy(1,17));

     JLabel jlabel9 = new JLabel();
     jlabel9.setText(ResourceManager.getText(Text.BORDER_COLOR));
     jpanel1.add(jlabel9,cc.xy(1,15));

     JComboBox<?> fontComboGroup = new JComboBox(new ComboBoxAdapter(dspm.getFonts(),
         new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_GROUP_FONTTYPE)));
     fontComboGroup.setEditable(true);
     jpanel1.add(fontComboGroup,cc.xywh(3,3,3,1));

     JComboBox<?> hAlignComboGroup = new JComboBox(new ComboBoxAdapter(
         dspm.getHAlignements(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_GROUP_HALIGN)));
     jpanel1.add(hAlignComboGroup,cc.xywh(3,7,3,1));

     JComboBox<?> vAlignComboGroup = new JComboBox(new ComboBoxAdapter(
         dspm.getVAlignements(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_GROUP_VALIGN)));
     jpanel1.add(vAlignComboGroup,cc.xywh(3,9,3,1));

     JSpinner borderWidthGroup = new JSpinner(SpinnerAdapterFactory
         .createNumberAdapter(pm.getModel(DesignerSetPModel.PROPERTY_GROUP_BORDERWIDTH),3,1,255,1));
     jpanel1.add(borderWidthGroup,cc.xywh(3,17,3,1));

     final JButton bgColorGroup = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.GROUP_FILL_COLOR_ACTION));
     bgColorGroup.setPreferredSize(new Dimension(30, 30));
     bgColorGroup.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               bgColorGroup.setBackground((Color)evt.getNewValue());
             }
           }
         });
     bgColorGroup.setBackground((Color)bgColorGroup.getAction().getValue("FG"));
     jpanel1.add(bgColorGroup,cc.xy(3,11));

     final JButton fontcolorGroup = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.GROUP_FONT_COLOR_ACTION));
     fontcolorGroup.setPreferredSize(new Dimension(30, 30));
     fontcolorGroup.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               fontcolorGroup.setBackground((Color)evt.getNewValue());
             }
           }
         });
     fontcolorGroup.setBackground((Color)fontcolorGroup.getAction().getValue("FG"));
     jpanel1.add(fontcolorGroup,cc.xy(3,5));

     final JButton gradientcolorGroup = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.GROUP_GRADIENT_COLOR_ACTION));
     gradientcolorGroup.setPreferredSize(new Dimension(30, 30));
     gradientcolorGroup.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               gradientcolorGroup.setBackground((Color)evt.getNewValue());
             }
           }
         });
     gradientcolorGroup.setBackground((Color)gradientcolorGroup.getAction().getValue("FG"));
     jpanel1.add(gradientcolorGroup,cc.xy(3,13));

     final JButton bordercolorGroup = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.GROUP_BORDER_COLOR_ACTION));
     bordercolorGroup.setPreferredSize(new Dimension(30, 30));
     bordercolorGroup.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               bordercolorGroup.setBackground((Color)evt.getNewValue());
             }
           }
         });
     bordercolorGroup.setBackground((Color)bordercolorGroup.getAction().getValue("FG"));
     jpanel1.add(bordercolorGroup,cc.xy(3,15));

     JLabel jlabel10 = new JLabel(ResourceManager.getText(Text.POSITION_SETTING));
     jlabel10.setFont(jlabel10.getFont().deriveFont(Font.BOLD, jlabel10.getFont().getSize()));
     jpanel1.add(jlabel10,cc.xy(1,19));

     JLabel jlabel11 = new JLabel(ResourceManager.getText(Text.FONT));
     jpanel1.add(jlabel11,cc.xy(1,21));

     JLabel jlabel12 = new JLabel();
     jlabel12.setText(ResourceManager.getText(Text.FONTCOLOR_MENU));
     jpanel1.add(jlabel12,cc.xy(1,23));

     JLabel jlabel13 = new JLabel();
     jlabel13.setText(ResourceManager.getText(Text.HORIZ_TEXT_ALIGN));
     jpanel1.add(jlabel13,cc.xy(1,25));

     JLabel jlabel14 = new JLabel();
     jlabel14.setText(ResourceManager.getText(Text.VERT_TEXT_ALIGN));
     jpanel1.add(jlabel14,cc.xy(1,27));

     JLabel jlabel15 = new JLabel();
     jlabel15.setText(ResourceManager.getText(Text.BACKGROUND_COLOR));
     jpanel1.add(jlabel15,cc.xy(1,29));

     JLabel jlabel16 = new JLabel();
     jlabel16.setText(ResourceManager.getText(Text.COLORGRADIENT_MENU));
     jpanel1.add(jlabel16,cc.xy(1,31));

     JLabel jlabel17 = new JLabel();
     jlabel17.setText(ResourceManager.getText(Text.BORDER_COLOR));
     jpanel1.add(jlabel17,cc.xy(1,33));

     JLabel jlabel18 = new JLabel();
     jlabel18.setText(ResourceManager.getText(Text.BORDER_WITH));
     jpanel1.add(jlabel18,cc.xy(1,35));

     JComboBox<?> fontComboPos = new JComboBox(new ComboBoxAdapter(dspm.getFonts(),
         new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_POS_FONTTYPE)));
     fontComboPos.setEditable(true);
     jpanel1.add(fontComboPos,cc.xywh(3,21,3,1));

     JComboBox<?> sizeComboPos = new JComboBox(new ComboBoxAdapter(
         dspm.getFontSizes(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_POS_FONTSIZE)));
     sizeComboPos.setEditable(true);
     jpanel1.add(sizeComboPos,cc.xy(7,21));

     JComboBox<?> hAlignComboPos = new JComboBox(new ComboBoxAdapter(
         dspm.getHAlignements(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_POS_HALIGN)));
     jpanel1.add(hAlignComboPos,cc.xywh(3,25,3,1));

     JComboBox<?> vAlignComboPos = new JComboBox(new ComboBoxAdapter(
         dspm.getVAlignements(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_POS_VALIGN)));
     jpanel1.add(vAlignComboPos,cc.xywh(3,27,3,1));

     final JButton fontColorPos = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.POS_FONT_COLOR_ACTION));
     fontColorPos.setPreferredSize(new Dimension(30, 30));
     fontColorPos.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               fontColorPos.setBackground((Color)evt.getNewValue());
             }
           }
         });
     fontColorPos.setBackground((Color)fontColorPos.getAction().getValue("FG"));
     jpanel1.add(fontColorPos,cc.xy(3,23));

     final JButton bgColorPos = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.POS_FILL_COLOR_ACTION));
     bgColorPos.setPreferredSize(new Dimension(30, 30));
     bgColorPos.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               bgColorPos.setBackground((Color)evt.getNewValue());
             }
           }
         });
     bgColorPos.setBackground((Color)bgColorPos.getAction().getValue("FG"));
     jpanel1.add(bgColorPos,cc.xy(3,29));

     final JButton gradientcolorPos = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.POS_GRADIENT_COLOR_ACTION));
     gradientcolorPos.setPreferredSize(new Dimension(30, 30));
     gradientcolorPos.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               gradientcolorPos.setBackground((Color)evt.getNewValue());
             }
           }
         });
     gradientcolorPos.setBackground((Color)gradientcolorPos.getAction().getValue("FG"));
     jpanel1.add(gradientcolorPos,cc.xy(3,31));

     final JButton bordercolorPos = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.POS_BORDER_COLOR_ACTION));
     bordercolorPos.setPreferredSize(new Dimension(30, 30));
     bordercolorPos.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               bordercolorPos.setBackground((Color)evt.getNewValue());
             }
           }
         });
     bordercolorPos.setBackground((Color)bordercolorPos.getAction().getValue("FG"));
     jpanel1.add(bordercolorPos,cc.xy(3,33));

     JSpinner borderWidthPos = new JSpinner(SpinnerAdapterFactory
         .createNumberAdapter(pm.getModel(DesignerSetPModel.PROPERTY_POS_BORDERWIDTH),3,1,255,1));
     jpanel1.add(borderWidthPos,cc.xywh(3,35,3,1));

     jpanel1.add(new JSeparator(),cc.xywh(3,19,5,1));
     

     jpanel1.add(new JSeparator(),cc.xywh(3,1,5,1));

     JLabel jlabel19 = new JLabel(ResourceManager.getText(Text.LINE_SETTING));
     jlabel19.setFont(jlabel19.getFont().deriveFont(Font.BOLD, jlabel19.getFont().getSize()));
     jpanel1.add(jlabel19,cc.xy(1,37));

     jpanel1.add(new JSeparator(),cc.xywh(3,37,5,1));

     JLabel jlabel20 = new JLabel();
     jlabel20.setText(ResourceManager.getText(Text.LINE_COLOR_MENU));
     jpanel1.add(jlabel20,cc.xy(1,39));

     final JButton linecolor = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.LINE_COLOR_ACTION));
     linecolor.setPreferredSize(new Dimension(30, 30));
     linecolor.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               linecolor.setBackground((Color)evt.getNewValue());
             }
           }
         });
     linecolor.setBackground((Color)linecolor.getAction().getValue("FG"));
     jpanel1.add(linecolor,cc.xy(3,39));

     JComboBox<?> fontComboLine = new JComboBox(new ComboBoxAdapter(dspm.getFonts(),
         new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_LINE_FONTTYPE)));
     fontComboLine.setEditable(true);
     jpanel1.add(fontComboLine,cc.xywh(3,47,3,1));

     JComboBox<?> sizeComboLine = new JComboBox(new ComboBoxAdapter(
         dspm.getFontSizes(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_LINE_FONTSIZE)));     
     sizeComboLine.setEditable(true);
     jpanel1.add(sizeComboLine,cc.xy(7,47));

     JLabel jlabel21 = new JLabel();
     jlabel21.setText(ResourceManager.getText(Text.LABEL_FONT));
     jpanel1.add(jlabel21,cc.xy(1,47));

     JLabel jlabel22 = new JLabel();
     jlabel22.setText(ResourceManager.getText(Text.LINEEND_SIZE));
     jpanel1.add(jlabel22,cc.xy(1,45));

     JSpinner lineendSize = new JSpinner(SpinnerAdapterFactory
         .createNumberAdapter(pm.getModel(DesignerSetPModel.PROPERTY_LINEEND_SIZE),3,1,255,1));
     jpanel1.add(lineendSize,cc.xywh(3,45,3,1));

     JComboBox<?> lineEndTypeCombo = new JComboBox(new ComboBoxAdapter(
         dspm.getLineEndTypes(), new PropertyAdapter<DesignerSetPModel>(dspm,
             DesignerSetPModel.PROPERTY_LINEEND_TYPE)));
     jpanel1.add(lineEndTypeCombo,cc.xywh(3,43,3,1));

     JLabel jlabel23 = new JLabel();
     jlabel23.setText(ResourceManager.getText(Text.LINEEND_MENU));
     jpanel1.add(jlabel23,cc.xy(1,43));

     JLabel jlabel25 = new JLabel();
     jlabel25.setText(ResourceManager.getText(Text.LINE_WIDTH_MENU));
     jpanel1.add(jlabel25,cc.xy(1,41));

     JSpinner lineWidth = new JSpinner(SpinnerAdapterFactory
         .createNumberAdapter(pm.getModel(DesignerSetPModel.PROPERTY_LINEWIDTH),3,1,255,1));
     jpanel1.add(lineWidth,cc.xywh(3,41,3,1));

     JLabel jlabel26 = new JLabel();
     jlabel26.setText(ResourceManager.getText(Text.LABEL_FONT_COLOR));
     jpanel1.add(jlabel26,cc.xy(1,49));

     final JButton fontcolorLine = new ColorPickerButton(
         dspm.getColorAction(DesignerSetPModel.LINE_FONT_COLOR_ACTION));
     fontcolorLine.setPreferredSize(new Dimension(30, 30));
     fontcolorLine.getAction().addPropertyChangeListener(
         new PropertyChangeListener() {

           @Override
           public void propertyChange(PropertyChangeEvent evt) {
             if (evt.getPropertyName() == "FG") {
               fontcolorLine.setBackground((Color)evt.getNewValue());
             }
           }
         });
     fontcolorLine.setBackground((Color)fontcolorLine.getAction().getValue("FG"));
     jpanel1.add(fontcolorLine,cc.xy(3,49));

     addFillComponents(jpanel1,new int[]{ 2,4,5,6,7 },new int[]{ 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50 });
     return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    this.setName(ResourceManager.getText(Text.DESIGNER));
    this.setMinimumSize(new Dimension(50, 50));
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.add(createPanel());
  }
}
