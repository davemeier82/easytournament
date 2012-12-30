package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.easytournament.basic.model.dialog.RefreeDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class RefreeDialog extends JDialog implements PropertyChangeListener {

  private RefreeDialogPModel pm;
  private boolean isAssistant;

  public RefreeDialog(Frame f, RefreeDialogPModel pm, boolean modal,
      boolean isAssistant) {
    super(f, ResourceManager.getText(Text.REFREE), modal);
    if (isAssistant)
      this.setTitle(ResourceManager.getText(Text.ASSISTANT));
    this.pm = pm;
    this.isAssistant = isAssistant;
    init();
    this.setLocationRelativeTo(f);
    this.setVisible(true);
  }

  public RefreeDialog(Dialog d, RefreeDialogPModel pm, boolean modal,
      boolean isAssistant) {
    super(d, ResourceManager.getText(Text.REFREE), modal);
    if (isAssistant)
      this.setTitle(ResourceManager.getText(Text.ASSISTANT));
    this.pm = pm;
    this.isAssistant = isAssistant;
    init();
    this.setLocationRelativeTo(d);
    this.setVisible(true);
  }

  private void init() {

    pm.addPropertyChangeListener(this);
    Container cpane = this.getContentPane();
    cpane.setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO),
        new PersonPanel(pm.getPersonPanelPModel()));
    if (this.isAssistant)
      tabbedPane.addTab(ResourceManager.getText(Text.JOB),
          this.getDetailPanel());
    else
      tabbedPane.addTab(ResourceManager.getText(Text.ASSISTANTS),
          new AssistantsTabPanel(pm.getAssistantsTabPanelPModel(), this));

    cpane.add(tabbedPane, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
    this.pack();

    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        RefreeDialog.this.pm.removePropertyChangeListener(RefreeDialog.this);
        super.windowClosed(e);
      }

    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private Component getDetailPanel() {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout formlayout1 = new FormLayout(
        "FILL:90PX:NONE,FILL:DEFAULT:NONE,FILL:200PX:NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    p.setLayout(formlayout1);

    JLabel genderLabel = new JLabel();
    genderLabel.setText(ResourceManager.getText(Text.FUNCTION));
    p.add(genderLabel, cc.xy(1, 2));

    JTextField functionTF = BasicComponentFactory.createTextField(pm
        .getRefreeValueModel(Refree.PROPERTY_FUNCTION));
    p.add(functionTF, cc.xy(3, 2));

    addFillComponents(p, new int[] {1, 2}, new int[] {1, 3});
    return p;
  }

  private Component getButtonPanel() {
    JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton okBtn = new JButton(pm.getAction(RefreeDialogPModel.OK_ACTION));
    JButton cancelBtn = new JButton(
        pm.getAction(RefreeDialogPModel.CANCEL_ACTION));
    bPanel.add(okBtn);
    bPanel.add(cancelBtn);
    return bPanel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(RefreeDialogPModel.DISPOSE)) {
      pm.removePropertyChangeListener(this);
      this.dispose();
    }

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

}
