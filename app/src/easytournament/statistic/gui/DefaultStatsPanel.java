package easytournament.statistic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.jgoodies.binding.adapter.BasicComponentFactory;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.statistic.model.DefaultStatsPModel;

public class DefaultStatsPanel extends JPanel implements
    PropertyChangeListener, TableModelListener {

  protected JComboBox<String> teamCB;
  protected JComboBox<String> groupByCB;
  protected JComboBox<String> statCB;
  protected TableColumnModel tcm;
  protected JTable table;
  protected JScrollPane pane;
  
  protected DefaultStatsPModel pm;

  public DefaultStatsPanel(DefaultStatsPModel pm) {
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    this.init();
  }

  protected void init() {
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.setLayout(new BorderLayout());
    this.add(getSelectionPanel(), BorderLayout.NORTH);
    this.add(getTablePanel(), BorderLayout.CENTER);
    this.add(getButtonComponent(), BorderLayout.SOUTH);
  }

  private Component getSelectionPanel() {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(new JLabel(ResourceManager.getText(Text.STATISTIC)+":"));
    statCB = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(DefaultStatsPModel.STATS));
    p.add(statCB);
    p.add(new JLabel(ResourceManager.getText(Text.TEAM)+":"));
    teamCB = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(DefaultStatsPModel.TEAMS));
    p.add(teamCB);
    p.add(new JLabel(ResourceManager.getText(Text.GROUP_BY)+":"));
    groupByCB = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(DefaultStatsPModel.GROUPBY));
    p.add(groupByCB);
    return p;
  }

  private Component getTablePanel() {
    DefaultTableModel tm = pm.getTableModel();
    table = new JTable(tm);
    table.setAutoCreateRowSorter(true);
    tcm = table.getColumnModel();
    DefaultTableCellRenderer dtcr = null;
    if(Organizer.getInstance().isSubstance())
      dtcr = new SubstanceDefaultTableCellRenderer();
    else
      dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    table.setDefaultRenderer(Integer.class, dtcr);
    tm.addTableModelListener(this);
    pane = new JScrollPane(table);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    return pane;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == DefaultStatsPModel.TEAM_SELECTION_ENABLED) {
      this.groupByCB.setEnabled((Boolean)evt.getNewValue());
    }

  }

  @Override
  public void tableChanged(TableModelEvent e) {
    
    SwingUtilities.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        FontMetrics fm = table.getTableHeader().getFontMetrics(
            table.getTableHeader().getFont());
        for(int i = 0; i < table.getColumnCount(); i++){
          int width = fm.stringWidth((String)tcm.getColumn(i).getHeaderValue());
          if(table.getColumnClass(i) == String.class) {
            for(int r = 0; r < table.getRowCount(); r++){            
              width = Math.max(width, fm.stringWidth((String) table.getValueAt(r, i)));
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
  
  private JComponent getButtonComponent(){
    Box hBox = Box.createHorizontalBox();
    hBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    hBox.setAlignmentY(Component.TOP_ALIGNMENT);
    hBox.add(new JButton(pm.getAction(DefaultStatsPModel.EXPORT_ACTION)));
    
    return hBox;
  }

  public JTable getTable() {
    return table;
  }
  
  
}
