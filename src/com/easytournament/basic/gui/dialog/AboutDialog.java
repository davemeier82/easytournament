package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.Path;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AboutDialog extends JDialog {

  private static final String LICENSE_TXT = ResourceManager
      .getPath(Path.LICENSE);
  JTextArea jtextarea1 = new JTextArea();
  JLabel logo;
  JLabel website, ccodec, jcalendar, jdom, substance, jgoodies, jgraph, fugue;

  /**
   * Default constructor
   */
  public AboutDialog(Frame f, boolean modal) {
    super(f, ResourceManager.getText(Text.INFO_MENU), modal);
    initializePanel();
    this.pack();
    this.setLocationRelativeTo(f);
    this.setResizable(false);
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

  public JPanel createPanel() {

    LinkMouseListener lml = Desktop.isDesktopSupported()? new LinkMouseListener()
        : null;

    JPanel jpanel1 = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(130PX;DEFAULT):NONE,FILL:10PX:NONE,FILL:MAX(160PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:200PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    jpanel1.setLayout(formlayout1);

    JLabel jlabel1 = new JLabel();
    jlabel1.setText(ResourceManager.getText(Text.VERSION) + ":");
    jpanel1.add(jlabel1, cc.xy(1, 3));

    logo = new JLabel();
    logo.setIcon(ResourceManager.getIcon(Icon.SPLASHSCREEN));

    jpanel1.add(logo, new CellConstraints(1, 1, 3, 1, CellConstraints.CENTER,
        CellConstraints.CENTER));

    JLabel jlabel2 = new JLabel();
    jlabel2.setText(ResourceManager.getText(Text.AUTHOR) + ":");
    jpanel1.add(jlabel2, cc.xy(1, 5));

    JLabel jlabel3 = new JLabel();
    jlabel3.setText(MetaInfos.getVersionNr());
    jpanel1.add(jlabel3, cc.xy(3, 3));

    JLabel jlabel4 = new JLabel();
    jlabel4.setText(MetaInfos.AUTHOR);
    jpanel1.add(jlabel4, cc.xy(3, 5));

    JLabel jlabel5 = new JLabel();
    jlabel5.setText(ResourceManager.getText(Text.WEBSITE) + ":");
    jpanel1.add(jlabel5, cc.xy(1, 7));

    website = new JLabel();
    website.setText("<html><u>" + MetaInfos.APP_WEBSITE + "</u></html>");

    jpanel1.add(website, cc.xy(3, 7));

    JLabel jlabel7 = new JLabel();
    jlabel7.setText(ResourceManager.getText(Text.LICENSE) + ":");
    jpanel1.add(jlabel7, cc.xy(1, 18));

    jtextarea1.setEditable(false);
    JScrollPane jscrollpane1 = new JScrollPane();
    jscrollpane1.setViewportView(jtextarea1);
    jscrollpane1
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    jscrollpane1
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jpanel1.add(jscrollpane1, cc.xywh(1, 19, 3, 1));

    JButton jbutton1 = new JButton(new AbstractAction(
        ResourceManager.getText(Text.CLOSE)) {
      @Override
      public void actionPerformed(ActionEvent e) {
        AboutDialog.this.dispose();
      }
    });
    jpanel1.add(jbutton1, new CellConstraints(3, 21, 1, 1,
        CellConstraints.RIGHT, CellConstraints.CENTER));

    JLabel jlabel8 = new JLabel();
    jlabel8.setText(ResourceManager.getText(Text.USEDLIBS) + ":");
    jpanel1.add(jlabel8, cc.xy(1, 9));

    jcalendar = new JLabel();
    jcalendar.setText("<html><u>JCalendar</u></html>");
    jpanel1.add(jcalendar, cc.xy(3, 10));

    ccodec = new JLabel();
    ccodec.setText("<html><u>" + "Common-Codec");
    jpanel1.add(ccodec, cc.xy(3, 9));

    jdom = new JLabel();
    jdom.setText("<html><u>" + "JDom");
    jpanel1.add(jdom, cc.xy(3, 11));

    substance = new JLabel();
    substance.setText("<html><u>Substance</u></html>");
    jpanel1.add(substance, cc.xy(3, 14));

    jgoodies = new JLabel();
    jgoodies.setText("<html><u>JGoodies</u></html>");
    jpanel1.add(jgoodies, cc.xy(3, 12));

    jgraph = new JLabel();
    jgraph.setText("<html><u>JGraph</u></html>");
    jpanel1.add(jgraph, cc.xy(3, 13));

    JLabel jlabel14 = new JLabel();
    jlabel14.setText(ResourceManager.getText(Text.ICON_SET) + ":");
    jpanel1.add(jlabel14, cc.xy(1, 16));

    fugue = new JLabel();
    fugue.setText("<html><u>Fugue</u></html>");
    jpanel1.add(fugue, cc.xy(3, 16));

    if (lml != null) {
      logo.addMouseListener(lml);
      website.addMouseListener(lml);
      ccodec.addMouseListener(lml);
      jdom.addMouseListener(lml);
      substance.addMouseListener(lml);
      jgoodies.addMouseListener(lml);
      jgraph.addMouseListener(lml);
      fugue.addMouseListener(lml);
    }

    readLicenseFile();

    addFillComponents(jpanel1, new int[] {2, 3}, new int[] {2, 4, 6, 8, 11, 12,
        13, 14, 15, 17, 20, 21});
    jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return jpanel1;
  }

  /**
   * Initializer
   */
  protected void initializePanel() {
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
  }

  protected void readLicenseFile() {
    File file = new File(LICENSE_TXT);
    StringBuffer contents = new StringBuffer();
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file));
      String text = null;

      // repeat until all lines is read
      while ((text = reader.readLine()) != null) {
        contents.append(text).append(System.getProperty("line.separator"));
      }
      jtextarea1.setText(contents.toString());
    }
    catch (Exception e) {
      jtextarea1.setText(ResourceManager.getText(Text.LICENCE_FILE_NOT_FOUND));
    }
    finally {
      try {
        if (reader != null) {
          reader.close();
        }
      }
      catch (IOException e) {
        /* Do nothing */
      }
    }
  }

  class LinkMouseListener extends MouseAdapter {

    Desktop desktop = Desktop.getDesktop();

    @Override
    public void mouseReleased(MouseEvent e) {
      Component c = e.getComponent();
      try {
        if (c == logo || c == website)
          desktop.browse(new URI(MetaInfos.APP_WEBSITE));
        else if (c == ccodec)
          desktop.browse(new URI("http://commons.apache.org/codec/"));
        else if (c == jcalendar)
          desktop.browse(new URI("www.toedter.com/en/jcalendar/"));
        else if (c == jdom)
          desktop.browse(new URI("www.jdom.org"));
        else if (c == jgoodies)
          desktop.browse(new URI("www.jgoodies.com"));
        else if (c == jgraph)
          desktop.browse(new URI("www.jgraph.com"));
        else if (c == substance)
          desktop.browse(new URI("http://insubstantial.posterous.com/"));
        else if (c == fugue)
          desktop.browse(new URI("http://p.yusukekamiyamane.com/"));
      }
      catch (Exception ex) {
        // Do nothing
      }

      super.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      e.getComponent()
          .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      super.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      e.getComponent().setCursor(Cursor.getDefaultCursor());
      super.mouseExited(e);
    }

  }
}
