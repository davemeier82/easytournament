package easytournament.tournament.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Tournament;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.tournament.model.tablemodel.TableTableModel;

public class TablesPanel extends JPanel implements TableModelListener {

  private static final long serialVersionUID = -4311875499612169505L;
  private Tournament t = Organizer.getInstance().getCurrentTournament();
  protected ArrayList<JTable> tables = new ArrayList<JTable>();
  protected ArrayList<JScrollPane> panes = new ArrayList<JScrollPane>();

  public TablesPanel() {
    this.init();
  }

  public void init() {
    for (JTable table : tables) {
      table.getModel().removeTableModelListener(this);
    }
    this.tables.clear();
    this.panes.clear();
    this.removeAll();
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());

    Box vBox = Box.createVerticalBox();

    for (AbstractGroup g : t.getPlan().getOrderedGroups()) {
      TableModel tm = new TableTableModel(g.getTable());
      JTable temp = new JTable(tm){

        protected String[] columnToolTips = {
            ResourceManager.getText(Text.TEAM),
            ResourceManager.getText(Text.GAMES),
            ResourceManager.getText(Text.WINS),
            ResourceManager.getText(Text.DRAWS),
            ResourceManager.getText(Text.DEFEATS),
            ResourceManager.getText(Text.POINTS),
            ResourceManager.getText(Text.SCORED_GOALS),
            ResourceManager.getText(Text.RECEIVED_GOALS),
            ResourceManager.getText(Text.GOAL_DIFFERENCE),};

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
      temp.setAutoCreateRowSorter(true);
      tm.addTableModelListener(this);
      temp.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      JLabel l = new JLabel(g.getName());
      l.setFont(new Font(l.getFont().getFontName(), Font.BOLD, l.getFont()
          .getSize()));
      l.setAlignmentX(Component.LEFT_ALIGNMENT);
      vBox.add(l);
      vBox.add(Box.createVerticalStrut(5));
      DefaultTableCellRenderer dtcr = null;
      if (Organizer.getInstance().isSubstance())
        dtcr = new SubstanceDefaultTableCellRenderer();
      else
        dtcr = new DefaultTableCellRenderer();

      dtcr.setHorizontalAlignment(SwingConstants.CENTER);
      TableColumnModel tcm = temp.getColumnModel();

      for (int i = 1; i < 9; i++)
        tcm.getColumn(i).setCellRenderer(dtcr);

      if (Organizer.getInstance().isSubstance())
        dtcr = new SubstanceDefaultTableCellRenderer() {
          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {

            JLabel label = (JLabel)super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
            label.setFont(label.getFont().deriveFont(Font.BOLD,
                label.getFont().getSize()));
            return label;
          }
        };
      else
        dtcr = new DefaultTableCellRenderer() {
          @Override
          public Component getTableCellRendererComponent(JTable table,
              Object value, boolean isSelected, boolean hasFocus, int row,
              int column) {

            JLabel label = (JLabel)super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
            label.setFont(label.getFont().deriveFont(Font.BOLD,
                label.getFont().getSize()));
            return label;
          }
        };
      dtcr.setHorizontalAlignment(SwingConstants.CENTER);
      dtcr.setFont(new Font(dtcr.getFont().getFontName(), Font.BOLD, dtcr
          .getFont().getSize()));
      tcm.getColumn(5).setCellRenderer(dtcr);

      JScrollPane pane = new JScrollPane(temp);
      pane.setAlignmentX(Component.LEFT_ALIGNMENT);
      Dimension dim = new Dimension(600, temp.getRowHeight()
          * (g.getPositions().size() + 1) + 2);
      pane.setPreferredSize(dim);
      pane.setMinimumSize(dim);
      pane.setMaximumSize(dim);
      vBox.add(pane);
      vBox.add(Box.createVerticalStrut(10));

      tables.add(temp);
      panes.add(pane);
    }
    vBox.add(Box.createVerticalGlue());
    this.setColumnWidths();

    this.add(vBox, BorderLayout.CENTER);
  }

  public ArrayList<JTable> getTables() {
    return tables;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    this.setColumnWidths();
  }

  public void setColumnWidths() {
    if (tables.size() < 1)
      return;

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        JTable table0 = tables.get(0);
        FontMetrics fm = table0.getTableHeader().getFontMetrics(
            table0.getTableHeader().getFont());
        int totalw = table0.getColumnModel().getTotalColumnWidth();

        int width = 150;
        for (JTable t : tables) {
          for (int r = 0; r < t.getRowCount(); r++) {
            width = Math.max(width,
                fm.stringWidth(t.getValueAt(r, 0).toString()) + 20);
          }
        }

        int i = 0;
        for (JTable t : tables) {
          TableColumnModel tcm = t.getColumnModel();
          tcm.getColumn(0).setPreferredWidth(width);
          tcm.getColumn(0).setWidth(width);
          int w = (int)((double)(totalw - width) / (double)(tcm
              .getColumnCount() - 1));
          for (int c = 1; c < tcm.getColumnCount(); c++) {
            tcm.getColumn(c).setPreferredWidth(w);
            tcm.getColumn(c).setWidth(w);
          }
          JScrollPane p = panes.get(i++);
          Dimension dim = new Dimension(totalw - 3, (int)p.getMaximumSize()
              .getHeight());
          p.setPreferredSize(dim);
          p.setMinimumSize(dim);
          p.setMaximumSize(dim);
        }
      }
    });
  }
}
