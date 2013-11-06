/* GameEventType.java - Game event type for Statistics
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.gameevent;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;

/**
 * Game events are the main filter types of statistics
 * @author David Meier
 * 
 */
public enum GameEventType {
  GOAL, PENALTY, OTHER;

  /**
   * Returns the translated name of the game event type
   * @param eventType the event type to get the name for
   * @return The translated name of the game event type
   */
  public static String getName(GameEventType eventType) {
    switch (eventType) {
      case GOAL:
        return ResourceManager.getText(Text.GOAL);
      case PENALTY:
        return ResourceManager.getText(Text.PENALTY);
      case OTHER:
        return ResourceManager.getText(Text.OTHER);
      default:
        break;
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return getName(this);
  }

}
