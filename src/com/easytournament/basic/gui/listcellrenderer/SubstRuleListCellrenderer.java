/* RuleListCellrenderer.java - Substance cell renderer for the rule list
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.listcellrenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Rule;


/**
 * Substance cell renderer for the rule list
 * @author David Meier
 *
 */
public class SubstRuleListCellrenderer extends SubstanceDefaultListCellRenderer {

  private static final long serialVersionUID = 2061408358568974551L;
  
  /**
   * The icon show when the rule is sorted ascending
   */
  private Icon ascendingIcon = ResourceManager
      .getIcon(com.easytournament.basic.resources.Icon.ASCENDING_ICON);
  /**
   * The icon show when the rule is sorted descending
   */
  private Icon descendingIcon = ResourceManager
      .getIcon(com.easytournament.basic.resources.Icon.DESCENDING_ICON);
  /**
   * String containing the translated word "ascending"
   */
  private String ascendingStr = ResourceManager.getText(Text.ASCENDING);
  /**
   * String containing the translated word "descending"
   */
  private String descendingStr = ResourceManager.getText(Text.DESCENDING);

  /* (non-Javadoc)
   * @see org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean hasFocus) {

    JLabel label = (JLabel)super.getListCellRendererComponent(list, value,
        index, isSelected, hasFocus);

    Rule rule = (Rule)value;
    if (rule.isAscending()) {
      label.setIcon(this.ascendingIcon);
      label.setToolTipText(rule.getName() + " - " + this.ascendingStr);
    }
    else {
      label.setIcon(this.descendingIcon);
      label.setToolTipText(rule.getName() + " - " + this.descendingStr);
    }
    label.setText(rule.getName());
    return label;
  }
}
