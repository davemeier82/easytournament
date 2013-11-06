package com.easytournament.statistic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.tablecellrenderer.CheckboxCellRenderer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.statistic.gui.editor.EventSelectionEditor;
import com.easytournament.statistic.model.ManualStatsPModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

public class ManualStatsPanel extends JPanel implements ChangeListener,
    TableModelListener {

  protected ManualStatsPModel pm;
  protected JComboBox<String> teamCombo;
  protected JFormattedTextField beginTimeFTF, endTimeFTF;
  protected JCheckBox fromDateCB = new JCheckBox();
  protected JCheckBox toDateCB = new JCheckBox();
  protected JCheckBox playtimeCB = new JCheckBox();
  protected JComboBox<String> groupByCombo;
  protected JTable jtable1, table;
  protected TableColumnModel tcm;
  protected JDateChooser startDateChooser, endDateChooser;
  protected JSpinner minSp, secSP, tominSp, tosecSP;

  public ManualStatsPanel(ManualStatsPModel manualStatsPModel) {
    this.pm = manualStatsPModel;
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout(0, 10));
    add(createNorthPanel(), BorderLayout.NORTH);
    add(createCenterPanel(), BorderLayout.CENTER);
    add(getButtonComponent(), BorderLayout.SOUTH);
  }

  public JPanel createNorthPanel() {
    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(80PX;DEFAULT):NONE,FILL:DEFAULT:NONE,FILL:MAX(200PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel(ResourceManager.getText(Text.TEAM));
    jpanel1.add(jlabel1, cc.xy(1, 1));

    teamCombo = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(ManualStatsPModel.TEAMS));
    jpanel1.add(teamCombo, cc.xy(3, 1));

    JLabel jlabel2 = new JLabel(ResourceManager.getText(Text.GROUP_BY));
    jpanel1.add(jlabel2, cc.xy(5, 1));

    groupByCombo = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(ManualStatsPModel.GROUPBY));
    jpanel1.add(groupByCombo, cc.xy(7, 1));

    fromDateCB = BasicComponentFactory.createCheckBox(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_FROMDATE_SELECTED, true),
        ResourceManager.getText(Text.FROM));
    jpanel1.add(fromDateCB, cc.xy(1, 5));
    fromDateCB.addChangeListener(this);

    startDateChooser = new JDateChooser();
    Bindings.bind(startDateChooser, "date", new PropertyAdapter<ManualStatsPModel>(pm, ManualStatsPModel.PROPERTY_BEGIN_DATE, true));
    startDateChooser.setLocale(ResourceManager.getLocale());
    startDateChooser.setEnabled(fromDateCB.isSelected());
    jpanel1.add(startDateChooser, cc.xy(3, 5));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(ResourceManager.getText(Text.TIME));
    jpanel1.add(jlabel3, cc.xy(5, 5));

    toDateCB = BasicComponentFactory.createCheckBox(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_TODATE_SELECTED, true), ResourceManager
            .getText(Text.TO));
    jpanel1.add(toDateCB, cc.xy(1, 7));
    toDateCB.addChangeListener(this);

    endDateChooser = new JDateChooser();
    Bindings.bind(endDateChooser, "date", new PropertyAdapter<ManualStatsPModel>(pm, ManualStatsPModel.PROPERTY_END_DATE, true));
    endDateChooser.setLocale(ResourceManager.getLocale());
    endDateChooser.setEnabled(fromDateCB.isSelected());
    jpanel1.add(endDateChooser, cc.xy(3, 7));

    JLabel jlabel4 = new JLabel(ResourceManager.getText(Text.TIME));
    jpanel1.add(jlabel4, cc.xy(5, 7));

    playtimeCB = BasicComponentFactory.createCheckBox(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_PLAYTIME_SELECTED, true),
        ResourceManager.getText(Text.FROM));
    jpanel1.add(playtimeCB, cc.xy(1, 11));
    playtimeCB.addChangeListener(this);

    Box fromTimeBox = Box.createHorizontalBox();
    minSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_FROM_TIME_MI, true), 0, 0, 100000, 1));
    minSp.setEnabled(playtimeCB.isSelected());
    fromTimeBox.add(minSp);

    JLabel dpL = new JLabel(" : ");
    fromTimeBox.add(dpL);

    secSP = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_FROM_TIME_SEC, true), 0, 0, 59, 1));
    secSP.setEnabled(playtimeCB.isSelected());
    fromTimeBox.add(secSP);
    jpanel1.add(fromTimeBox, cc.xy(3, 11));

    JLabel jlabel5 = new JLabel(ResourceManager.getText(Text.DATE));
    jlabel5.setFont(jlabel5.getFont().deriveFont(Font.BOLD));
    jpanel1.add(jlabel5, cc.xy(1, 3));

    JLabel jlabel6 = new JLabel(ResourceManager.getText(Text.PLAYTIME));
    jlabel6.setFont(jlabel5.getFont().deriveFont(Font.BOLD));
    jpanel1.add(jlabel6, cc.xy(1, 9));

    JLabel jlabel7 = new JLabel(ResourceManager.getText(Text.TO));
    jpanel1.add(jlabel7, cc.xy(5, 11));

    beginTimeFTF = BasicComponentFactory.createFormattedTextField(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_BEGIN_TIME, true), DateFormat
            .getTimeInstance(DateFormat.SHORT, ResourceManager.getLocale()));
    jpanel1.add(beginTimeFTF, cc.xy(7, 5));
    beginTimeFTF.setEnabled(fromDateCB.isSelected());

    endTimeFTF = BasicComponentFactory.createFormattedTextField(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_END_TIME, true), DateFormat
            .getTimeInstance(DateFormat.SHORT, ResourceManager.getLocale()));
    jpanel1.add(endTimeFTF, cc.xy(7, 7));
    endTimeFTF.setEnabled(toDateCB.isSelected());

    Box toTimeBox = Box.createHorizontalBox();
    tominSp = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_TO_TIME_MI, true), 0, 0, 100000, 1));
    tominSp.setEnabled(playtimeCB.isSelected());
    toTimeBox.add(tominSp);

    JLabel todpL = new JLabel(" : ");
    toTimeBox.add(todpL);

    tosecSP = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_TO_TIME_SEC, true), 0, 0, 59, 1));
    tosecSP.setEnabled(playtimeCB.isSelected());
    toTimeBox.add(tosecSP);

    jpanel1.add(toTimeBox, cc.xy(7, 11));

    jtable1 = new JTable(
        pm.getTableModel(ManualStatsPModel.EVENT_SEL_TABLEMODEL)) {

      protected String[] columnToolTips = {
          ResourceManager.getText(Text.GAMEEVENT),
          ResourceManager.getText(Text.SECONDARY_PLAYER),
          ResourceManager.getText(Text.NUMBER_OF),
          ResourceManager.getText(Text.WRONG_TEAM),
          ResourceManager.getText(Text.LABEL)};

      // Implement table header tool tips.
      protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
          public String getToolTipText(MouseEvent e) {
            java.awt.Point p = e.getPoint();
            int index = columnModel.getColumnIndexAtX(p.x);
            int realIndex = columnModel.getColumn(index).getModelIndex();
            return columnToolTips[realIndex];
          }
        };
      }
    };
    jtable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    jtable1.setSelectionModel(pm
        .getSelectionModel(ManualStatsPModel.EVENT_SEL_TABLEMODEL));
    TableColumnModel tcm = jtable1.getColumnModel();
    tcm.getColumn(1).setMaxWidth(30);
    tcm.getColumn(2).setMaxWidth(30);
    tcm.getColumn(3).setMaxWidth(30);
    jtable1.setDefaultEditor(GameEvent.class,
        new EventSelectionEditor(pm.getGameEvents()));
    jtable1.setDefaultRenderer(Boolean.class, new CheckboxCellRenderer());
    JScrollPane jscrollpane1 = new JScrollPane();
    jscrollpane1.setViewportView(jtable1);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(9, 1, 1, 9));

    Box btnBox = Box.createHorizontalBox();
    JButton addBtn = new JButton(pm.getAction(ManualStatsPModel.ADD_ACTION));
    JButton deleteBtn = new JButton(
        pm.getAction(ManualStatsPModel.REMOVE_ACTION));

    btnBox.add(addBtn);
    btnBox.add(Box.createHorizontalStrut(10));
    btnBox.add(deleteBtn);

    jpanel1.add(btnBox, cc.xy(9, 11));

    JCheckBox totalCB = BasicComponentFactory.createCheckBox(
        new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_TOTAL_SELECTED, true), ResourceManager
            .getText(Text.SHOWTOTAL));
    jpanel1.add(totalCB, cc.xywh(1, 13, 4, 1));

    jpanel1.add(new JLabel(ResourceManager.getText(Text.TABLETITLE)),
        cc.xy(1, 15));

    jpanel1.add(BasicComponentFactory
        .createTextField(new PropertyAdapter<ManualStatsPModel>(pm,
            ManualStatsPModel.PROPERTY_TABLE_TITLE, true)), cc.xy(3, 15));

    jpanel1.add(new JButton(pm.getAction(ManualStatsPModel.CALC_ACTION)),
        cc.xy(7, 15));

    addFillComponents(jpanel1, new int[] {2, 4, 6, 8}, new int[] {2, 4, 6, 8,
        10, 12, 14});
    return jpanel1;
  }

  public Component createCenterPanel() {
    TableModel tm = pm.getTableModel(ManualStatsPModel.STATISTIC_TABLEMODEL);
    table = new JTable(tm);
    table.setAutoCreateRowSorter(true);
    tcm = table.getColumnModel();
    tm.addTableModelListener(this);
    DefaultTableCellRenderer dtcr = null;
    if (Organizer.getInstance().isSubstance())
      dtcr = new SubstanceDefaultTableCellRenderer();
    else
      dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    table.setDefaultRenderer(Integer.class, dtcr);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    JScrollPane jscrollpane1 = new JScrollPane();
    jscrollpane1.setViewportView(table);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    return jscrollpane1;
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(pm.getAction(ManualStatsPModel.EXPORT_ACTION)));
    return hBox;
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
  public void tableChanged(TableModelEvent e) {

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = table.getTableHeader().getFontMetrics(
            table.getTableHeader().getFont());
        for (int i = 0; i < table.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          if (table.getColumnClass(i) == String.class) {
            for (int r = 0; r < table.getRowCount(); r++) {
              width = Math.max(width,
                  fm.stringWidth((String)table.getValueAt(r, i)));
            }
          }
          width += 20;
          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
          tcm.getColumn(i).setMinWidth(width);
        }
      }
    });

  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == this.fromDateCB) {
      boolean enabled = this.fromDateCB.isSelected();
      this.beginTimeFTF.setEnabled(enabled);
      this.startDateChooser.setEnabled(enabled);

    }
    else if (e.getSource() == this.toDateCB) {
      boolean enabled = this.toDateCB.isSelected();
      this.endTimeFTF.setEnabled(enabled);
      this.endDateChooser.setEnabled(enabled);
    }
    else if (e.getSource() == this.playtimeCB) {
      boolean enabled = this.playtimeCB.isSelected();
      this.minSp.setEnabled(enabled);
      this.secSP.setEnabled(enabled);
      this.tominSp.setEnabled(enabled);
      this.tosecSP.setEnabled(enabled);
    }

  }

  public JTable getTable() {
    return table;
  }
}
