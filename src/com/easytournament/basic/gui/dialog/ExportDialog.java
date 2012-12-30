package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.easytournament.basic.model.dialog.ExportDialogPModel;
import com.easytournament.basic.model.dialog.GEventDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.jgoodies.binding.adapter.BasicComponentFactory;


public class ExportDialog extends JDialog implements PropertyChangeListener {

  protected ExportDialogPModel pm;

  /**
   * Default constructor
   */
  public ExportDialog(Frame f, boolean modal, ExportDialogPModel pm) {
    super(f, ResourceManager.getText(Text.SELECT_EXPORT_ITEM), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setMinimumSize(new Dimension(450,110));
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        ExportDialog.this.pm.removePropertyChangeListener(ExportDialog.this);
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
    JButton ok = new JButton(this.pm.getAction(ExportDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(ExportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  public Component createPanel() {
    Box vBox = Box.createVerticalBox();
    JComboBox<String> exportCombo = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(ExportDialogPModel.EXPORT_LIST));
    vBox.add(exportCombo);
    return vBox;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GEventDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

}
