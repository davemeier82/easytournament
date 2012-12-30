package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


import com.easytournament.basic.model.dialog.TeamDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TeamDialog extends JDialog implements PropertyChangeListener {

  protected JTextField jtextfield1;
  protected JTextField jtextfield2;
  protected JTextField websiteTF;
  protected JButton jbutton1;
  protected JTextArea jtextarea1;
  protected JTextArea jtextarea2;
  protected JTextField jtextfield3;
  protected JTextField jtextfield4;
  protected JTextField jtextfield5;
  protected JPopupMenu popup;

  private TeamDialogPModel pm;

  public TeamDialog(Frame f, TeamDialogPModel pm, boolean modal) {
    super(f, ResourceManager.getText(Text.TEAM), modal);
    this.pm = pm;
    init();
    pm.addPropertyChangeListener(this);    
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        TeamDialog.this.pm.removePropertyChangeListener(TeamDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setLocationRelativeTo(f);
    this.setVisible(true);
  }

  private void init() {
    Container cpane = this.getContentPane();
    cpane.setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO), getGeneralInfoPanel());
    tabbedPane.addTab(ResourceManager.getText(Text.PLAYERS),
        new PlayersTabPanel(pm.getPlayersTabPanelPModel(), this));
    tabbedPane.addTab(ResourceManager.getText(Text.STAFF), new StaffTabPanel(pm.getStaffTabPanelPModel(), this));
    cpane.add(tabbedPane, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
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

  public JPanel getGeneralInfoPanel() {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:40PX:NONE,CENTER:10px:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:80PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NAME));
    p.add(jlabel1, cc.xy(1, 2));

    JLabel websiteLabel = new JLabel();
    websiteLabel.setText(ResourceManager.getText(Text.WEBSITE));
    p.add(websiteLabel, cc.xy(1, 4));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.ADDRESS));
    p.add(jlabel2, cc.xy(1, 12));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.PHONE));
    p.add(jlabel3, cc.xy(1, 19));

    JLabel jlabel4 = new JLabel();
    jlabel4.setText(ResourceManager.getText(Text.EMAIL));
    p.add(jlabel4, cc.xy(1, 21));

    jtextfield1 = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_PHONE));
    p.add(jtextfield1, cc.xy(3, 19));

    jtextfield2 = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_EMAIL));
    p.add(jtextfield2, cc.xy(3, 21));

    jbutton1 = new JButton(pm.getAction(TeamDialogPModel.LOGO_ACTION));
    jbutton1.setFocusable(false);
    p.add(jbutton1, cc.xywh(5, 2, 1, 9));

    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.NOTES));
    p.add(jlabel5, cc.xy(5, 12));

    JScrollPane jscrollpane1 = new JScrollPane();
    jtextarea1 = BasicComponentFactory.createTextArea(pm
        .getTeamValueModel(Team.PROPERTY_ADDRESS));
    jscrollpane1.setViewportView(jtextarea1);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    p.add(jscrollpane1, cc.xywh(1, 13, 3, 5));

    JScrollPane jscrollpane2 = new JScrollPane();
    jtextarea2 = BasicComponentFactory.createTextArea(pm
        .getTeamValueModel(Team.PROPERTY_NOTES));
    jscrollpane2.setViewportView(jtextarea2);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    p.add(jscrollpane2, cc.xywh(5, 13, 1, 9));

    jtextfield3 = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_NAME));
    p.add(jtextfield3, cc.xy(3, 2));

    websiteTF = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_WEBSITE));
    p.add(websiteTF, cc.xy(3, 4));

    JLabel contactLabel = new JLabel(ResourceManager.getText(Text.CONTACT_PERSON));
    p.add(contactLabel, cc.xywh(1, 6, 3, 1));

    JLabel jlabel6 = new JLabel();
    jlabel6.setText(ResourceManager.getText(Text.FIRSTNAME));
    p.add(jlabel6, cc.xy(1, 8));

    JLabel jlabel7 = new JLabel();
    jlabel7.setText(ResourceManager.getText(Text.LASTNAME));
    p.add(jlabel7, cc.xy(1, 10));

    jtextfield4 = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_CONTACT_PRENAME));
    p.add(jtextfield4, cc.xy(3, 8));

    jtextfield5 = BasicComponentFactory.createTextField(pm
        .getTeamValueModel(Team.PROPERTY_CONTACT_NAME));
    p.add(jtextfield5, cc.xy(3, 10));
    
    popup = new JPopupMenu();
    JMenuItem mi = new JMenuItem(pm.getAction(TeamDialogPModel.RESET_ICON_ACTION));
    popup.add(mi);
    mi.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        jbutton1.setIcon(null);       
      }
    });
    
    jbutton1.addMouseListener(new PopupListener());

    addFillComponents(p, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 4, 5, 7,
        9, 11, 14, 15, 16, 17, 18, 20});
    return p;
  }

  private Component getButtonPanel() {
    JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(pm.getAction(TeamDialogPModel.OK_ACTION));
    JButton cancelBtn = new JButton(
        pm.getAction(TeamDialogPModel.CANCEL_ACTION));
    bPanel.add(okBtn);
    bPanel.add(cancelBtn);
    return bPanel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
      this.dispose();
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
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

}
