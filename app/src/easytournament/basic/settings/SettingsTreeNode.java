package easytournament.basic.settings;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

public class SettingsTreeNode extends DefaultMutableTreeNode {

  private Settings p;

  public SettingsTreeNode(String s, Settings pref) {
    super(s);
    this.p = pref;
  }

  public JComponent getPanel() {
    return p.getPanel();
  }

  public Settings getPreferences() {
    return p;
  }

}
