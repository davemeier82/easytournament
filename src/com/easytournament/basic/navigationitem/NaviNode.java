package com.easytournament.basic.navigationitem;

import javax.swing.tree.DefaultMutableTreeNode;

public class NaviNode extends DefaultMutableTreeNode {

  private static final long serialVersionUID = 1L;
  NavigationItem item;

  public NaviNode(String text, NavigationItem item) {
    super(text);
    this.item = item;
  }

  public NavigationItem getItem() {
    return item;
  }

}
