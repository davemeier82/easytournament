package easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import easytournament.basic.MetaInfos;
import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

public class SplashScreen extends JWindow {
  private static final long serialVersionUID = 1L;
  private static SplashScreen instance;
  private JLabel imageLabel = new JLabel();
  private JPanel southPanel = new JPanel();
  private FlowLayout southPanelFlowLayout = new FlowLayout();
  private JProgressBar progressBar = new JProgressBar();
  private ImageIcon imageIcon;

  /**
   * Generates a Splashscreen with Progressbar
   */
  private SplashScreen() {
    imageIcon = ResourceManager.getIcon(Icon.SPLASHSCREEN);
    try {
      init();
    }
    catch (Exception e) {
      ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
  }

  public static SplashScreen getInstance() {
    if (instance == null) {
      instance = new SplashScreen();
    }
    return instance;
  }

  /**
   * Initialize the Splashscreen (set position, and layout)
   * @throws Exception
   */
  void init() throws Exception {
    JPanel panel = new JPanel(new BorderLayout(0, 5));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    imageLabel.setIcon(imageIcon);
    Box b = Box.createVerticalBox();
    b.add(imageLabel);
    JLabel authorLabel = new JLabel("Author: " + MetaInfos.AUTHOR);
    authorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    JLabel versionLabel = new JLabel("Version: " + MetaInfos.getVersionNr());
    versionLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    b.add(authorLabel);
    b.add(versionLabel);
    b.add(Box.createVerticalStrut(5));
    southPanel.setLayout(southPanelFlowLayout);
    southPanel.add(progressBar, null);

    southPanel.setBackground(Color.WHITE);

    panel.add(b, BorderLayout.NORTH);
    panel.add(southPanel, BorderLayout.SOUTH);
    this.add(panel);
    this.pack();
    this.setLocationRelativeTo(null);
  }

  public void setProgressMax(int maxProgress) {
    progressBar.setMaximum(maxProgress);
  }

  /**
   * Sets the progress of the progressbar
   * @param progress
   *          as int
   */
  public void setProgress(int progress) {
    final int theProgress = progress;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setValue(theProgress);
      }
    });
  }

  /**
   * Sets the progress and a String in the Progressbar
   * @param message
   *          to display in the Progressbar as String
   * @param progress
   *          as int
   */
  public void setProgress(String message, int progress) {
    final int theProgress = progress;
    final String theMessage = message;
    setProgress(progress);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setValue(theProgress);
        setMessage(theMessage);
      }
    });
  }

  /**
   * Show/Hide the Splashscreen
   * @param b
   *          boolean, true for show
   */
  public void setScreenVisible(boolean b) {
    final boolean boo = b;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setVisible(boo);
        if (!boo)
          dispose();
      }
    });
  }

  /**
   * Sets the Message in the Progressbar
   * @param message
   *          to display in the Progressbar as String
   */
  private void setMessage(String message) {
    if (message == null) {
      message = "";
      progressBar.setStringPainted(false);
    }
    else {
      progressBar.setStringPainted(true);
    }
    progressBar.setString(message);
  }
}
