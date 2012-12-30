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

public class ErrorDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  private static final Dimension SMALL_DIM = new Dimension(600, 250);
  private static final Dimension EXTENDED_DIM = new Dimension(600, 450);
  private String text;
  private Throwable t;
  private JPanel cp;
  private JButton detailButton;
  private boolean detailHidden = false;
  private JScrollPane detailPane;
  private JTextField emailTf = new JTextField();

  /**
   * @param owner
   * @param title
   *          Dialog-Title
   */
  public ErrorDialog(Dialog owner, String title, String text, Throwable t) {
    super(owner, title, true);
    this.text = text;
    this.t = t;
    this.init();
    this.setLocationRelativeTo(owner);
  }

  /**
   * @param owner
   * @param title
   *          Dialog-Title
   */
  public ErrorDialog(Frame owner, String title, String text, Throwable t) {
    super(owner, title, true);
    this.text = text;
    this.t = t;
    this.init();
    this.setLocationRelativeTo(owner);
  }

  /**
   * 
   */
  private void init() {
    cp = new JPanel();
    this.setMinimumSize(SMALL_DIM);
    cp.setLayout(new BorderLayout(10, 10));
    cp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Box box = Box.createVerticalBox();
    if(GeneralSettings.getInstance().hasNewVersion())
    {
      JLabel label = new JLabel(ResourceManager.getText(Text.ERROR_DIALOG_NEW_VERSION));
      label.setFont(label.getFont().deriveFont(Font.BOLD));
      label.setForeground(Color.red);
      box.add(label);
      box.add(Box.createVerticalStrut(10));
    }
    
    JLabel label = new JLabel(text);
    box.add(label);
    box.add(Box.createVerticalStrut(10));
    // create detail button
    detailButton = new JButton(ResourceManager.getText(Text.SHOW_DETAILS));
    detailButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (detailHidden) {
          cp.remove(detailPane);
          detailButton.setText(ResourceManager.getText(Text.DETAILS));
          ErrorDialog.this.setSize(SMALL_DIM);
          detailHidden = false;
        }
        else {
          detailButton.setText(ResourceManager.getText(Text.HIDE_DETAILS));
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
          t.printStackTrace(pw);
          pw.flush();
          sw.flush();
          message = sw.toString();
          // crate detail textarea
          JTextArea ta = new JTextArea();
          ta.setEditable(false);
          ta.setText(message);
          detailPane = new JScrollPane(ta);
          cp.add(detailPane, BorderLayout.CENTER);
          ErrorDialog.this.setSize(EXTENDED_DIM);
          detailHidden = true;
          pw.close();
          try {
            sw.close();
          }
          catch (IOException e1) {
            // do nothing
          }
        }
      }

    });

    box.add(detailButton);
    box.add(Box.createVerticalStrut(10));
    box.add(new JLabel(ResourceManager.getText(Text.SUPPORT_EMAIL)));
    box.add(emailTf);
    cp.add(box, BorderLayout.NORTH);
    this.getContentPane().add(cp);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnPanel.add(new JButton(new SendAction(t)));
    JButton okButton = new JButton(ResourceManager.getText(Text.CLOSE));
    okButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // close dialog
        ErrorDialog.this.dispose();
      }
    });
    btnPanel.add(okButton);
    cp.add(btnPanel, BorderLayout.SOUTH);
    this.setSize(SMALL_DIM);
  }

  class SendAction extends AbstractAction {

    private Throwable t;

    public SendAction(Throwable t) {
      super(ResourceManager.getText(Text.SEND_ERROR_REPORT));
      this.t = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      try {
        String urlStr = "email="
            + URLEncoder.encode(emailTf.getText(), "UTF-8");
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
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();

        urlStr += "&error=" + URLEncoder.encode(sw.toString(), "UTF-8");

        pw.close();
        sw.close();

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

      JOptionPane.showMessageDialog(ErrorDialog.this,
          ResourceManager.getText(Text.THANKS_ERROR_REPORT),
          ResourceManager.getText(Text.THANK_YOU),
          JOptionPane.INFORMATION_MESSAGE);
      
      ErrorDialog.this.dispose();
    }
  }
}
