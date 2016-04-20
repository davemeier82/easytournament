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

/**
 * Dialog of application and license information
 * @author David Meier
 * 
 */
public class AboutDialog extends JDialog {

  private static final long serialVersionUID = 25300622888927060L;

  /**
   * UI elements
   */
  JTextArea licenseTextfield = new JTextArea();
  JLabel logo, website, ccodec, jcalendar, jdom, substance, jgoodies, jgraph,
      fugue;

  /**
   * Default constructor
   */
  public AboutDialog(Frame f, boolean modal) {
    super(f, ResourceManager.getText(Text.INFO_MENU), modal);
    setLayout(new BorderLayout());
    add(createPanel(), BorderLayout.CENTER);
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

  /**
   * Creates the main panel of the window
   * @return The main panel
   */
  public JPanel createPanel() {

    LinkMouseListener lml = Desktop.isDesktopSupported()? new LinkMouseListener()
        : null;

    JPanel mainpanel = new JPanel();
    FormLayout formlayout1 = new FormLayout(
        "FILL:MAX(130PX;DEFAULT):NONE,FILL:10PX:NONE,FILL:MAX(160PX;DEFAULT):NONE",
        "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:200PX:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
    CellConstraints cc = new CellConstraints();
    mainpanel.setLayout(formlayout1);

    // Version text
    JLabel versionLabel = new JLabel();
    versionLabel.setText(ResourceManager.getText(Text.VERSION) + ":");
    mainpanel.add(versionLabel, cc.xy(1, 3));
    
    JLabel versionNrLabel = new JLabel();
    versionNrLabel.setText(MetaInfos.getVersionNr());
    mainpanel.add(versionNrLabel, cc.xy(3, 3));

    // Logo image
    this.logo = new JLabel();
    this.logo.setIcon(ResourceManager.getIcon(Icon.SPLASHSCREEN));

    mainpanel.add(this.logo, new CellConstraints(1, 1, 3, 1,
        CellConstraints.CENTER, CellConstraints.CENTER));

    // Author 
    JLabel authorLabel = new JLabel();
    authorLabel.setText(ResourceManager.getText(Text.AUTHOR) + ":");
    mainpanel.add(authorLabel, cc.xy(1, 5));

    JLabel authorNameLabel = new JLabel();
    authorNameLabel.setText(MetaInfos.AUTHOR);
    mainpanel.add(authorNameLabel, cc.xy(3, 5));
    
    // Website
    JLabel websiteLabel = new JLabel();
    websiteLabel.setText(ResourceManager.getText(Text.WEBSITE) + ":");
    mainpanel.add(websiteLabel, cc.xy(1, 7));

    this.website = new JLabel();
    this.website.setText("<html><u>" + MetaInfos.APP_WEBSITE + "</u></html>");

    mainpanel.add(this.website, cc.xy(3, 7));

    // License
    JLabel licenseLabel = new JLabel();
    licenseLabel.setText(ResourceManager.getText(Text.LICENSE) + ":");
    mainpanel.add(licenseLabel, cc.xy(1, 18));

    this.licenseTextfield.setEditable(false);
    JScrollPane licenseScrollPane = new JScrollPane();
    licenseScrollPane.setViewportView(this.licenseTextfield);
    licenseScrollPane
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    licenseScrollPane
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    mainpanel.add(licenseScrollPane, cc.xywh(1, 19, 3, 1));
    
    readLicenseFile();

    // Libraries
    JLabel libraryLabel = new JLabel();
    libraryLabel.setText(ResourceManager.getText(Text.USEDLIBS) + ":");
    mainpanel.add(libraryLabel, cc.xy(1, 9));

    this.jcalendar = new JLabel();
    this.jcalendar.setText("<html><u>JCalendar</u></html>");
    mainpanel.add(this.jcalendar, cc.xy(3, 10));

    this.ccodec = new JLabel();
    this.ccodec.setText("<html><u>" + "Common-Codec");
    mainpanel.add(this.ccodec, cc.xy(3, 9));

    this.jdom = new JLabel();
    this.jdom.setText("<html><u>" + "JDom");
    mainpanel.add(this.jdom, cc.xy(3, 11));

    this.substance = new JLabel();
    this.substance.setText("<html><u>Substance</u></html>");
    mainpanel.add(this.substance, cc.xy(3, 14));

    this.jgoodies = new JLabel();
    this.jgoodies.setText("<html><u>JGoodies</u></html>");
    mainpanel.add(this.jgoodies, cc.xy(3, 12));

    this.jgraph = new JLabel();
    this.jgraph.setText("<html><u>JGraph</u></html>");
    mainpanel.add(this.jgraph, cc.xy(3, 13));

    JLabel jlabel14 = new JLabel();
    jlabel14.setText(ResourceManager.getText(Text.ICON_SET) + ":");
    mainpanel.add(jlabel14, cc.xy(1, 16));

    this.fugue = new JLabel();
    this.fugue.setText("<html><u>Fugue</u></html>");
    mainpanel.add(this.fugue, cc.xy(3, 16));

    // only add mouselistener if links are supported 
    if (lml != null) {
      this.logo.addMouseListener(lml);
      this.website.addMouseListener(lml);
      this.jcalendar.addMouseListener(lml);
      this.ccodec.addMouseListener(lml);
      this.jdom.addMouseListener(lml);
      //this.substance.addMouseListener(lml); page doesn't exist anymore
      this.jgoodies.addMouseListener(lml);
      this.jgraph.addMouseListener(lml);
      this.fugue.addMouseListener(lml);
    }

    JButton closeButton = new JButton(
     /** 
     * Button action to close the window
     * @author David Meier
     * 
     */
    new AbstractAction(ResourceManager.getText(Text.CLOSE)) {

      private static final long serialVersionUID = -2836468451019152571L;

      /*
       * (non-Javadoc)
       * 
       * @see
       * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
       * )
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        AboutDialog.this.dispose();
      }
    });
    mainpanel.add(closeButton, new CellConstraints(3, 21, 1, 1,
        CellConstraints.RIGHT, CellConstraints.CENTER));

    addFillComponents(mainpanel, new int[] {2, 3}, new int[] {2, 4, 6, 8, 11, 12,
        13, 14, 15, 17, 20, 21});
    mainpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return mainpanel;
  }


  /**
   * Reads the license file and adds the text to the license text field
   */
  protected void readLicenseFile() {
    File file = new File(ResourceManager.getPath(Path.LICENSE));
    StringBuffer contents = new StringBuffer();
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file));
      String text = null;

      // repeat until all lines are read
      while ((text = reader.readLine()) != null) {
        contents.append(text).append(System.getProperty("line.separator"));
      }
      this.licenseTextfield.setText(contents.toString());
    }
    catch (Exception e) {
      // reading of the license file failed
      this.licenseTextfield.setText(ResourceManager
          .getText(Text.LICENCE_FILE_NOT_FOUND));
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

  /**
   * @author David Meier
   * 
   */
  class LinkMouseListener extends MouseAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      Desktop desktop = Desktop.getDesktop();
      Component c = e.getComponent();
      try {
        if (c == AboutDialog.this.logo || c == AboutDialog.this.website)
          desktop.browse(new URI("http://"+MetaInfos.APP_WEBSITE));
        else if (c == AboutDialog.this.ccodec)
          desktop.browse(new URI("http://commons.apache.org/codec/"));
        else if (c == AboutDialog.this.jcalendar)
          desktop.browse(new URI("http://www.toedter.com/en/jcalendar/"));
        else if (c == AboutDialog.this.jdom)
          desktop.browse(new URI("http://www.jdom.org"));
        else if (c == AboutDialog.this.jgoodies)
          desktop.browse(new URI("http://www.jgoodies.com"));
        else if (c == AboutDialog.this.jgraph)
          desktop.browse(new URI("http://www.jgraph.com"));
        else if (c == AboutDialog.this.substance)
          desktop.browse(new URI("http://http://insubstantial.posterous.com/"));
        else if (c == AboutDialog.this.fugue)
          desktop.browse(new URI("http://p.yusukekamiyamane.com/"));
      }
      catch (Exception ex) {
        // Do nothing
      }

      super.mouseReleased(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
      // change cursor to hand cursor when mouse is moved over a link
      e.getComponent()
          .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      super.mouseEntered(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
      // reset the mouse cursor back to the default one when leaving a link
      e.getComponent().setCursor(Cursor.getDefaultCursor());
      super.mouseExited(e);
    }
  }
}
