package com.easytournament.basic.model.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.easytournament.basic.resources.ResourceManager;
import com.jgoodies.binding.beans.Model;

public class DateChooserDialogModel extends Model {

  private static final long serialVersionUID = -9081018224986048280L;
  public static final String PROPERTY_TIME = "time";
  public static final String PROPERTY_DATE = "date";

  public DateChooserDialogModel(Calendar calendar) {
    super();
    this.calendar = calendar;
  }

  protected Calendar calendar = new GregorianCalendar(
      ResourceManager.getLocale());

  /**
   * @return the date
   */
  public Date getDate() {
    return this.calendar.getTime();
  }

  /**
   * @param date
   *          the date to set
   */
  public void setDate(Date date) {
    Calendar tmpCalendar = new GregorianCalendar(ResourceManager.getLocale());
    tmpCalendar.setTime(date);
    this.calendar.set(tmpCalendar.get(Calendar.YEAR),
        tmpCalendar.get(Calendar.MONTH), tmpCalendar.get(Calendar.DAY_OF_MONTH));
  }

  public void setTime(Date time) {
    Calendar tmpCalendar = new GregorianCalendar(ResourceManager.getLocale());
    tmpCalendar.setTime(time);
    this.calendar.set(Calendar.HOUR, tmpCalendar.get(Calendar.HOUR));
    this.calendar.set(Calendar.MINUTE, tmpCalendar.get(Calendar.MINUTE));
    this.calendar.set(Calendar.SECOND, tmpCalendar.get(Calendar.SECOND));
    this.calendar.set(Calendar.MILLISECOND, tmpCalendar.get(Calendar.MILLISECOND));
    this.calendar.set(Calendar.AM_PM, tmpCalendar.get(Calendar.AM_PM));
  }

  public Date getTime() {
    return this.calendar.getTime();
  }

  /**
   * @return the calendar
   */
  public Calendar getCalendar() {
    return this.calendar;
  }

}
