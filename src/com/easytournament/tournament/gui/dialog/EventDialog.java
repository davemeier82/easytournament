package com.easytournament.tournament.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;


import com.easytournament.basic.gameevent.GameEventType;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.transferhandler.PlayerTransferHandler;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.tournament.model.dialog.EventDialogPModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class EventDialog extends JDialog implements PropertyChangeListener {

  protected EventDialogPModel pm;
  protected JList<String> assistList;
  protected JLabel assistLabel;
  protected JButton addAssistBtn, removeAssistBtn;
  protected JScrollPane assistScrollPane;
  protected SelectionInList<?> sil;

  public EventDialog(Dialog f, boolean modal, EventDialogPModel pm) {
    super(f, ResourceManager.getText(Text.EVENT), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    this.init();
    this.pack();
    this.setLocationRelativeTo(f);    
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        EventDialog.this.sil.removeValueChangeListener(EventDialog.this);
        EventDialog.this.pm.removePropertyChangeListener(EventDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializer
   */
  protected void init() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
    add(getButtonPanel(), BorderLayout.SOUTH);
  }

  public JPanel createPanel() {
    JPanel jpanel1 = new JPanel();
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(50PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(30PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:5PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,TOP:35PX:NONE,CENTER:DEFAULT:NONE,TOP:35PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:25PX:NONE,CENTER:DEFAULT:NONE,TOP:35PX:NONE,TOP:DEFAULT:NONE,CENTER:35PX:NONE,FILL:80PX:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel(ResourceManager.getText(Text.EVENT));
    jpanel1.add(jlabel1, cc.xy(1, 2));

    sil = pm.getSelectionInList(GameEvent.PROPERTY_NAME);
    sil.addValueChangeListener(this);
    JComboBox<GameEventType> nameCB = BasicComponentFactory.createComboBox(sil);
    jpanel1.add(nameCB, cc.xy(3, 2));

    JLabel timeL = new JLabel(ResourceManager.getText(Text.PLAYTIME));
    jpanel1.add(timeL, cc.xy(7, 2));

    JSpinner minSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getValueModel(GameEventEntry.PROPERTY_TIME_MIN), 0, 0, 100000, 1));
    jpanel1.add(minSp, cc.xy(9, 2));

    JLabel dpL = new JLabel(" : ");
    jpanel1.add(dpL, cc.xy(10, 2));

    JSpinner secSP = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getValueModel(GameEventEntry.PROPERTY_TIME_SEC), 0, 0, 59, 1));
    jpanel1.add(secSP, cc.xy(11, 2));

    JRadioButton homeBtn = BasicComponentFactory.createRadioButton(
        new PropertyAdapter<EventDialogPModel>(pm, EventDialogPModel.PROPERTY_HOMETEAM, true), true,
        pm.getTeamName(true));
    jpanel1.add(homeBtn, new CellConstraints(1, 4, 7, 1, CellConstraints.LEFT,
        CellConstraints.DEFAULT));

    JRadioButton awayBtn = BasicComponentFactory.createRadioButton(
        new PropertyAdapter<EventDialogPModel>(pm, EventDialogPModel.PROPERTY_HOMETEAM, true), false,
        pm.getTeamName(false));
    jpanel1.add(awayBtn, new CellConstraints(1, 6, 7, 1, CellConstraints.LEFT,
        CellConstraints.DEFAULT));

    JButton addPlayerBtn = new JButton(
        pm.getAction(EventDialogPModel.ADD_PLAYER_ACTION));
    jpanel1.add(addPlayerBtn, new CellConstraints(5, 10, 1, 1,
        CellConstraints.LEFT, CellConstraints.DEFAULT));

    JButton removePlayerBtn = new JButton(
        pm.getAction(EventDialogPModel.REMOVE_PLAYER_ACTION));
    jpanel1.add(removePlayerBtn, new CellConstraints(5, 12, 1, 1,
        CellConstraints.LEFT, CellConstraints.DEFAULT));

    addAssistBtn = new JButton(
        pm.getAction(EventDialogPModel.ADD_ASSIST_ACTION));
    jpanel1.add(addAssistBtn, new CellConstraints(5, 17, 1, 1,
        CellConstraints.LEFT, CellConstraints.DEFAULT));

    removeAssistBtn = new JButton(
        pm.getAction(EventDialogPModel.REMOVE_ASSIST_ACTION));
    jpanel1.add(removeAssistBtn, new CellConstraints(5, 19, 1, 1,
        CellConstraints.LEFT, CellConstraints.DEFAULT));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.PLAYER_S));
    jpanel1.add(jlabel2, cc.xywh(1, 8, 4, 1));

    assistLabel = new JLabel(pm.getSecondaryText());
    jpanel1.add(assistLabel, cc.xywh(1, 15, 6, 1));

    JComboBox<Team> teams = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(EventDialogPModel.PROPERTY_TEAMFORLIST));
    jpanel1.add(teams, cc.xyw(7, 8, 5));

    JScrollPane jscrollpane1 = new JScrollPane();
    JList<String> playerList = new JList<String>(
        pm.getListModel(EventDialogPModel.PLAYER_LIST));
    playerList.setSelectionModel(pm
        .getListSelectionModel(EventDialogPModel.PLAYER_LIST));
    playerList.setDragEnabled(true);
    playerList.setDropMode(DropMode.INSERT);
    playerList.setTransferHandler(new PlayerTransferHandler(
        TransferHandler.MOVE, true, false));
    jscrollpane1.setViewportView(playerList);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(1, 9, 3, 5));

    assistScrollPane = new JScrollPane();
    assistList = new JList<String>(
        pm.getListModel(EventDialogPModel.ASSIST_LIST));
    assistList.setSelectionModel(pm
        .getListSelectionModel(EventDialogPModel.ASSIST_LIST));
    assistList.setDragEnabled(true);
    assistList.setDropMode(DropMode.INSERT);
    assistList.setTransferHandler(new PlayerTransferHandler(
        TransferHandler.MOVE, true, false));
    assistScrollPane.setViewportView(assistList);
    assistScrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    assistScrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(assistScrollPane, cc.xywh(1, 16, 3, 5));

    JScrollPane jscrollpane3 = new JScrollPane();
    JList<String> allPlayersList = new JList<String>(
        pm.getListModel(EventDialogPModel.ALL_PLAYERS_LIST));
    allPlayersList.setSelectionModel(pm
        .getListSelectionModel(EventDialogPModel.ALL_PLAYERS_LIST));
    allPlayersList.setDragEnabled(true);
    allPlayersList.setDropMode(DropMode.INSERT);
    allPlayersList.setTransferHandler(new PlayerTransferHandler(
        TransferHandler.COPY, false, true));
    jscrollpane3.setViewportView(allPlayersList);
    jscrollpane3
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane3
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane3, cc.xywh(7, 10, 5, 11));
    setSecondaryVisible();

    addFillComponents(jpanel1, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
        new int[] {1, 3, 5, 7, 10, 11, 12, 13, 14, 17, 18, 19, 20});
    return jpanel1;
  }

  private JPanel getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(EventDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(EventDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == sil) {
      setSecondaryVisible();
    }
    else {
      if (evt.getPropertyName() == EventDialogPModel.DISPOSE)
        sil.removeValueChangeListener(this);
        pm.removePropertyChangeListener(this);
        this.dispose();
    }
  }

  private void setSecondaryVisible() {
    boolean visible = pm.isSecondaryVisible();
    assistScrollPane.setVisible(visible);
    assistList.setVisible(visible);
    assistLabel.setVisible(visible);
    assistLabel.setText(pm.getSecondaryText());
    addAssistBtn.setVisible(visible);
    removeAssistBtn.setVisible(visible);
  }

}
