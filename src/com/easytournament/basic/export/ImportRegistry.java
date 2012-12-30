package com.easytournament.basic.export;

import java.util.HashMap;

public class ImportRegistry {

  protected static HashMap<String,Importable> registry = new HashMap<String,Importable>();

  public static void register(String desc, Importable e) {
    registry.put(desc, e);
  }

  public static void unregister(String desc) {
    registry.remove(desc);
  }

  public static HashMap<String,Importable> getRegistry() {
    return registry;
  }

}
