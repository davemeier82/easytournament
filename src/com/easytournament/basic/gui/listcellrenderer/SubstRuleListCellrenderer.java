package com.easytournament.basic.gui.listcellrenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Rule;


public class SubstRuleListCellrenderer extends SubstanceDefaultListCellRenderer {

  private Rule rule;
  private Icon ascendingIcon = ResourceManager
      .getIcon(com.easytournament.basic.resources.Icon.ASCENDING_ICON);
  private Icon descendingIcon = ResourceManager
      .getIcon(com.easytournament.basic.resources.Icon.DESCENDING_ICON);
  private String ascendingStr = ResourceManager.getText(Text.ASCENDING);
  private String descendingStr = ResourceManager.getText(Text.DESCENDING);

  @Override
  public Component getListCellRendererComponent(JList list, Object rule,
      int index, boolean isSelected, boolean hasFocus) {

    JLabel label = (JLabel)super.getListCellRendererComponent(list, rule,
        index, isSelected, hasFocus);

    this.rule = (Rule)rule;
    if (this.rule.isAscending()) {
      label.setIcon(ascendingIcon);
      label.setToolTipText(this.rule.getName() + " - " + this.ascendingStr);
    }
    else {
      label.setIcon(descendingIcon);
      label.setToolTipText(this.rule.getName() + " - " + this.descendingStr);
    }
    label.setText(this.rule.getName());
    return label;
  }
}
