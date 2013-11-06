/* ErrorDialog.java - Dialog to show an error message to the user
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.settings.GeneralSettings;

/**
 * Dialog that shows an error message to the user.
 * It provides the possibility to send the error to
 * the developer
 * @author David Meier
 *
 */
public class ErrorDialog extends JDialog {

  private static final long serialVersionUID = -3888328940736943085L;
  /**
   * Size of the dialog if no details are shown
   */
  static final Dimension SMALL_DIM = new Dimension(600, 250);
  /**
   * Size of the dialog with details
   */
  static final Dimension EXTENDED_DIM = new Dimension(600, 450);
  /**
   * The text show in the error dialog
   */
  private String text;
  /**
   * The error object
   */
  private Throwable throwable;
  /**
   * The main panel of the dialog
   */
  private JPanel mainPanel;
  /**
   * The button to show or hide the details
   */
  private JButton detailButton;
  /**
   * True if the details are hidden
   */
  private boolean detailHidden = false;
  /**
   * The scroll pain that contains the detailed error message
   */
  private JScrollPane detailPane;
  /**
   * The text field for the optional e-mail address of the user
   */
  private JTextField emailTf = new JTextField();

  /**
   * Constructor
   * @param owner The parent dialog
   * @param title The dialog title
   * @param text The text show in the error dialog
   * @param t The error object
   */
  public ErrorDialog(Dialog owner, String title, String text, Throwable t) {
    super(owner, title, true);
    this.text = text;
    this.throwable = t;
    this.init();
    this.setLocationRelativeTo(owner);
  }

  /**
   * Constructor
   * @param owner The parent dialog
   * @param title The dialog title
   * @param text The text show in the error dialog
   * @param t The error object
   */
  public ErrorDialog(Frame owner, String title, String text, Throwable t) {
    super(owner, title, true);
    this.text = text;
    this.throwable = t;
    this.init();
    this.setLocationRelativeTo(owner);
  }

  /**
   * 
   */
  private void init() {
    this.mainPanel = new JPanel();
    this.setMinimumSize(SMALL_DIM);
    this.mainPanel.setLayout(new BorderLayout(10, 10));
    this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Box box = Box.createVerticalBox();
    
    // inform the user if there is a new version available
    if(GeneralSettings.getInstance().hasNewVersion())
    {
      JLabel label = new JLabel(ResourceManager.getText(Text.ERROR_DIALOG_NEW_VERSION));
      label.setFont(label.getFont().deriveFont(Font.BOLD));
      label.setForeground(Color.red);
      box.add(label);
      box.add(Box.createVerticalStrut(10));
    }
    
    JLabel label = new JLabel(this.text);
    box.add(label);
    box.add(Box.createVerticalStrut(10));
    // create detail button
    this.detailButton = new JButton(ResourceManager.getText(Text.SHOW_DETAILS));
    this.detailButton.addActionListener(new ActionListener() {

      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        JPanel contentPane = getMainPanel();
        if (isDetailHidden()) {
          contentPane.remove(getDetailPane());
          setDetailButtonText(ResourceManager.getText(Text.DETAILS));
          ErrorDialog.this.setSize(SMALL_DIM);
          setDetailHidden(false);
        }
        else {
          if(getDetailPane() == null) {
            createDetailPane();
          }
          contentPane.add(getDetailPane(), BorderLayout.CENTER);
          ErrorDialog.this.setSize(EXTENDED_DIM);
          setDetailHidden(true);
        }
      }
    });

