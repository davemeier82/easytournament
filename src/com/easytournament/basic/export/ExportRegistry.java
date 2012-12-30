package com.easytournament.basic.export;

import java.util.HashMap;

public class ExportRegistry {

  protected static HashMap<String,Exportable> registry = new HashMap<String,Exportable>();


  public static void register(String desc, Exportable e) {
    registry.put(desc, e);
  }

  public static void unregister(String desc) {
    registry.remove(desc);
  }

  public static HashMap<String,Exportable> getRegistry() {
    return registry;
  }

}
