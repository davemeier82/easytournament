package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.SportDialog;
import com.easytournament.basic.model.dialog.SportDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.filechooser.ImageFilter;
import com.easytournament.basic.util.filechooser.ImagePreview;
import com.easytournament.basic.util.filechooser.Utils;
import com.easytournament.basic.valueholder.Sport;
import com.easytournament.basic.valueholder.Tournament;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

public class TPanelPModel extends Model implements PropertyChangeListener {

  public static final int NEW_SPORT_ACTION = 0;
  public static final int EDIT_SPORT_ACTION = 1;
  public static final int LOGO_ACTION = 3;
  public static final int RESET_ICON_ACTION = 4;

  public static final String PROPERTY_BEGIN_TIME = "beginTime";
  public static final String PROPERTY_END_TIME = "endTime";
  public static final String PROPERTY_BEGIN_DATE = "beginDate";
  public static final String PROPERTY_END_DATE = "endDate";

  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected DateFormat dateFormatter = DateFormat.getDateInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  protected DateFormat timeFormatter = DateFormat.getTimeInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  protected Calendar calendar = new GregorianCalendar(
      ResourceManager.getLocale());
  protected LogoAction logoAction;
  
  public TPanelPModel() {
    logoAction = new LogoAction(null, t.getLogo());
    t.addPropertyChangeListener(this);
  }

  public ValueModel getTournamentValueModel(String propertyName) {
    if (propertyName.equals(Tournament.PROPERTY_SPORT)) {
      return new PropertyAdapter<TPanelPModel>(this, "sport", true);
    }
    return new PropertyAdapter<Tournament>(t, propertyName, true);
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_SPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.NEW)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            new SportDialog(Organizer.getInstance().getMainFrame(), true,
                new SportDialogPModel());

          }
        };
      case EDIT_SPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EDIT)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            new SportDialog(Organizer.getInstance().getMainFrame(), true,
                new SportDialogPModel(t.getSport()));

          }
        };
      case LOGO_ACTION:
        return logoAction;
      case RESET_ICON_ACTION:
        return new AbstractAction(
            ResourceManager.getText(Text.RESET_LOGO),
            ResourceManager
                .getIcon(com.easytournament.basic.resources.Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            putValue(Action.SMALL_ICON, null);
            putValue(Action.SHORT_DESCRIPTION,
                ResourceManager.getText(Text.SELECT_LOGO));
            t.setLogo(null);
          }
        };
    }
    return null;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == this.t) {
      if (evt.getPropertyName() == Tournament.PROPERTY_BEGIN) {
        firePropertyChange(PROPERTY_BEGIN_TIME, null, t.getBegin().getTime());
        firePropertyChange(PROPERTY_BEGIN_DATE, null, t.getBegin().getTime());
      }
      else if (evt.getPropertyName() == Tournament.PROPERTY_END) {
        firePropertyChange(PROPERTY_END_TIME, null, t.getEnd().getTime());
        firePropertyChange(PROPERTY_END_DATE, null, t.getEnd().getTime());
      }
      else if (evt.getPropertyName() == Tournament.PROPERTY_LOGO) {
        logoAction.putValue(Action.SMALL_ICON, t.getLogo());
      }
    }
  }

  public void setBeginTime(Date time) {
    calendar.setTime(time);
    t.setBeginTime(calendar);
  }

  public Date getBeginTime() {
    return t.getBegin().getTime();
  }

  public void setEndTime(Date time) {
    calendar.setTime(time);
    t.setEndTime(calendar);
  }

  public Date getEndTime() {
    return t.getEnd().getTime();
  }

  class LogoAction extends AbstractAction {

    private static final long serialVersionUID = -7033290522646891650L;

    public LogoAction(String name, Icon icon) {
      super(name, icon);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      JFileChooser fc = new JFileChooser();

      fc.setFileFilter(new ImageFilter());
      fc.setAcceptAllFileFilterUsed(false);

      fc.setAccessory(new ImagePreview(fc));

      int returnVal = fc.showDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.SELECT_LOGO));

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        if (file != null) {
          ImageIcon icon = new ImageIcon(file.getAbsolutePath());

          double scale = Math.min(250.0 / icon.getIconWidth(),
              250.0 / icon.getIconHeight()); // TODO do not hardcode size

          ImageIcon scaled = Utils.getScaledImage(icon, scale);
          putValue(Action.SMALL_ICON, scaled);
          putValue(Action.SHORT_DESCRIPTION, null);
          t.setLogo(scaled);
        }
      }
    }

  }

  public ArrayListModel<Sport> getSports() {
    return new ArrayListModel<Sport>(Organizer.getInstance().getSports()
        .values());
  }

  public void setSport(Sport s) {
    if (t.getSport().isEdited()) {
      int answer = JOptionPane.showConfirmDialog(Organizer.getInstance()
          .getMainFrame(),
          ResourceManager.getText(Text.CHANGE_SPORT1) + t.getSport().getName()
              + ResourceManager.getText(Text.CHANGE_SPORT2), ResourceManager
              .getText(Text.CHANGE_SPORT), JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (answer == JOptionPane.OK_OPTION)
        t.setSport(s);
    }
    else {
      t.setSport(s);
    }
  }

  public Sport getSport() {
    return t.getSport();
  }

  public Date getBeginDate() {
    return t.getBegin().getTime();
  }

  public void setBeginDate(Date beginDate) {
    calendar.setTime(beginDate);
    t.getBegin().set(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
  }

  public Date getEndDate() {
    return t.getEnd().getTime();
  }

  public void setEndDate(Date endDate) {
    calendar.setTime(endDate);
    t.getEnd().set(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
  }
  
  
}
