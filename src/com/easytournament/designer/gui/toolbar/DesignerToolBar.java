package com.easytournament.designer.gui.toolbar;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.designer.model.DesignerToolBarModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;


public class DesignerToolBar extends JToolBar {
  
  private DesignerToolBarModel pm;
  
  public DesignerToolBar(DesignerToolBarModel pm){
    this.pm = pm;
    this.init();
  }

  private void init() {
    this.setFloatable(false);
    this.addSeparator();
    JButton zoomout = new JButton(pm.getAction(MainMenuAction.ZOOMOUT));
    zoomout.setFocusPainted(false);
    this.add(zoomout);
    JButton zoomin = new JButton(pm.getAction(MainMenuAction.ZOOMIN));
    zoomin.setFocusPainted(false);
    this.add(zoomin);
    this.addSeparator();
    
    JComboBox<?> fontCombo = new JComboBox(new ComboBoxAdapter(pm.getFonts(),
        new PropertyAdapter<DesignerToolBarModel>(pm, DesignerToolBarModel.PROPERTY_FONTTYPE, true)));
    fontCombo.setEditable(true);
    fontCombo.setMinimumSize(new Dimension(180, 0));
    fontCombo.setPreferredSize(new Dimension(180, 0));
    fontCombo.setMaximumSize(new Dimension(180, 100));
    this.add(fontCombo);
    JComboBox<?> sizeCombo = new JComboBox(new ComboBoxAdapter(pm.getFontSizes(),
        new PropertyAdapter<DesignerToolBarModel>(pm, DesignerToolBarModel.PROPERTY_FONTSIZE, true)));
    sizeCombo.setEditable(true);
    sizeCombo.setMinimumSize(new Dimension(80, 0));
    sizeCombo.setPreferredSize(new Dimension(80, 0));
    sizeCombo.setMaximumSize(new Dimension(80, 100));
    this.add(sizeCombo);
    this.addSeparator();
    JButton fontcolor = new JButton(pm.getAction(MainMenuAction.FONT_COLOR));
    fontcolor.setFocusPainted(false);
    this.add(fontcolor);
    JButton linecolor = new JButton(pm.getAction(MainMenuAction.LINE_COLOR));
    linecolor.setFocusPainted(false);
    this.add(linecolor);
    JButton shapefillcolor = new JButton(pm.getAction(MainMenuAction.CELL_FILLCOLOR));
    shapefillcolor.setFocusPainted(false);
    this.add(shapefillcolor);
    this.addSeparator();
    JButton shapealignleft = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_LEFT));
    shapealignleft.setFocusPainted(false);
    this.add(shapealignleft);
    JButton shapealigncenter = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_CENTER));
    shapealigncenter.setFocusPainted(false);
    this.add(shapealigncenter);
    JButton shapealignright = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_RIGHT));
    shapealignright.setFocusPainted(false);
    this.add(shapealignright);
    JButton shapealigntop = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_TOP));
    shapealigntop.setFocusPainted(false);
    this.add(shapealigntop);
    JButton shapealignmiddle = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_MIDDLE));
    shapealignmiddle.setFocusPainted(false);
    this.add(shapealignmiddle);
    JButton shapealignbottom = new JButton(pm.getAction(MainMenuAction.SHAPE_ALIGN_BOTTOM));
    shapealignbottom.setFocusPainted(false);
    this.add(shapealignbottom);
    this.addSeparator();
    JButton changeViewBtn = new JButton(pm.getAction(DesignerToolBarModel.CHANGE_VIEW_ACTION));
    changeViewBtn.setFocusPainted(false);
    this.add(changeViewBtn);
  }

}
