package easytournament.basic.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import easytournament.basic.model.wizard.WizardModel;

public class WizardDialog extends JDialog implements PropertyChangeListener {

  private static final long serialVersionUID = 6068770781717664991L;
  protected WizardModel pm;
  private JPanel mainpanel;
  private JPanel bPanel;

  public WizardDialog(Dialog owner, WizardModel wizardmodel, boolean modal) {
    super(owner, wizardmodel.getTitel(), modal);
    this.pm = wizardmodel;
    this.init();
    this.setLocationRelativeTo(owner);
    this.pm.addPropertyChangeListener(this);
    this.addWindowListener(new WindowAdapter() {

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e) {
        WizardDialog.this.pm.removePropertyChangeListener(WizardDialog.this);
        super.windowClosed(e);
      }
    });
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  /**
   * Initializes the dialog
   */
  private void init() {
    this.pm.addPropertyChangeListener(this);
    Container cpane = this.getContentPane();
    cpane.setLayout(new BorderLayout());
    this.mainpanel = this.pm.getPanel();
    cpane.add(this.mainpanel, BorderLayout.CENTER);
    cpane.add(getButtonPanel(), BorderLayout.SOUTH);
    this.setSize(400,300);
  }

  private Component getButtonPanel() {
    this.bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    List<Action> buttonActions = this.pm.getButtonActions();
    for (Action a : buttonActions) {
      this.bPanel.add(new JButton(a));
    }
    return this.bPanel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == WizardModel.DISPOSE) {
      this.pm.removePropertyChangeListener(this);
      this.dispose();
    }
    else if (evt.getPropertyName() == WizardModel.BUTTONS_CHANGED) {
      this.bPanel.removeAll();
      List<Action> buttonActions = this.pm.getButtonActions();
      for (Action a : buttonActions) {
        this.bPanel.add(new JButton(a));
      }
    }
    else if (evt.getPropertyName() == WizardModel.PANEL_CHANGED) {
      Container cpane = this.getContentPane();
      cpane.remove(this.mainpanel);
      this.mainpanel = this.pm.getPanel();      
      cpane.add(this.mainpanel, BorderLayout.CENTER);
      this.invalidate();
      this.validate();
    }
    else if (evt.getPropertyName() == WizardModel.TITLE_CHANGED) {
      this.setTitle(this.pm.getTitel());
    }
  }
}
