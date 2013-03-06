package com.easytournament.designer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.tablecelleditor.DateCellEditor;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.popupmenu.TablePopupMenu;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.designer.gui.dialog.ScheduleGeneratorDialog;
import com.easytournament.designer.gui.editor.PositionSelectionEditor;
import com.easytournament.designer.gui.editor.RefreeSelectionEditor;
import com.easytournament.designer.gui.renderer.PositionListCellRenderer;
import com.easytournament.designer.gui.renderer.PositionTableCellRenderer;
import com.easytournament.designer.gui.renderer.SubstPositionTableCellRenderer;
import com.easytournament.designer.model.SchedulePanelPModel;
import com.easytournament.designer.model.dialog.SGeneratorPModel;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.tournament.gui.renderer.DateTableCellRenderer;
import com.easytournament.tournament.gui.renderer.SubstDateTableCellRenderer;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class SchedulePanel extends JPanel implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private SchedulePanelPModel pm;
  private TablePopupMenu popup;
  private JTable schedTable;
  private TableColumnModel tcm;

  public SchedulePanel(SchedulePanelPModel pm) {
    this.pm = pm;
    this.setLayout(new BorderLayout(0, 10));
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getGroupsComponent(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
    this.pm.addPropertyChangeListener(this);
  }

  private JComponent getButtonComponent() {
    Box hBox = Box.createHorizontalBox();
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(new AbstractAction(ResourceManager
        .getText(Text.GENERATE_GAMES), ResourceManager
        .getIcon(Icon.GENERATE_GAMES_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        new ScheduleGeneratorDialog(Organizer.getInstance().getMainFrame(),
            true, new SGeneratorPModel());
      }
    }));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(SchedulePanelPModel.NEW_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(SchedulePanelPModel.DELETE_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(SchedulePanelPModel.EXPORT_ACTION)));

    return hBox;
  }

  private JComponent getGroupsComponent() {

    TableModel tm = pm.getTableModel();
    tm.addTableModelListener(this);
    schedTable = new JTable(tm);
    // schedTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    schedTable.setSelectionModel(pm.getSelectionModel());
    schedTable.setFillsViewportHeight(true);
    schedTable.getTableHeader().setReorderingAllowed(false);
    tcm = schedTable.getColumnModel();
    schedTable.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });
    DefaultTableCellRenderer dtcr = null;
    if (Organizer.getInstance().isSubstance()) {
      dtcr = new SubstanceDefaultTableCellRenderer();
      schedTable.setDefaultRenderer(Date.class,
          new SubstDateTableCellRenderer());
    }
    else {
      dtcr = new DefaultTableCellRenderer();
      schedTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
    }
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    schedTable.setDefaultEditor(Refree.class,
        new RefreeSelectionEditor(pm.getRefrees()));

    TableColumn home = tcm.getColumn(0);
    TableColumn away = tcm.getColumn(1);
    TableColumn date = tcm.getColumn(3);
    TableColumn time = tcm.getColumn(4);

    PositionListCellRenderer listCellRenderer = new PositionListCellRenderer();
    Bindings.bind(listCellRenderer,
        PositionListCellRenderer.PROPERTY_SHOW_TEAMS,
        new PropertyAdapter<SchedulePanelPModel>(pm,
            SchedulePanelPModel.PROPERTY_SHOW_TEAMS, true));

    home.setCellEditor(new PositionSelectionEditor(listCellRenderer));
    away.setCellEditor(new PositionSelectionEditor(0, listCellRenderer));

    if (Organizer.getInstance().isSubstance()) {
      SubstPositionTableCellRenderer cellrenderer = new SubstPositionTableCellRenderer();
      home.setCellRenderer(cellrenderer);
      away.setCellRenderer(cellrenderer);
      Bindings.bind(cellrenderer,
          SubstPositionTableCellRenderer.PROPERTY_SHOW_TEAMS,
          new PropertyAdapter<SchedulePanelPModel>(pm,
              SchedulePanelPModel.PROPERTY_SHOW_TEAMS, true));
    }
    else {
      PositionTableCellRenderer cellrenderer = new PositionTableCellRenderer();
      home.setCellRenderer(cellrenderer);
      away.setCellRenderer(cellrenderer);
      Bindings.bind(cellrenderer,
          PositionTableCellRenderer.PROPERTY_SHOW_TEAMS,
          new PropertyAdapter<SchedulePanelPModel>(pm,
              SchedulePanelPModel.PROPERTY_SHOW_TEAMS, true));
    }
    JTextFieldDateEditor dateTextfield = new JTextFieldDateEditor();
    JDateChooser cal = new JDateChooser(dateTextfield);
    cal.setLocale(ResourceManager.getLocale());
    date.setCellEditor(new DateCellEditor(cal));
    time.setCellRenderer(dtcr);

    // stop editing if the enter button was pressed in the datechooser
    dateTextfield.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          super.keyPressed(e);
          if (schedTable.isEditing())
            schedTable.getCellEditor().stopCellEditing();
        }
      }
    });

    schedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    JScrollPane spane = new JScrollPane(schedTable);
    spane.setPreferredSize(new Dimension(600, -1));

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        pm.getAction(SchedulePanelPModel.NEW_ACTION));
    popup.add(newItem);
    JMenuItem deleteItem = new JMenuItem(
        pm.getAction(SchedulePanelPModel.DELETE_ACTION));
    popup.add(deleteItem);
    schedTable.addMouseListener(new PopupListener());
    spane.addMouseListener(new PopupListener());
    return spane;
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
        int row = schedTable.rowAtPoint(e.getPoint());
        if (row >= 0 && !schedTable.isCellSelected(row, 0))
          schedTable.setRowSelectionInterval(row, row);
        popup.setRow(row);
        popup.setCol(schedTable.rowAtPoint(e.getPoint()));
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FontMetrics fm = schedTable.getTableHeader().getFontMetrics(
            schedTable.getTableHeader().getFont());
        for (int i = 0; i < schedTable.getColumnCount(); i++) {
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          if (i == 3) {
            try {
              Date date = (Date)schedTable.getValueAt(0, i);
              DateFormat dateFormatter = DateFormat.getDateInstance(
                  DateFormat.SHORT, ResourceManager.getLocale());
              String dateStr = dateFormatter.format(date);
              width = fm.stringWidth(dateStr) + 50;
            }
            catch (Exception ex) {
              // do nothing
            }
          }
          else {
            for (int r = 0; r < schedTable.getRowCount(); r++) {
              Object value = schedTable.getValueAt(r, i);
              try {
                Position p = (Position)value;
                if (pm.isDataChanged()) {
                  JLabel label = (JLabel) schedTable.getCellRenderer(r, i)
                      .getTableCellRendererComponent(schedTable, value, false,
                          false, r, i);
                  width = Math.max(width, fm.stringWidth(label.getText()));
                }
                else {
                  if (p != null)
                    width = Math.max(width, fm.stringWidth(p.toString()));
                }

              }
              catch (Exception ex) {
                if (value != null)
                  width = Math.max(width, fm.stringWidth(value.toString()));
              }

            }
            if (i < 2)
              width += 50;
            else
              width += 20;
          }

          tcm.getColumn(i).setPreferredWidth(width);
          tcm.getColumn(i).setWidth(width);
          tcm.getColumn(i).setMinWidth(width);
        }
      }
    });
  }

  public JTable getTable() {
    return schedTable;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(
        SchedulePanelPModel.PROPERTY_STOP_CELL_EDITING)) {
      if (schedTable.isEditing())
        schedTable.getCellEditor().stopCellEditing();
    }
  }
}
