package com.easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.easytournament.basic.model.TPanelPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Sport;
import com.easytournament.basic.valueholder.Tournament;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

public class TournamentPanel extends JPanel {

  protected TPanelPModel pm = new TPanelPModel();
  protected JPopupMenu popup;
  
  /**
   * Default constructor
   */
  public TournamentPanel(TPanelPModel pm) {
    this.pm = pm;
    initializePanel();
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
  }

  public JPanel createPanel() {

    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(130PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:DEFAULT:NONE,RIGHT:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:30PX:NONE,FILL:80PX:NONE,FILL:10PX:NONE:NONE,FILL:75PX:NONE,FILL:10PX:NONE,FILL:75PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,FILL:140PX:NONE,CENTER:10PX:NONE,CENTER:30PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:DEFAULT:NONE,CENTER:DEFAULT:NONE,TOP:80PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:30PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.NAME));
    jpanel1.add(jlabel1, cc.xy(1, 2));

    JTextField nameField = BasicComponentFactory.createTextField(pm
        .getTournamentValueModel(Tournament.PROPERTY_NAME));
    jpanel1.add(nameField, cc.xywh(3, 2, 5, 1));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.DESCRIPTION));
    jpanel1.add(jlabel2, cc.xy(1, 4));

    JTextArea descField = BasicComponentFactory.createTextArea(pm
        .getTournamentValueModel(Tournament.PROPERTY_DESC));
    JScrollPane jscrollpane1 = new JScrollPane();
    jscrollpane1.setViewportView(descField);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(1, 5, 7, 1));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.BEGINNING));
    jpanel1.add(jlabel3, cc.xy(1, 7));

    JDateChooser beginDateChooser = new JDateChooser();    
    ValueModel beginModel = new PropertyAdapter<TPanelPModel>(pm, TPanelPModel.PROPERTY_BEGIN_DATE, true);
    Bindings.bind(beginDateChooser, "date", beginModel);
    beginDateChooser.setLocale(ResourceManager.getLocale());    
    jpanel1.add(beginDateChooser, cc.xy(3, 7));
    
    JLabel beginTimeL = new JLabel(ResourceManager.getText(Text.TIME));
    jpanel1.add(beginTimeL, cc.xy(5, 7));
    JFormattedTextField beginTimeFTF = BasicComponentFactory.createFormattedTextField(new PropertyAdapter<TPanelPModel>(pm, TPanelPModel.PROPERTY_BEGIN_TIME, true), DateFormat.getTimeInstance(
        DateFormat.SHORT, ResourceManager.getLocale()));
    jpanel1.add(beginTimeFTF, cc.xy(7, 7));

    JLabel jlabel4 = new JLabel();
    jlabel4.setText(ResourceManager.getText(Text.ENDING));
    jpanel1.add(jlabel4, cc.xy(1, 9));

    JDateChooser endDateChooser = new JDateChooser();    
    ValueModel endModel = new PropertyAdapter<TPanelPModel>(pm, TPanelPModel.PROPERTY_END_DATE, true);
    Bindings.bind(endDateChooser, "date", endModel);
    endDateChooser.setLocale(ResourceManager.getLocale());   
    jpanel1.add(endDateChooser, cc.xy(3, 9));
    
    JLabel endTimeL = new JLabel(ResourceManager.getText(Text.TIME));
    jpanel1.add(endTimeL, cc.xy(5, 9));
    JFormattedTextField endTimeFTF = BasicComponentFactory.createFormattedTextField(new PropertyAdapter<TPanelPModel>(pm, TPanelPModel.PROPERTY_END_TIME, true), DateFormat.getTimeInstance(
        DateFormat.SHORT, ResourceManager.getLocale()));
    jpanel1.add(endTimeFTF, cc.xy(7, 9));

    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.LOCATION));
    jpanel1.add(jlabel5, cc.xy(1, 11));
    
    JScrollPane jscrollpane4 = new JScrollPane();
    jscrollpane4.setPreferredSize(new Dimension(-1, 100));
    JTextArea locTA = BasicComponentFactory.createTextArea(pm.getTournamentValueModel(Tournament.PROPERTY_LOC));
    jscrollpane4.setViewportView(locTA);
    jscrollpane4
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane4
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane4, cc.xywh(1, 12, 7, 3));
    
    JLabel websiteLabel = new JLabel(ResourceManager.getText(Text.WEBSITE));
    jpanel1.add(websiteLabel, cc.xy(1, 16));
    
    JTextField websiteTF = BasicComponentFactory.createTextField(pm.getTournamentValueModel(Tournament.PROPERTY_WEBSITE));
    jpanel1.add(websiteTF, cc.xywh(3, 16, 5, 1));

    JLabel jlabel6 = new JLabel();
    jlabel6.setText(ResourceManager.getText(Text.CONTACT_PERSON));
    jpanel1.add(jlabel6, cc.xywh(1, 18, 7, 1));

    JLabel jlabel7 = new JLabel();
    jlabel7.setText(ResourceManager.getText(Text.FIRSTNAME));
    jpanel1.add(jlabel7, cc.xy(1, 20));

    JLabel jlabel8 = new JLabel();
    jlabel8.setText(ResourceManager.getText(Text.LASTNAME));
    jpanel1.add(jlabel8, cc.xy(1, 22));

    JTextField fnameTF = BasicComponentFactory.createTextField(pm.getTournamentValueModel(Tournament.PROPERTY_FNAME));
    jpanel1.add(fnameTF, cc.xywh(3, 20, 5, 1));

    JTextField lnameTF = BasicComponentFactory.createTextField(pm.getTournamentValueModel(Tournament.PROPERTY_LNAME));
    jpanel1.add(lnameTF, cc.xywh(3, 22, 5, 1));

    JLabel jlabel9 = new JLabel();
    jlabel9.setText(ResourceManager.getText(Text.ADDRESS));
    jpanel1.add(jlabel9, cc.xy(1, 24));

    JScrollPane jscrollpane2 = new JScrollPane();
    jscrollpane2.setPreferredSize(new Dimension(-1, 100));
    JTextArea addressTA = BasicComponentFactory.createTextArea(pm.getTournamentValueModel(Tournament.PROPERTY_ADDRESS));
    jscrollpane2.setViewportView(addressTA);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane2, cc.xywh(1, 25, 7, 1));

    JLabel jlabel10 = new JLabel();
    jlabel10.setText(ResourceManager.getText(Text.PHONE));
    jpanel1.add(jlabel10, cc.xy(9,20));

    JLabel jlabel11 = new JLabel();
    jlabel11.setText(ResourceManager.getText(Text.EMAIL));
    jpanel1.add(jlabel11, cc.xy(9, 22));

    JTextField phoneTF = BasicComponentFactory.createTextField(pm.getTournamentValueModel(Tournament.PROPERTY_PHONE));
    jpanel1.add(phoneTF, cc.xywh(11, 20, 3, 1));

    JTextField emailTF = BasicComponentFactory.createTextField(pm.getTournamentValueModel(Tournament.PROPERTY_EMAIL));
    jpanel1.add(emailTF, cc.xywh(11, 22, 3, 1));

    JLabel jlabel12 = new JLabel();
    jlabel12.setText(ResourceManager.getText(Text.NOTES));
    jpanel1.add(jlabel12, cc.xy(9, 24));

    JScrollPane jscrollpane3 = new JScrollPane();
    jscrollpane3.setPreferredSize(new Dimension(-1, 100));
    JTextArea notesTA = BasicComponentFactory.createTextArea(pm.getTournamentValueModel(Tournament.PROPERTY_NOTES));
    jscrollpane3.setViewportView(notesTA);
    jscrollpane3
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane3
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane3, cc.xywh(9, 25, 5, 1));

    JLabel jlabel13 = new JLabel();
    jlabel13.setText(ResourceManager.getText(Text.SPORT));
    jpanel1.add(jlabel13, cc.xy(9, 12));

    JComboBox<Sport> sportsCb = new JComboBox<Sport>(new ComboBoxAdapter<ArrayListModel<Sport>>(pm.getSports(), pm.getTournamentValueModel(Tournament.PROPERTY_SPORT)));

    jpanel1.add(sportsCb, cc.xywh(11, 12, 3, 1));

    JButton newBtn = new JButton(pm.getAction(TPanelPModel.NEW_SPORT_ACTION));
    jpanel1.add(newBtn, cc.xy(11, 14));

    JButton editBtn = new JButton(pm.getAction(TPanelPModel.EDIT_SPORT_ACTION));
    jpanel1.add(editBtn, cc.xy(13, 14));

    JButton logoBtn = new JButton(pm.getAction(TPanelPModel.LOGO_ACTION));
    logoBtn.setPreferredSize(new Dimension(300,300));
    logoBtn.setMinimumSize(new Dimension(300,300));
    logoBtn.setSize(new Dimension(300,300));
    logoBtn.setFocusable(false);
    jpanel1.add(logoBtn, cc.xywh(9, 2, 5, 6));
    
    popup = new JPopupMenu();
    popup.add(new JMenuItem(pm.getAction(TPanelPModel.RESET_ICON_ACTION)));
    logoBtn.addMouseListener(new PopupListener());

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
        12, 13}, new int[] {1, 3, 6, 8, 10, 13, 14, 15, 17, 19, 21, 23, 26});
    return jpanel1;
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
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

}
