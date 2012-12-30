package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

import com.easytournament.basic.gui.tablecellrenderer.CheckboxCellRenderer;
import com.easytournament.basic.model.dialog.GEventDialogPModel;
import com.easytournament.basic.model.dialog.ImportListDialogPModel;


public class ImportListDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 4775688030231693269L;
  protected ImportListDialogPModel<?> pm;
  protected JTextField textTF;
  protected JCheckBox secondaryChB;

  /**
   * Default constructor
   */
  public ImportListDialog(Frame f,  String title, boolean modal,ImportListDialogPModel<?> pm) {
    super(f, title, modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setSize(800,600);
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        ImportListDialog.this.pm.removePropertyChangeListener(ImportListDialog.this);
        super.windowClosed(e);
      }
      
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
    add(this.getButtonPanel(), BorderLayout.SOUTH);
  }
  
  private Component getButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton ok = new JButton(this.pm.getAction(GEventDialogPModel.OK_ACTION)); 
    JButton cancel = new JButton(this.pm.getAction(GEventDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  public JPanel createPanel() {
    JPanel p = new JPanel(new BorderLayout());
    final JTable table = new JTable(pm.getTableModel());
    TableColumnModel tcm = table.getColumnModel();
    tcm.getColumn(0).setCellRenderer(new CheckboxCellRenderer());
    tcm.getColumn(0).setMaxWidth(30);
    
    table.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        pm.toggleSelected(row);
      }

    });
    
    JScrollPane pane = new JScrollPane(table);
    p.add(pane);

    return p;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)){
      pm.removePropertyChangeListener(this);
      this.dispose();
    }
    
  }

}
