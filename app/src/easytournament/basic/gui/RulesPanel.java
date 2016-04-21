package easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import easytournament.basic.Organizer;
import easytournament.basic.gui.listcellrenderer.RuleListCellrenderer;
import easytournament.basic.gui.listcellrenderer.SubstRuleListCellrenderer;
import easytournament.basic.model.RulesPanelPModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.popupmenu.TablePopupMenu;
import easytournament.basic.util.transferhandler.RuleTransferHandler;
import easytournament.basic.valueholder.Rule;


public class RulesPanel extends JPanel {

  protected RulesPanelPModel pm;
  protected TablePopupMenu popup, popupAvail;
  protected JList<Rule> usedList, availList;
  protected ImageIcon descIcon = ResourceManager.getIcon(Icon.DESCENDING_ICON);
  protected ImageIcon ascIcon = ResourceManager.getIcon(Icon.ASCENDING_ICON);
  
  public RulesPanel(RulesPanelPModel pm){
    this.pm = pm;
    init();
  }
  
  private void init(){
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());
    Box hBox = Box.createHorizontalBox();    
    hBox.add(getLeftBox());
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(getCenterBox());    
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(getRightBox());
    this.add(hBox, BorderLayout.WEST);
  }

  private Box getLeftBox() {
    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.TOURN_DEF_RULES));
    title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, title.getFont().getSize()));
    title.setAlignmentX(CENTER_ALIGNMENT);
    vBox.add(title);
    vBox.add(Box.createVerticalStrut(10));
    JScrollPane jscrollpane1 = new JScrollPane();
    usedList = new JList<Rule>(pm.getListModel(RulesPanelPModel.USED_LIST));
    if(Organizer.getInstance().isSubstance())
      usedList.setCellRenderer(new SubstRuleListCellrenderer());
    else
      usedList.setCellRenderer(new RuleListCellrenderer());
    jscrollpane1.setPreferredSize(new Dimension(400,-1));
    usedList.setSelectionModel(pm
        .getListSelectionModel(RulesPanelPModel.USED_LIST));
    usedList.setDragEnabled(true);
    usedList.setDropMode(DropMode.INSERT);
    usedList.setTransferHandler(new RuleTransferHandler());
    usedList.addMouseListener(new MouseAdapter(){

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1){
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
    JButton up = new JButton(pm.getAction(RulesPanelPModel.UP_ACTION));
    vBox.add(up);
    vBox.add(Box.createVerticalStrut(10));
    JButton left = new JButton(pm.getAction(RulesPanelPModel.ADDTO_USED_ACTION));
    vBox.add(left);
    vBox.add(Box.createVerticalStrut(10));
    JButton right = new JButton(pm.getAction(RulesPanelPModel.ADDTO_AVAILABLE_ACTION));
    vBox.add(right);
    vBox.add(Box.createVerticalStrut(10));
    JButton down = new JButton(pm.getAction(RulesPanelPModel.DOWN_ACTION));
    vBox.add(down);
    vBox.add(Box.createVerticalGlue());
    return vBox;
  }
  
  private Box getRightBox() {
    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.AVAIL_RULES));
    title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, title.getFont().getSize()));
    title.setAlignmentX(CENTER_ALIGNMENT);
    vBox.add(title);
    vBox.add(Box.createVerticalStrut(10));
    JScrollPane jscrollpane2 = new JScrollPane();
    availList = new JList<Rule>(pm.getListModel(RulesPanelPModel.AVAILABLE_LIST));
    if(Organizer.getInstance().isSubstance())
      availList.setCellRenderer(new SubstRuleListCellrenderer());
    else
      availList.setCellRenderer(new RuleListCellrenderer());
    jscrollpane2.setPreferredSize(new Dimension(400,-1));
    availList.setSelectionModel(pm
        .getListSelectionModel(RulesPanelPModel.AVAILABLE_LIST));
    availList.setDragEnabled(true);
    availList.setDropMode(DropMode.INSERT);
    availList.setTransferHandler(new RuleTransferHandler());
    availList.addMouseListener(new MouseAdapter(){

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1){
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