package com.easytournament.basic.export;

import com.easytournament.basic.navigationitem.NavigationItem;

/**
 * Interface for object that can be exported
 * @author David Meier
 * 
 */
public interface Exportable {

  /**
   * Exports the data
   * @param activeModule
   *          True if the corresponding module is active. E.g. if the
   *          SchedulePanel is shown when the user wants to export the schedule
   */
  public void doExport(boolean activeModule);

  /** 
   * @return The module to which the exportable belongs or null of it does not
   *         belong to any module
   */
  public NavigationItem getModule();
}