    box.add(this.detailButton);
    box.add(Box.createVerticalStrut(10));
    box.add(new JLabel(ResourceManager.getText(Text.SUPPORT_EMAIL)));
    box.add(this.emailTf);
    this.mainPanel.add(box, BorderLayout.NORTH);
    this.getContentPane().add(this.mainPanel);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnPanel.add(new JButton(new SendAction(this.throwable)));
    JButton closeButton = new JButton(ResourceManager.getText(Text.CLOSE));
    closeButton.addActionListener(new ActionListener() {

      /* (non-Javadoc)
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // close dialog
        ErrorDialog.this.dispose();
      }
    });
    btnPanel.add(closeButton);
    this.mainPanel.add(btnPanel, BorderLayout.SOUTH);
    this.setSize(SMALL_DIM);
  }

  /**
   * Action to send the error message to the developer
   * @author David Meier
   *
   */
  class SendAction extends AbstractAction {

    private static final long serialVersionUID = -8583011695449954032L;
    /**
     * The error object
     */
    private Throwable t;

    /**
     * @param t The error object
     */
    public SendAction(Throwable t) {
      super(ResourceManager.getText(Text.SEND_ERROR_REPORT));
      this.t = t;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

      // setting up the error message url
      try {
        String urlStr = "email="
            + URLEncoder.encode(getEmailTf().getText(), "UTF-8");
        urlStr += "&version="
            + URLEncoder.encode(MetaInfos.getVersionNr(), "UTF-8");
        urlStr += "&lang="
            + URLEncoder
                .encode(ResourceManager.getLocale().toString(), "UTF-8"); // TODO
        // toLanguageTag()
        // if java version
        // 7
        urlStr += "&os_name="
            + URLEncoder.encode(System.getProperty("os.name"), "UTF-8");
        urlStr += "&os_arch="
            + URLEncoder.encode(System.getProperty("os.arch"), "UTF-8");
        urlStr += "&os_vers="
            + URLEncoder.encode(System.getProperty("os.version"), "UTF-8");
        urlStr += "&java_vendor="
            + URLEncoder.encode(System.getProperty("java.vendor"), "UTF-8");
        urlStr += "&java_version="
            + URLEncoder.encode(System.getProperty("java.version"), "UTF-8");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        this.t.printStackTrace(pw);
        pw.flush();
        sw.flush();

        urlStr += "&error=" + URLEncoder.encode(sw.toString(), "UTF-8");

        pw.close();
        sw.close();

        // connect to the url
        URL errorUrl = new URL("http://easy-tournament.com/logerror.php"); 
        HttpURLConnection connection = (HttpURLConnection) errorUrl.openConnection();           
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlStr.getBytes().length));
        connection.setUseCaches (false);

        // send message
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(urlStr);
        wr.flush();
        wr.close();
        connection.disconnect();
        
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(
        connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
          sb.append(line);
        }
        rd.close();
        //System.out.println(sb.toString());
      }
      catch (Exception ex) {
        ErrorLogger.getLogger().throwing("ErrorDialog", "SendAction", ex);
        ex.printStackTrace();
      }

      // show thank you message
      JOptionPane.showMessageDialog(ErrorDialog.this,
          ResourceManager.getText(Text.THANKS_ERROR_REPORT),
          ResourceManager.getText(Text.THANK_YOU),
          JOptionPane.INFORMATION_MESSAGE);
      
      ErrorDialog.this.dispose();
    }
  }

  /**
   * @return the main panel
   */
  public JPanel getMainPanel() {
    return this.mainPanel;
  }

  /**
   * @return True if the details are hidden
   */
  public boolean isDetailHidden() {
    return this.detailHidden;
  }

  /**
   * @param detailHidden True to hide the details
   */
  public void setDetailHidden(boolean detailHidden) {
    this.detailHidden = detailHidden;
  }

  /**
   * @return the error object
   */
  public Throwable getThrowable() {
    return this.throwable;
  }

  /**
   * @return the user email address text field
   */
  public JTextField getEmailTf() {
    return this.emailTf;
  }

  /**
   * Sets the text that is displayed on the detail button
   * @param The text of the detail button
   */
  public void setDetailButtonText(String text)
  {
    this.detailButton.setText(text);
  }
  
  /**
   * Creates the detail message
   */
  public void createDetailPane()
  {
    setDetailButtonText(ResourceManager.getText(Text.HIDE_DETAILS));
    // create detail string
    String message = "";
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    pw.print("OS: ");
    pw.println(System.getProperty("os.name"));
    pw.print("OS Architecture: ");
    pw.println(System.getProperty("os.arch"));
    pw.print("OS Version: ");
    pw.println(System.getProperty("os.version"));
    pw.print("Java Vendor: ");
    pw.println(System.getProperty("java.vendor"));
    pw.print("Java Version: ");
    pw.println(System.getProperty("java.version"));
    pw.print("Version: ");
    pw.println(MetaInfos.getVersionNr());
    pw.print("Language: ");
    pw.println(ResourceManager.getLocale().toString()); // TODO
                                                        // toLanguageTag()
                                                        // if java version
                                                        // 7
    getThrowable().printStackTrace(pw);
    pw.flush();
    sw.flush();
    message = sw.toString();
    // crate detail textarea
    JTextArea ta = new JTextArea();
    ta.setEditable(false);
    ta.setText(message);
    this.detailPane = new JScrollPane(ta);
    pw.close();
    try {
      sw.close();
    }
    catch (IOException e1) {
      // do nothing
    }
  }

  /**
   * @return the scroll pane containing the detailed error message
   */
  public JScrollPane getDetailPane() {
    return this.detailPane;
  }  
}
