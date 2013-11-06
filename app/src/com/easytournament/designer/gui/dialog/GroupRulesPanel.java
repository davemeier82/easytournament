package com.easytournament.designer.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.listcellrenderer.RuleListCellrenderer;
import com.easytournament.basic.gui.listcellrenderer.SubstRuleListCellrenderer;
import com.easytournament.basic.model.RulesPanelPModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;
import com.easytournament.basic.util.transferhandler.RuleTransferHandler;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.designer.model.dialog.GroupDialogPModel;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class GroupRulesPanel extends JPanel implements ChangeListener{

  protected GroupDialogPModel pm;
  
  protected JList<Rule> usedList, availList;
  protected JButton up, left, right, down;
  protected JCheckBox useDefaultChB;
  protected JComboBox<AbstractGroup> importCB;
  protected JButton importBtn;
  protected TablePopupMenu popup, popupAvail;
  protected ImageIcon descIcon = ResourceManager.getIcon(Icon.DESCENDING_ICON);
  protected ImageIcon ascIcon = ResourceManager.getIcon(Icon.ASCENDING_ICON);
  
  public GroupRulesPanel(GroupDialogPModel pm){
    this.pm = pm;
    init();
  }
  
  private void init(){
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());
    this.add(getTopPanel(), BorderLayout.NORTH);
    Box hBox = Box.createHorizontalBox();    
    hBox.add(getLeftBox());
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(getCenterBox());    
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(getRightBox());    
    setCompsEnabled();
    this.add(hBox, BorderLayout.WEST);
  }
  
  private Component getTopPanel() {
    JPanel p = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(90PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:240PX:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);
    
    useDefaultChB = BasicComponentFactory.createCheckBox(new PropertyAdapter<GroupDialogPModel>(pm,GroupDialogPModel.PROPERTY_DEFAULT_RULES), ResourceManager.getText(Text.USE_DEFAULT_RULES));
    useDefaultChB.addChangeListener(this);
    
    p.add(useDefaultChB, cc.xyw(1, 2, 3));
    
    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.IMPORT_FROM));
    p.add(firstNameLabel, cc.xy(1, 4));
    
    importCB  = new JComboBox<AbstractGroup>(new ComboBoxAdapter<ArrayListModel<AbstractGroup>>((SelectionInList<ArrayListModel<AbstractGroup>>) pm.getSelectionInList(pm.IMPORT_LIST)));
    p.add(importCB, cc.xy(3, 4));
    
    importBtn = new JButton(pm.getAction(GroupDialogPModel.IMPORT_RULES_ACTION));
    p.add(importBtn, cc.xy(5, 4));
    
    addFillComponents(p, new int[] {2, 4}, new int[] {1, 3, 5});
    
    return p;
  }

  private Box getLeftBox() {
    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.GROUP_RULES));
    title.setAlignmentX(CENTER_ALIGNMENT);
    vBox.add(title);
    JScrollPane jscrollpane1 = new JScrollPane();
    usedList = new JList<Rule>(pm.getListModel(RulesPanelPModel.USED_LIST));
    if(Organizer.getInstance().isSubstance())
      usedList.setCellRenderer(new SubstRuleListCellrenderer());
    else
      usedList.setCellRenderer(new RuleListCellrenderer());
    jscrollpane1.setPreferredSize(new Dimension(340,-1));
    usedList.setSelectionModel(pm
        .getListSelectionModel(RulesPanelPModel.USED_LIST));
    usedList.setDragEnabled(true);
    usedList.setDropMode(DropMode.INSERT);
    usedList.setTransferHandler(new RuleTransferHandler());
    usedList.addMouseListener(new MouseAdapter(){

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1 && usedList.isEnabled()){
          pm.changeOrdering(RulesPanelPModel.USED_LIST, usedList.locationToIndex(e.getPoint()));
          usedList.repaint();
        }
      }
      
    });
    jscrollpane1.setViewportView(usedList);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    vBox.add(jscrollpane1);
    
    popup = new TablePopupMenu();
    JMenuItem editItem = new JMenuItem(new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            int row = popup.getRow();
            if (row >= 0) {
              pm.changeOrdering(RulesPanelPModel.USED_LIST, row);
              usedList.repaint();
            }
          }
        });
    popup.add(editItem);
    JMenuItem removeItem = new JMenuItem(pm.getAction(RulesPanelPModel.ADDTO_AVAILABLE_ACTION));
    removeItem.getAction().putValue(Action.NAME, ResourceManager.getText(Text.REMOVE_RULE));
    removeItem.getAction().putValue(Action.SMALL_ICON, ResourceManager.getIcon(Icon.DELETE_ICON_SMALL));
    popup.add(removeItem);

    usedList.addMouseListener(new PopupListener());
    jscrollpane1.addMouseListener(new PopupListener());
    
    return vBox;
  }
  
  private Box getCenterBox() {
    Box vBox = Box.createVerticalBox();
    vBox.add(Box.createVerticalGlue());
    up = new JButton(pm.getAction(RulesPanelPModel.UP_ACTION));
    vBox.add(up);
    vBox.add(Box.createVerticalStrut(10));
    left = new JButton(pm.getAction(RulesPanelPModel.ADDTO_USED_ACTION));
    vBox.add(left);
    vBox.add(Box.createVerticalStrut(10));
    right = new JButton(pm.getAction(RulesPanelPModel.ADDTO_AVAILABLE_ACTION));
    vBox.add(right);
    vBox.add(Box.createVerticalStrut(10));
    down = new JButton(pm.getAction(RulesPanelPModel.DOWN_ACTION));
    vBox.add(down);
    vBox.add(Box.createVerticalGlue());
    return vBox;
  }
  
  private Box getRightBox() {
    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.AVAIL_RULES));
    title.setAlignmentX(CENTER_ALIGNMENT);
    vBox.add(title);
    JScrollPane jscrollpane2 = new JScrollPane();
    availList = new JList<Rule>(pm.getListModel(RulesPanelPModel.AVAILABLE_LIST));
    if(Organizer.getInstance().isSubstance())
      availList.setCellRenderer(new SubstRuleListCellrenderer());
    else
      availList.setCellRenderer(new RuleListCellrenderer());
    jscrollpane2.setPreferredSize(new Dimension(340,-1));
    availList.setSelectionModel(pm
        .getListSelectionModel(RulesPanelPModel.AVAILABLE_LIST));
    availList.setDragEnabled(true);
    availList.setDropMode(DropMode.INSERT);
    availList.setTransferHandler(new RuleTransferHandler());
    availList.addMouseListener(new MouseAdapter(){

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1 && availList.isEnabled()){
          pm.changeOrdering(RulesPanelPModel.AVAILABLE_LIST, availList.locationToIndex(e.getPoint()));
          availList.repaint();
        }
      }
      
    });
    jscrollpane2.setViewportView(availList);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    vBox.add(jscrollpane2);
    
    popupAvail = new TablePopupMenu();
    JMenuItem editItem = new JMenuItem(new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            int row = popupAvail.getRow();
            if (row >= 0) {
              pm.changeOrdering(RulesPanelPModel.AVAILABLE_LIST, row);
              availList.repaint();
            }
          }
        });
    popupAvail.add(editItem);
    JMenuItem addItem = new JMenuItem(pm.getAction(RulesPanelPModel.ADDTO_USED_ACTION));
    addItem.getAction().putValue(Action.NAME, ResourceManager.getText(Text.ADD_RULE));
    addItem.getAction().putValue(Action.SMALL_ICON, ResourceManager.getIcon(Icon.ADD_ICON_SMALL));
    popupAvail.add(addItem);

    availList.addMouseListener(new PopupListener());
    
    return vBox;
  }

  @Override
  public void stateChanged(ChangeEvent evt) {
    if(evt.getSource() == useDefaultChB){
      setCompsEnabled();
    }
    
  }

  private void setCompsEnabled() {
    boolean enabled = !this.useDefaultChB.isSelected();
    this.availList.setEnabled(enabled);
    this.usedList.setEnabled(enabled);
    this.up.setEnabled(enabled);
    this.left.setEnabled(enabled);
    this.right.setEnabled(enabled);
    this.down.setEnabled(enabled);
    this.importCB.setEnabled(enabled);
    this.importBtn.setEnabled(enabled);
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
  
  class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        int row;
        int list;
        TablePopupMenu pop;
        if(e.getComponent().equals(usedList)) {
          list = RulesPanelPModel.USED_LIST;
          row = usedList.locationToIndex(e.getPoint());
          pop = popup;
          if(!usedList.isSelectedIndex(row))
            usedList.setSelectedIndex(row);
        } else {
          pop = popupAvail;
          row = availList.locationToIndex(e.getPoint());
          list = RulesPanelPModel.AVAILABLE_LIST;
          if(!availList.isSelectedIndex(row))
            availList.setSelectedIndex(row);
        }
        
        pop.setRow(row);

        JMenuItem editItem = (JMenuItem) pop.getComponent(0);
        if (pm.isAscending(list, row)) {
          editItem.setText(ResourceManager.getText(Text.CHANGE_DESC));
          editItem.setIcon(descIcon);
        }
        else {
          editItem.setText(ResourceManager.getText(Text.CHANGE_ASC));
          editItem.setIcon(ascIcon);
        }

        pop.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
