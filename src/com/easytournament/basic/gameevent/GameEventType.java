package com.easytournament.basic.gameevent;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;


public enum GameEventType {
  GOAL, PENALTY, OTHER;
  
  public static String getName(GameEventType et){
    switch(et){
      case GOAL:
        return ResourceManager.getText(Text.GOAL);
      case PENALTY:
        return ResourceManager.getText(Text.PENALTY);
      case OTHER:
        return ResourceManager.getText(Text.OTHER);
    }
    return null;
  }

  @Override
  public String toString() {
    return getName(this);
  }
  
  
}
