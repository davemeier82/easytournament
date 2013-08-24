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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.easytournament.basic.model.dialog.DateChooserDialogModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.toedter.calendar.JDateChooser;

public class DateChooserDialog extends JDialog {

  private static final long serialVersionUID = 1291998622613668815L;

  /**
   * The text show in the error dialog
   */
  private String text;

  private DateChooserDialogModel model;

  protected static boolean okPressed = false;

  /**
   * Constructor
   * @param owner
   *          The parent dialog
   * @param title
   *          The dialog title
   * @param text
   *          The text show in the error dialog
   * @param model
   *          The model
   */
  private DateChooserDialog(Frame owner, String title, String text,
      DateChooserDialogModel model) {
    super(owner, title, true);
    this.text = text;
    this.model = model;
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.init();
  }

  /**
   * 
   */
  private void init() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Box box = Box.createHorizontalBox();
    box.add(new JLabel(this.text));
    box.add(Box.createHorizontalStrut(10));

    JDateChooser dateChooser = new JDateChooser();
    ValueModel dateModel = new PropertyAdapter<DateChooserDialogModel>(
        this.model, DateChooserDialogModel.PROPERTY_DATE, true);
    Bindings.bind(dateChooser, "date", dateModel);
    dateChooser.setLocale(ResourceManager.getLocale());
    box.add(dateChooser);
    box.add(Box.createHorizontalStrut(10));
    JFormattedTextField timeField = BasicComponentFactory
        .createFormattedTextField(new PropertyAdapter<DateChooserDialogModel>(
            this.model, DateChooserDialogModel.PROPERTY_TIME, true), DateFormat
            .getTimeInstance(DateFormat.SHORT, ResourceManager.getLocale()));
    box.add(timeField);
    mainPanel.add(box, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnPanel.add(new JButton(new AbstractAction(ResourceManager
        .getText(Text.OK)) {

      private static final long serialVersionUID = -3401656878375781490L;

      @Override
      public void actionPerformed(ActionEvent e) {
        okPressed = true;
        dispose();
      }
    }));
    btnPanel.add(new JButton(new AbstractAction(ResourceManager
        .getText(Text.CANCEL)) {

      private static final long serialVersionUID = -6331087162831723975L;

      @Override
      public void actionPerformed(ActionEvent e) {
        okPressed = false;
        dispose();
      }
    }));
    mainPanel.add(btnPanel, BorderLayout.SOUTH);
    this.getContentPane().add(mainPanel);
  }

  public static boolean showDialog(Frame owner, String title, String text,
      DateChooserDialogModel model) {
    DateChooserDialog dialog = new DateChooserDialog(owner, title, text, model);
    dialog.pack();
    dialog.setLocationRelativeTo(owner);
    dialog.setVisible(true);
    return okPressed;
  }
}
