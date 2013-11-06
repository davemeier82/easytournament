/* Exportable.java - Interface for exportable objects
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

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
