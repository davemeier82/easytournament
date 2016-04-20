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

/**
 * Dialog to create and modify teams.
 * @author David Meier
 *
 */
public class TeamDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = -5455770462414834316L;
  /**
   * Text field for the phone number
   */
  protected JTextField phoneTextField;
  /**
   * Text field for the email address
   */
  protected JTextField emailTextField;
  /**
   * Text field for the website URL
   */
  protected JTextField websiteTextField;
  /**
   * Button to show and change the team logo
   */
  protected JButton logoButton;
  /**
   * Text area for the address
   */
  protected JTextArea addressTextArea;
  /**
   * Text area for additional notes
   */
  protected JTextArea notesTextArea;
  /**
   * Text field for the team name
   */
  protected JTextField nameTextField;
  /**
   * Text field for the first name of the contact person
   */
  protected JTextField contactFirstNameTextField;
  /**
   * Text field for the last name of the contact person
   */
  protected JTextField contactLastNameTextField;
  /**
   * The context menu on the logo button
   */
  protected JPopupMenu popup;

  /**
   * The presentation model
   */
  protected TeamDialogPModel pm;

  /**
   * @param owner The owner of this dialog
   * @param pm The presentation model
   * @param modal True if the dialog is modal
   */
  public TeamDialog(Frame owner, TeamDialogPModel pm, boolean modal) {
    super(owner, ResourceManager.getText(Text.TEAM), modal);
    this.pm = pm;
    init();
    pm.addPropertyChangeListener(this);    
    this.addWindowListener(new WindowAdapter() {

      /* (non-Javadoc)
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        TeamDialog.this.pm.removePropertyChangeListener(TeamDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setLocationRelativeTo(owner);
    this.setVisible(true);
  }

  /**
   * Initializes the dialog
   */
  private void init() {
    Container cpane = this.getContentPane();
    cpane.setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();
    // the first tab contains general team information
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO), getGeneralInfoPanel());
    // the second tab allows to add and edit players of the team
    tabbedPane.addTab(ResourceManager.getText(Text.PLAYERS),
        new PlayersTabPanel(this.pm.getPlayersTabPanelPModel(), this));
    // the third tab allows to add and edit the staff of the team
    tabbedPane.addTab(ResourceManager.getText(Text.STAFF), new StaffTabPanel(this.pm.getStaffTabPanelPModel(), this));
    cpane.add(tabbedPane, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
  }

  /**
   * Creates the general information panel
   * @return The general information panel
   */
  public JPanel getGeneralInfoPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:40PX:NONE,CENTER:10px:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:80PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    panel.setLayout(formlayout1);
    
    this.logoButton = new JButton(this.pm.getAction(TeamDialogPModel.LOGO_ACTION));
    this.logoButton.setFocusable(false);
    panel.add(this.logoButton, cc.xywh(5, 2, 1, 9));

    JLabel nameLabel = new JLabel();
    nameLabel.setText(ResourceManager.getText(Text.NAME));
    panel.add(nameLabel, cc.xy(1, 2));
    
    this.nameTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_NAME));
    panel.add(this.nameTextField, cc.xy(3, 2));

    JLabel websiteLabel = new JLabel();
    websiteLabel.setText(ResourceManager.getText(Text.WEBSITE));
    panel.add(websiteLabel, cc.xy(1, 4));

    this.websiteTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_WEBSITE));
    panel.add(this.websiteTextField, cc.xy(3, 4));    

    JLabel contactLabel = new JLabel(ResourceManager.getText(Text.CONTACT_PERSON));
    panel.add(contactLabel, cc.xywh(1, 6, 3, 1));

    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.FIRSTNAME));
    panel.add(firstNameLabel, cc.xy(1, 8));
    
    this.contactFirstNameTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_CONTACT_PRENAME));
    panel.add(this.contactFirstNameTextField, cc.xy(3, 8));

    JLabel lastNameLabel = new JLabel();
    lastNameLabel.setText(ResourceManager.getText(Text.LASTNAME));
    panel.add(lastNameLabel, cc.xy(1, 10));
    
    this.contactLastNameTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_CONTACT_NAME));
    panel.add(this.contactLastNameTextField, cc.xy(3, 10));

    JLabel addressLabel = new JLabel();
    addressLabel.setText(ResourceManager.getText(Text.ADDRESS));
    panel.add(addressLabel, cc.xy(1, 12));
    
    JLabel notesLabel = new JLabel();
    notesLabel.setText(ResourceManager.getText(Text.NOTES));
    panel.add(notesLabel, cc.xy(5, 12));

    JScrollPane addressScrollPane = new JScrollPane();
    this.addressTextArea = BasicComponentFactory.createTextArea(this.pm
        .getTeamValueModel(Team.PROPERTY_ADDRESS));
    addressScrollPane.setViewportView(this.addressTextArea);
    addressScrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    addressScrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panel.add(addressScrollPane, cc.xywh(1, 13, 3, 5));
    
    JScrollPane notesScrollPane = new JScrollPane();
    this.notesTextArea = BasicComponentFactory.createTextArea(this.pm
        .getTeamValueModel(Team.PROPERTY_NOTES));
    notesScrollPane.setViewportView(this.notesTextArea);
    notesScrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    notesScrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panel.add(notesScrollPane, cc.xywh(5, 13, 1, 9));
    
    JLabel phoneLabel = new JLabel();
    phoneLabel.setText(ResourceManager.getText(Text.PHONE));
    panel.add(phoneLabel, cc.xy(1, 19));
    
    this.phoneTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_PHONE));
    panel.add(this.phoneTextField, cc.xy(3, 19));

    JLabel emailLabel = new JLabel();
    emailLabel.setText(ResourceManager.getText(Text.EMAIL));
    panel.add(emailLabel, cc.xy(1, 21));

    this.emailTextField = BasicComponentFactory.createTextField(this.pm
        .getTeamValueModel(Team.PROPERTY_EMAIL));
    panel.add(this.emailTextField, cc.xy(3, 21));

    this.popup = new JPopupMenu();
    JMenuItem menuItem = new JMenuItem(this.pm.getAction(TeamDialogPModel.RESET_ICON_ACTION));
    this.popup.add(menuItem);
    menuItem.addActionListener(new ActionListener() {
      
      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        TeamDialog.this.logoButton.setIcon(null);       
      }
    });
    
    this.logoButton.addMouseListener(new PopupListener());

    addFillComponents(panel, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 4, 5, 7,
        9, 11, 14, 15, 16, 17, 18, 20});
    return panel;
  }

  /**
   * Creates the button panel
   * @return The button panel
   */
  private Component getButtonPanel() {
    JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(this.pm.getAction(TeamDialogPModel.OK_ACTION));
    JButton cancelBtn = new JButton(
        this.pm.getAction(TeamDialogPModel.CANCEL_ACTION));
    bPanel.add(okBtn);
    bPanel.add(cancelBtn);
    return bPanel;
  }



  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(TeamDialogPModel.DISPOSE)) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }
  
  /**
   * Adds fill components to empty cells in the first row and first column of
   * the grid. This ensures that the grid spacing will be the same as shown in
   * the designer.
   * @param panel
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

  /**
   * Mouse listener for the context menu on the logo button
   * @author David Meier
   *
   */
  class PopupListener extends MouseAdapter {
    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    /**
     * Shows the context menu
     * @param e The mouse event
     */
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        TeamDialog.this.popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
