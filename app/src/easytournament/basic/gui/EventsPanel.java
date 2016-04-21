package easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;

import com.jgoodies.binding.adapter.BasicComponentFactory;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.GameEventDialog;
import easytournament.basic.model.EventsPanelPModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.popupmenu.TablePopupMenu;
import easytournament.basic.util.transferhandler.GameEventTransferHandler;
import easytournament.basic.valueholder.Sport;

public class EventsPanel extends JPanel implements PropertyChangeListener {

  protected EventsPanelPModel pm;
  protected JTable usedTable, availTable;
  protected TablePopupMenu popup, popupAvail;

  public EventsPanel(EventsPanelPModel pm) {
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    init();
  }

  private void init() {
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

    Box hBox = Box.createHorizontalBox();
    hBox.add(new JButton(pm.getAction(EventsPanelPModel.NEW_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(EventsPanelPModel.EDIT_ACTION)));
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(EventsPanelPModel.DELETE_ACTION)));
    hBox.setMaximumSize(new Dimension((int)hBox.getPreferredSize().getWidth(), 20));

    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.TOURN_EVENTS));
    title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, title
        .getFont().getSize()));
    title.setAlignmentX(CENTER_ALIGNMENT);
    vBox.add(title);
    vBox.add(Box.createVerticalStrut(10));
    vBox.add(hBox);
    vBox.add(Box.createVerticalStrut(5));
    JScrollPane jscrollpane1 = new JScrollPane();
    usedTable = new JTable(pm.getTableModel(EventsPanelPModel.USED_TABLE));
    usedTable.setSelectionModel(pm
        .getSelectionModel(EventsPanelPModel.USED_TABLE));
    usedTable.setFillsViewportHeight(true);
    jscrollpane1.setPreferredSize(new Dimension(400, -1));
    usedTable.setDragEnabled(true);
    usedTable.setDropMode(DropMode.INSERT);
    usedTable.setTransferHandler(new GameEventTransferHandler(
        TransferHandler.NONE, true));
    usedTable.getColumnModel().getColumn(0).setMaxWidth(22);
    usedTable.getColumnModel().getColumn(1).setMaxWidth(100);
    usedTable.getColumnModel().getColumn(0)
        .setCellRenderer(new DefaultTableCellRenderer() {

          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {
            this.setIcon((ImageIcon)value);
            return super.getTableCellRendererComponent(table, null, isSelected,
                hasFocus, row, column);
          }

        });
    usedTable.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
          pm.fireDoubleClickAction(usedTable.getSelectedRow());
        }
      }

    });
    usedTable.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        super.keyReleased(ke);
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
          pm.deleteAction();
        }
      }

    });
    jscrollpane1.setViewportView(usedTable);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    vBox.add(jscrollpane1);
    vBox.add(Box.createVerticalStrut(10));

    popup = new TablePopupMenu();
    JMenuItem newItem = new JMenuItem(
        pm.getAction(EventsPanelPModel.NEW_ACTION));
    popup.add(newItem);
    JMenuItem editItem = new JMenuItem(
        pm.getAction(EventsPanelPModel.EDIT_ACTION));
    popup.add(editItem);
    JMenuItem delItem = new JMenuItem(
        pm.getAction(EventsPanelPModel.DELETE_ACTION));
    popup.add(delItem);

    usedTable.addMouseListener(new PopupListener());
    jscrollpane1.addMouseListener(new PopupListener());

    return vBox;
  }

  private Box getCenterBox() {
    Box vBox = Box.createVerticalBox();
    vBox.add(Box.createVerticalGlue());
    JButton add = new JButton(pm.getAction(EventsPanelPModel.ADD_ACTION));
    vBox.add(add);
    vBox.add(Box.createVerticalGlue());
    return vBox;
  }

  private Box getRightBox() {
    JComboBox<Sport> sportsCb = BasicComponentFactory.createComboBox(pm
        .getSportSelectionInList());
    Box hBox = Box.createHorizontalBox();

    hBox.add(sportsCb);
    hBox.add(Box.createHorizontalStrut(10));
    hBox.add(new JButton(pm.getAction(EventsPanelPModel.IMPORT_ACTION)));
    hBox.setMaximumSize(new Dimension(405, 20));

    Box vBox = Box.createVerticalBox();
    JLabel title = new JLabel(ResourceManager.getText(Text.AVAIL_EVENTS));
    title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, title
        .getFont().getSize()));
    title.setAlignmentX(CENTER_ALIGNMENT);

    vBox.add(title);
    vBox.add(Box.createVerticalStrut(10));
    vBox.add(hBox);
    vBox.add(Box.createVerticalStrut(5));
    JScrollPane jscrollpane2 = new JScrollPane();
    availTable = new JTable(pm.getTableModel(EventsPanelPModel.AVAILABLE_TABLE));
    availTable.setSelectionModel(pm
        .getSelectionModel(EventsPanelPModel.AVAILABLE_TABLE));
    jscrollpane2.setPreferredSize(new Dimension(400, -1));

    availTable.setDragEnabled(true);
    availTable.setDropMode(DropMode.INSERT);
    availTable.setTransferHandler(new GameEventTransferHandler(
        TransferHandler.COPY, false));
    availTable.getColumnModel().getColumn(0).setMaxWidth(22);
    availTable.getColumnModel().getColumn(1).setMaxWidth(100);
    availTable.getColumnModel().getColumn(0)
        .setCellRenderer(new DefaultTableCellRenderer() {

          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {
            this.setIcon((ImageIcon)value);
            return super.getTableCellRendererComponent(table, null, isSelected,
                hasFocus, row, column);
          }

        });
    jscrollpane2.setViewportView(availTable);
    jscrollpane2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane2
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    vBox.add(jscrollpane2);
    vBox.add(Box.createVerticalStrut(10));

    popupAvail = new TablePopupMenu();
    JMenuItem add = new JMenuItem(pm.getAction(EventsPanelPModel.ADD_ACTION));
    add.getAction().putValue(Action.NAME,
        ResourceManager.getText(Text.ADD_EVENT));
    add.getAction().putValue(Action.SMALL_ICON,
        ResourceManager.getIcon(Icon.ADD_ICON_SMALL));
    popupAvail.add(add);

    availTable.addMouseListener(new PopupListener());
    return vBox;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(EventsPanelPModel.SHOW_EVENT_DIALOG)) {
      new GameEventDialog(Organizer.getInstance().getMainFrame(), true, pm);
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
        int row;
        TablePopupMenu pop;
        if (e.getComponent().equals(usedTable)) {
          row = usedTable.rowAtPoint(e.getPoint());
          pop = popup;
          if (row >= 0) {
            if (!usedTable.isCellSelected(row, 0))
              usedTable.setRowSelectionInterval(row, row);
            popup.getComponent(1).setVisible(true);
            popup.getComponent(2).setVisible(true);
          }
          else if (row < 0) {
            popup.getComponent(1).setVisible(false);
            popup.getComponent(2).setVisible(false);
          }
        }
        else {
          pop = popupAvail;
          row = availTable.rowAtPoint(e.getPoint());
          if (row >= 0 && !availTable.isCellSelected(row, 0))
            availTable.setRowSelectionInterval(row, row);
        }

        pop.setRow(row);
        pop.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
