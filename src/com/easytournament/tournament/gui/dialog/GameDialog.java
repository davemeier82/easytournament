package com.easytournament.tournament.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.print.TablePrinter;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;
import com.easytournament.tournament.model.dialog.EventDialogPModel;
import com.easytournament.tournament.model.dialog.GameDialogPModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GameDialog extends JDialog implements PropertyChangeListener,
    ChangeListener, TableModelListener {

  private static final int MAX_SCORE = 1000000;
  private static final int MIN_SCORE = -1000000;
  private static final long serialVersionUID = 1L;
  private GameDialogPModel pm;

  protected JCheckBox penaltyCB;
  protected JSpinner hPspinner, aPspinner;
  protected ArrayList<JSpinner> spinners;
  protected ArrayList<JSpinner> oTspinners;
  protected ArrayList<JCheckBox> overtimeCBs;
  protected JTable eventTable;
  protected TablePopupMenu popup;
  protected TableColumnModel tcm;

  public GameDialog(Frame f, boolean modal, GameDialogPModel pm) {
    super(f, "", modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        GameDialog.this.pm.removePropertyChangeListener(GameDialog.this);
        super.windowClosed(e);
      }

    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.init();
    this.setSize(1000, 600);
    this.setLocationRelativeTo(f);
    this.setVisible(true);

  }

  private void init() {
    this.setTitle(ResourceManager.getText(Text.GAME));

    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(createWestPanel(), BorderLayout.WEST);
    cp.add(createCenterPanel(), BorderLayout.CENTER);
    cp.add(this.getButtonPanel(), BorderLayout.SOUTH);
  }

  public JComponent createWestPanel() {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    String rows = "";
    int nGameParts = pm.getNumGameTimes();
    int nOvertimes = pm.getNumOvertimes();
    for (int i = 0; i < (nGameParts + nOvertimes + 3); i++) {
      rows += "CENTER:DEFAULT:NONE,CENTER:10PX:NONE,";
    }
    FormLayout formlayout1 = new FormLayout(
        "CENTER:30PX:NONE,FILL:MAX(100PX;DEFAULT):NONE,FILL:10PX:NONE,FILL:80PX:NONE,CENTER:20PX:NONE,FILL:80PX:NONE",
        rows);
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);
    p.add(new JLabel(ResourceManager.getText(Text.GOALS)), cc.xyw(1, 1, 2));
    p.add(new JLabel(pm.getTeamName(true)), cc.xy(4, 1));
    p.add(new JLabel(pm.getTeamName(false)), cc.xy(6, 1));
    spinners = new ArrayList<JSpinner>();
    oTspinners = new ArrayList<JSpinner>();
    overtimeCBs = new ArrayList<JCheckBox>();
    int y = 3;
    for (int i = 0; i < nGameParts; i++, y += 2) {
      p.add(new JLabel(ResourceManager.getText(Text.PART) + " " + (i + 1)),
          cc.xy(2, y));
      JSpinner hspinner = new JSpinner(
          SpinnerAdapterFactory.createNumberAdapter(pm.getScoreModel(i, true),
              0, MIN_SCORE, MAX_SCORE, 1));
      p.add(hspinner, cc.xy(4, y));
      p.add(new JLabel(":"), cc.xy(5, y));
      JSpinner aspinner = new JSpinner(
          SpinnerAdapterFactory.createNumberAdapter(pm.getScoreModel(i, false),
              0, MIN_SCORE, MAX_SCORE, 1));
      p.add(aspinner, cc.xy(6, y));
      spinners.add(hspinner);
      spinners.add(aspinner);
    }
    for (int i = 0; i < nOvertimes; i++, y += 2) {
      JCheckBox overtimeCB = BasicComponentFactory.createCheckBox(
          pm.getCheckedModel(nGameParts + i),
          "");
      overtimeCB.addChangeListener(this);
      p.add(overtimeCB, cc.xy(1, y));
      this.overtimeCBs.add(overtimeCB);
      p.add(new JLabel(ResourceManager.getText(Text.OVERTIME) + " " + (i + 1)),
          cc.xy(2, y));
      JSpinner hOTspinner = new JSpinner(
          SpinnerAdapterFactory.createNumberAdapter(
              pm.getScoreModel(nGameParts + i, true), 0, MIN_SCORE, MAX_SCORE,
              1));
      hOTspinner.setEnabled(true);
      p.add(hOTspinner, cc.xy(4, y));
      p.add(new JLabel(":"), cc.xy(5, y));
      this.oTspinners.add(hOTspinner);
      JSpinner aOTspinner = new JSpinner(
          SpinnerAdapterFactory.createNumberAdapter(
              pm.getScoreModel(nGameParts + i, false), 0, MIN_SCORE, MAX_SCORE,
              1));
      aOTspinner.setEnabled(true);
      p.add(aOTspinner, cc.xy(6, y));
      this.oTspinners.add(aOTspinner);
    }

    penaltyCB = BasicComponentFactory.createCheckBox(
        pm.getCheckedModel(nGameParts + nOvertimes),
        "");
    penaltyCB.addChangeListener(this);
    p.add(penaltyCB, cc.xy(1, y));
    p.add(new JLabel(ResourceManager.getText(Text.PENALTYSHOOTOUT)),
        cc.xy(2, y));
    hPspinner = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getScoreModel(nGameParts + nOvertimes, true), 0, MIN_SCORE,
        MAX_SCORE, 1));
    hPspinner.setEnabled(false);
    p.add(hPspinner, cc.xy(4, y));
    p.add(new JLabel(":"), cc.xy(5, y));
    aPspinner = new JSpinner(SpinnerAdapterFactory.createNumberAdapter(
        pm.getScoreModel(nGameParts + nOvertimes, false), 0, MIN_SCORE,
        MAX_SCORE, 1));
    aPspinner.setEnabled(false);
    p.add(aPspinner, cc.xy(6, y));
    y += 2;
    p.add(new JLabel(ResourceManager.getText(Text.RESULT)), cc.xy(2, y));
    JFormattedTextField hfield = BasicComponentFactory.createIntegerField(pm
        .getScoreModel(nGameParts + nOvertimes + 1, true));
    hfield.setHorizontalAlignment(SwingConstants.CENTER);
    hfield.setEditable(false);
    p.add(hfield, cc.xy(4, y));
    p.add(new JLabel(":"), cc.xy(5, y));
    JFormattedTextField afield = BasicComponentFactory.createIntegerField(pm
        .getScoreModel(nGameParts + nOvertimes + 1, false));
    p.add(afield, cc.xy(6, y));
    afield.setHorizontalAlignment(SwingConstants.CENTER);
    afield.setEditable(false);

    return new JScrollPane(p);
  }

  public JPanel createCenterPanel() {
    JPanel p = new JPanel(new BorderLayout());
    TableModel tm = pm.getTableModel();
    eventTable = new JTable(tm);
    tm.addTableModelListener(this);
    eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    eventTable.setSelectionModel(pm.getTableSelectionModel());
    tcm = eventTable.getColumnModel();
    tcm.getColumn(0).setMaxWidth(22);
    eventTable.setFillsViewportHeight(true);
    tcm.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

      @Override
      public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row,
          int column) {
        this.setIcon((ImageIcon)value);
        return super.getTableCellRendererComponent(table, null, isSelected,
            hasFocus, row, column);
      }
    });

    DefaultTableCellRenderer dtcr = null;
    if(Organizer.getInstance().isSubstance())
      dtcr = new SubstanceDefaultTableCellRenderer();
    else
      dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    tcm.getColumn(2).setCellRenderer(dtcr);
    tcm.getColumn(4).setCellRenderer(dtcr);
    tcm.getColumn(5).setCellRenderer(dtcr);

    eventTable.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });
    eventTable.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
          EventDialogPModel edpm = pm.getEventDialogPModel(eventTable
              .rowAtPoint(e.getPoint()));
          if (edpm != null)
            new EventDialog(GameDialog.this, true, edpm);
        }
      }

    });
    JScrollPane spane = new JScrollPane();
    spane.setViewportView(eventTable);
    spane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    spane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    p.add(spane, BorderLayout.CENTER);

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(pm.getAction(GameDialogPModel.ADD_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(
        pm.getAction(GameDialogPModel.EDIT_ACTION));
    popup.add(editItem);
    JMenuItem delItem = new JMenuItem(
        pm.getAction(GameDialogPModel.DELETE_ACTION));
    popup.add(delItem);

    eventTable.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton add = new JButton(this.pm.getAction(GameDialogPModel.ADD_ACTION));
    JButton edit = new JButton(this.pm.getAction(GameDialogPModel.EDIT_ACTION));
    JButton delete = new JButton(
        this.pm.getAction(GameDialogPModel.DELETE_ACTION));
    JButton print = new JButton(
        this.pm.getAction(GameDialogPModel.PRINT_ACTION));
    JButton export = new JButton(
        this.pm.getAction(GameDialogPModel.EXPORT_ACTION));
    buttons.add(add);
    buttons.add(edit);
    buttons.add(delete);
    buttons.add(print);
    buttons.add(export);
    p.add(buttons, BorderLayout.SOUTH);

    enableFields(pm.getGameEvents() == 0);
    setColumnWidths();

    return p;
  }

  private JPanel getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(GameDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(GameDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == GameDialogPModel.DISPOSE) {
      pm.removePropertyChangeListener(this);
      this.dispose();
    }
    else if (evt.getPropertyName() == GameDialogPModel.SHOW_EVENTDIALOG) {
      new EventDialog(this, true,
          (EventDialogPModel)evt.getNewValue());
    }
    else if (evt.getPropertyName() == GameDialogPModel.ENABLE_RESULTS) {
      enableFields((Boolean)evt.getNewValue());
    }
    else if (evt.getPropertyName() == GameDialogPModel.PRINT) {
      print();
    }
  }

  private void enableFields(boolean enable) {
    for (JSpinner s : spinners)
      s.setEnabled(enable);
    for (JCheckBox cb : this.overtimeCBs) {
      cb.setEnabled(enable);
    }
    if (enable) {
      this.penaltyCB.setEnabled(true);
      this.hPspinner.setEnabled(this.penaltyCB.isSelected());
      this.aPspinner.setEnabled(this.penaltyCB.isSelected());
      for (int i = 0; i < overtimeCBs.size(); i++) {
        JCheckBox t = this.overtimeCBs.get(i);
        this.oTspinners.get(2 * i).setEnabled(t.isSelected());
        this.oTspinners.get(2 * i + 1).setEnabled(t.isSelected());
      }
    }
    else {
      penaltyCB.setEnabled(false);
      hPspinner.setEnabled(false);
      aPspinner.setEnabled(false);
      for (JSpinner s : oTspinners) {
        s.setEnabled(false);
      }

    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == this.penaltyCB) {
      this.hPspinner.setEnabled(this.penaltyCB.isSelected());
      this.aPspinner.setEnabled(this.penaltyCB.isSelected());
      if (pm.getGameEvents() < 1) {
        this.hPspinner.setValue(0);
        this.aPspinner.setValue(0);
      }
    }
    else {
      for (int i = 0; i < overtimeCBs.size(); i++) {
        JCheckBox t = this.overtimeCBs.get(i);
        if (e.getSource() == t) {
          JSpinner s1 = this.oTspinners.get(2 * i);
          s1.setEnabled(t.isSelected());
          JSpinner s2 = this.oTspinners.get(2 * i + 1);
          s2.setEnabled(t.isSelected());

          if (!t.isSelected() && pm.getGameEvents() < 1) {
            s1.setValue(0);
            s2.setValue(0);
          }
        }
      }

    }
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    setColumnWidths();
  }

  public void setColumnWidths() {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = eventTable.getTableHeader().getFontMetrics(
            eventTable.getTableHeader().getFont());
        int col3 = 0;
        for (int i = 1; i < eventTable.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          for (int r = 0; r < eventTable.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(eventTable.getValueAt(r, i).toString()));
          }
          width = Math.max(50, width + 20);
          if (i == 3)
            col3 = width;

          if (i == 6)
            if (col3 > width)
              width = col3;
            else {
              tcm.getColumn(3).setWidth(width);
              tcm.getColumn(3).setPreferredWidth(width);
            }

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
        }
      }
    });
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

        int row = eventTable.rowAtPoint(e.getPoint());
        TablePopupMenu pop = popup;

        if (row >= 0) {
          if (!eventTable.isCellSelected(row, 0))
            eventTable.setRowSelectionInterval(row, row);
          popup.getComponent(1).setVisible(true);
          popup.getComponent(2).setVisible(true);
        }
        else if (row < 0) {
          popup.getComponent(1).setVisible(false);
          popup.getComponent(2).setVisible(false);
        }

        pop.setRow(row);
        pop.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  private void print() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName(ResourceManager.getText(Text.GAMEREPORT));
    eventTable.setRowSelectionAllowed(false);
    job.setPrintable(new TablePrinter(eventTable, ResourceManager
        .getText(Text.GAMEREPORT)));
    if (job.printDialog()) {
      try {
        job.print();
      }
      catch (Exception e) {
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
    eventTable.setRowSelectionAllowed(true);
  }

}
