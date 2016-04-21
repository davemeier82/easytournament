package easytournament.basic.model;

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

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.SportDialog;
import easytournament.basic.model.dialog.SportDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.filechooser.ImageFilter;
import easytournament.basic.util.filechooser.ImagePreview;
import easytournament.basic.util.filechooser.Utils;
import easytournament.basic.valueholder.Sport;
import easytournament.basic.valueholder.Tournament;

public class TPanelPModel extends Model implements PropertyChangeListener {

  private static final long serialVersionUID = -4282471524543952098L;
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
    this.logoAction = new LogoAction(null, this.t.getLogo());
    this.t.addPropertyChangeListener(this);
  }

  public ValueModel getTournamentValueModel(String propertyName) {
    if (propertyName.equals(Tournament.PROPERTY_SPORT)) {
      return new PropertyAdapter<TPanelPModel>(this, "sport", true);
    }
    return new PropertyAdapter<Tournament>(this.t, propertyName, true);
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_SPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.NEW)) {

          private static final long serialVersionUID = -2859408984600504000L;

          @SuppressWarnings("unused")
          @Override
          public void actionPerformed(ActionEvent arg0) {
            new SportDialog(Organizer.getInstance().getMainFrame(), true,
                new SportDialogPModel());

          }
        };
      case EDIT_SPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EDIT)) {

          private static final long serialVersionUID = 1227629601797395521L;

          @SuppressWarnings("unused")
          @Override
          public void actionPerformed(ActionEvent arg0) {
            new SportDialog(Organizer.getInstance().getMainFrame(), true,
                new SportDialogPModel(TPanelPModel.this.t.getSport()));

          }
        };
      case LOGO_ACTION:
        return this.logoAction;
      case RESET_ICON_ACTION:
        return new AbstractAction(
            ResourceManager.getText(Text.RESET_LOGO),
            ResourceManager
                .getIcon(easytournament.basic.resources.Icon.DELETE_ICON_SMALL)) {

          private static final long serialVersionUID = 6808297329369860383L;

          @Override
          public void actionPerformed(ActionEvent e) {
            putValue(Action.SMALL_ICON, null);
            putValue(Action.SHORT_DESCRIPTION,
                ResourceManager.getText(Text.SELECT_LOGO));
            TPanelPModel.this.t.setLogo(null);
          }
        };
      default:
        break;
    }
    return null;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == this.t) {
      if (evt.getPropertyName() == Tournament.PROPERTY_BEGIN) {
        firePropertyChange(PROPERTY_BEGIN_TIME, null, this.t.getBegin()
            .getTime());
        firePropertyChange(PROPERTY_BEGIN_DATE, null, this.t.getBegin()
            .getTime());
      }
      else if (evt.getPropertyName() == Tournament.PROPERTY_END) {
        firePropertyChange(PROPERTY_END_TIME, null, this.t.getEnd().getTime());
        firePropertyChange(PROPERTY_END_DATE, null, this.t.getEnd().getTime());
      }
      else if (evt.getPropertyName() == Tournament.PROPERTY_LOGO) {
        this.logoAction.putValue(Action.SMALL_ICON, this.t.getLogo());
      }
    }
  }

  public void setBeginTime(Date time) {
    this.calendar.setTime(time);
    this.t.setBeginTime(this.calendar);
  }

  public Date getBeginTime() {
    return this.t.getBegin().getTime();
  }

  public void setEndTime(Date time) {
    this.calendar.setTime(time);
    this.t.setEndTime(this.calendar);
  }

  public Date getEndTime() {
    return this.t.getEnd().getTime();
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
          TPanelPModel.this.t.setLogo(scaled);
        }
      }
    }
  }

  public ArrayListModel<Sport> getSports() {
    return new ArrayListModel<Sport>(Organizer.getInstance().getSports()
        .values());
  }

  public void setSport(Sport s) {
    if (this.t.getSport().isEdited()) {
      int answer = JOptionPane.showConfirmDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.CHANGE_SPORT1)
              + this.t.getSport().getName()
              + ResourceManager.getText(Text.CHANGE_SPORT2), ResourceManager
              .getText(Text.CHANGE_SPORT), JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (answer == JOptionPane.OK_OPTION)
        this.t.setSport(s);
    }
    else {
      this.t.setSport(s);
    }
  }

  public Sport getSport() {
    return this.t.getSport();
  }

  public Date getBeginDate() {
    return this.t.getBegin().getTime();
  }

  public void setBeginDate(Date beginDate) {
    this.calendar.setTime(beginDate);
    this.t.getBegin().set(this.calendar.get(Calendar.YEAR),
        this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH));
  }

  public Date getEndDate() {
    return this.t.getEnd().getTime();
  }

  public void setEndDate(Date endDate) {
    this.calendar.setTime(endDate);
    this.t.getEnd().set(this.calendar.get(Calendar.YEAR),
        this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH));
  }

}
