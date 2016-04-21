package easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import easytournament.basic.model.dialog.PersonPanelPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Person;

/**
 * This panel allows to add informations (Name, date of birth,...) to a person. 
 * @author David Meier
 *
 */
public class PersonPanel extends JPanel {

  private static final long serialVersionUID = -9098409780227271839L;
  /**
   * Checkbox to choose the gender
   */
  protected JComboBox<String> genderCB = new JComboBox<String>();
  /**
   * Text field for the first name
   */
  protected JTextField firstNameTF;
  /**
   * Text field for the last name
   */
  protected JTextField lastNameTF;
  /**
   * Date chooser for the date of birth
   */
  protected JDateChooser bDateChooser;
  /**
   * Text field for the phone number
   */
  protected JTextField phoneTF;
  /**
   * Text field for the email address
   */
  protected JTextField emailTF;
  /**
   * Button to add an image to a person
   */
  protected JButton pictureButton;
  /**
   * Text area for the address
   */
  protected JTextArea addressTA;
  /**
   * Text area to add some notes
   */
  protected JTextArea notesTA;
  /**
   * Presentation model
   */
  protected PersonPanelPModel pm;
  /**
   * Context menu on the picture button
   */
  protected JPopupMenu popup;

  /**
   * Default constructor
   */
  public PersonPanel(PersonPanelPModel pm) {
    this.pm = pm;
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

  /**
   * Creates the main panel
   * @return The main panel
   */
  @SuppressWarnings("unchecked")
  public JPanel createPanel() {
    JPanel mainpanel = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:50PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:80PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    mainpanel.setLayout(formlayout1);

    JLabel genderLabel = new JLabel();
    genderLabel.setText(ResourceManager.getText(Text.GENDER));
    mainpanel.add(genderLabel, cc.xy(1, 2));

    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.FIRSTNAME));
    mainpanel.add(firstNameLabel, cc.xy(1, 4));

    JLabel lastNameLAbel = new JLabel();
    lastNameLAbel.setText(ResourceManager.getText(Text.LASTNAME));
    mainpanel.add(lastNameLAbel, cc.xy(1, 6));
    
    ComboBoxAdapter<ArrayListModel<String>> genderAdaper = new ComboBoxAdapter<ArrayListModel<String>>(this.pm.getGenderSelectionInList());
    this.genderCB = new JComboBox<String>(genderAdaper);
    mainpanel.add(this.genderCB, cc.xy(3, 2));

    this.firstNameTF = BasicComponentFactory.createTextField(this.pm.getPersonValueModel(Person.PROPERTY_PRENAME));
    mainpanel.add(this.firstNameTF, cc.xy(3, 4));

    this.lastNameTF = BasicComponentFactory.createTextField(this.pm.getPersonValueModel(Person.PROPERTY_NAME));
    mainpanel.add(this.lastNameTF, cc.xy(3, 6));

    JLabel bdateLabel = new JLabel();
    bdateLabel.setText(ResourceManager.getText(Text.BIRTHDATE));
    mainpanel.add(bdateLabel, cc.xy(1, 8));

    this.bDateChooser = new JDateChooser();
    Bindings.bind(this.bDateChooser, "date", this.pm.getPersonValueModel(Person.PROPERTY_BDATE));
    this.bDateChooser.setLocale(ResourceManager.getLocale());
    mainpanel.add(this.bDateChooser, cc.xy(3, 8));

    JLabel addressLabel = new JLabel();
    addressLabel.setText(ResourceManager.getText(Text.ADDRESS));
    mainpanel.add(addressLabel, cc.xy(1, 12));

    JLabel phoneLabel = new JLabel();
    phoneLabel.setText(ResourceManager.getText(Text.PHONE));
    mainpanel.add(phoneLabel, cc.xy(1, 19));

    JLabel emailLabel = new JLabel();
    emailLabel.setText(ResourceManager.getText(Text.EMAIL));
    mainpanel.add(emailLabel, cc.xy(1, 21));

    this.phoneTF = BasicComponentFactory.createTextField(this.pm.getPersonValueModel(Person.PROPERTY_PHONE));
    mainpanel.add(this.phoneTF, cc.xy(3, 19));

    this.emailTF = BasicComponentFactory.createTextField(this.pm.getPersonValueModel(Person.PROPERTY_EMAIL));
    mainpanel.add(this.emailTF, cc.xy(3, 21));

    this.pictureButton = new JButton(this.pm.getAction(PersonPanelPModel.PICTURE_ACTION));
    this.pictureButton.setFocusable(false);
    mainpanel.add(this.pictureButton, cc.xywh(5, 2, 1, 8));

    JLabel notesLabel = new JLabel();
    notesLabel.setText(ResourceManager.getText(Text.NOTES));
    mainpanel.add(notesLabel, cc.xy(5, 12));

    JScrollPane jscrollpane1 = new JScrollPane();
    this.addressTA = BasicComponentFactory.createTextArea(this.pm.getPersonValueModel(Person.PROPERTY_ADDRESS));
    jscrollpane1.setViewportView(this.addressTA);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    mainpanel.add(jscrollpane1, cc.xywh(1, 13, 3, 5));

    JScrollPane jscrollpane2 = new JScrollPane();
    this.notesTA = BasicComponentFactory.createTextArea(this.pm.getPersonValueModel(Person.PROPERTY_NOTES));
    jscrollpane2.setViewportView(this.notesTA);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    mainpanel.add(jscrollpane2, cc.xywh(5, 13, 1, 9));
    
    // add context menu to the picture button
    this.popup = new JPopupMenu();
    JMenuItem mi = new JMenuItem(this.pm.getAction(PersonPanelPModel.RESET_ICON_ACTION));
    this.popup.add(mi);
    mi.addActionListener(new ActionListener() {
      
      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        PersonPanel.this.pictureButton.setIcon(null);       
      }
    });
    this.pictureButton.addMouseListener(new PopupListener());

    addFillComponents(mainpanel, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        7, 9, 10, 11, 14, 15, 16, 17, 18, 20});
    return mainpanel;
  }

  /**
   * Initializes the panel
   */
  protected void initializePanel() {
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
  }
  
  /**
   * Popup listener for the context menu on the picture button
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
     * Show the context menu if the right mouse button is pressed
     * @param e The mouse event
     */
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        PersonPanel.this.popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
