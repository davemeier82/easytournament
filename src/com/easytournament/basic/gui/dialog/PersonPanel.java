package com.easytournament.basic.gui.dialog;

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

import com.easytournament.basic.model.dialog.PersonPanelPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Person;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

public class PersonPanel extends JPanel {
  protected JComboBox<String> genderCB = new JComboBox<String>();
  protected JTextField firstNameTF;
  protected JTextField lastNameTF;
  protected JDateChooser bDateChooser;
  protected JTextField phoneTF;
  protected JTextField emailTF;
  protected JButton pictureButton;
  protected JTextArea addressTA;
  protected JTextArea notesTA;
  protected PersonPanelPModel pm;
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

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:50PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:80PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel genderLabel = new JLabel();
    genderLabel.setText(ResourceManager.getText(Text.GENDER));
    jpanel1.add(genderLabel, cc.xy(1, 2));

    JLabel firstNameLabel = new JLabel();
    firstNameLabel.setText(ResourceManager.getText(Text.FIRSTNAME));
    jpanel1.add(firstNameLabel, cc.xy(1, 4));

    JLabel lastNameLAbel = new JLabel();
    lastNameLAbel.setText(ResourceManager.getText(Text.LASTNAME));
    jpanel1.add(lastNameLAbel, cc.xy(1, 6));

    
    ComboBoxAdapter<ArrayListModel<String>> genderAdaper = new ComboBoxAdapter<ArrayListModel<String>>(pm.getGenderSelectionInList());
    genderCB = new JComboBox<String>(genderAdaper);
    jpanel1.add(genderCB, cc.xy(3, 2));

    firstNameTF = BasicComponentFactory.createTextField(pm.getPersonValueModel(Person.PROPERTY_PRENAME));
    jpanel1.add(firstNameTF, cc.xy(3, 4));

    lastNameTF = BasicComponentFactory.createTextField(pm.getPersonValueModel(Person.PROPERTY_NAME));
    jpanel1.add(lastNameTF, cc.xy(3, 6));

    JLabel bdateLabel = new JLabel();
    bdateLabel.setText(ResourceManager.getText(Text.BIRTHDATE));
    jpanel1.add(bdateLabel, cc.xy(1, 8));

    bDateChooser = new JDateChooser();
    Bindings.bind(bDateChooser, "date", pm.getPersonValueModel(Person.PROPERTY_BDATE));
    bDateChooser.setLocale(ResourceManager.getLocale());
    jpanel1.add(bDateChooser, cc.xy(3, 8));

    JLabel addressLabel = new JLabel();
    addressLabel.setText(ResourceManager.getText(Text.ADDRESS));
    jpanel1.add(addressLabel, cc.xy(1, 12));

    JLabel phoneLabel = new JLabel();
    phoneLabel.setText(ResourceManager.getText(Text.PHONE));
    jpanel1.add(phoneLabel, cc.xy(1, 19));

    JLabel emailLabel = new JLabel();
    emailLabel.setText(ResourceManager.getText(Text.EMAIL));
    jpanel1.add(emailLabel, cc.xy(1, 21));

    phoneTF = BasicComponentFactory.createTextField(pm.getPersonValueModel(Person.PROPERTY_PHONE));
    jpanel1.add(phoneTF, cc.xy(3, 19));

    emailTF = BasicComponentFactory.createTextField(pm.getPersonValueModel(Person.PROPERTY_EMAIL));
    jpanel1.add(emailTF, cc.xy(3, 21));

    pictureButton = new JButton(pm.getAction(PersonPanelPModel.PICTURE_ACTION));
    pictureButton.setFocusable(false);
    jpanel1.add(pictureButton, cc.xywh(5, 2, 1, 8));

    JLabel notesLabel = new JLabel();
    notesLabel.setText(ResourceManager.getText(Text.NOTES));
    jpanel1.add(notesLabel, cc.xy(5, 12));

    JScrollPane jscrollpane1 = new JScrollPane();
    addressTA = BasicComponentFactory.createTextArea(pm.getPersonValueModel(Person.PROPERTY_ADDRESS));
    jscrollpane1.setViewportView(addressTA);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(1, 13, 3, 5));

    JScrollPane jscrollpane2 = new JScrollPane();
    notesTA = BasicComponentFactory.createTextArea(pm.getPersonValueModel(Person.PROPERTY_NOTES));
    jscrollpane2.setViewportView(notesTA);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane2, cc.xywh(5, 13, 1, 9));
    
    popup = new JPopupMenu();
    JMenuItem mi = new JMenuItem(pm.getAction(PersonPanelPModel.RESET_ICON_ACTION));
    popup.add(mi);
    mi.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        pictureButton.setIcon(null);       
      }
    });
    pictureButton.addMouseListener(new PopupListener());

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5,
        7, 9, 10, 11, 14, 15, 16, 17, 18, 20});
    return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
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
