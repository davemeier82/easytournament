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

import com.easytournament.basic.model.dialog.ImportDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.jgoodies.binding.adapter.BasicComponentFactory;


public class ImportDialog extends JDialog implements PropertyChangeListener {

  protected ImportDialogPModel pm;

  /**
   * Default constructor
   */
  public ImportDialog(Frame f, boolean modal, ImportDialogPModel pm) {
    super(f, ResourceManager.getText(Text.SELECT_IMPORT_ITEM), modal);
    this.pm = pm;
    this.pm.addPropertyChangeListener(this);
    initializePanel();
    this.setMinimumSize(new Dimension(450,110));
    this.setLocationRelativeTo(f);
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        ImportDialog.this.pm.removePropertyChangeListener(ImportDialog.this);
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
    JButton ok = new JButton(this.pm.getAction(ImportDialogPModel.OK_ACTION));
    JButton cancel = new JButton(
        this.pm.getAction(ImportDialogPModel.CANCEL_ACTION));
    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  public Component createPanel() {
    Box vBox = Box.createVerticalBox();
    JComboBox<String> exportCombo = BasicComponentFactory.createComboBox(pm
        .getSelectionInList(ImportDialogPModel.IMPORT_LIST));
    vBox.add(exportCombo);
    return vBox;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(ImportDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
      this.dispose();
    }
  }

}
