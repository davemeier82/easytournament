package easytournament.basic.export;

import java.util.HashMap;

/**
 * Class to register objects that can be exported via the 
 * export dialog
 * @author David Meier
 *
 */
public class ExportRegistry {

  /**
   * Map of the exportable objects
   */
  protected static HashMap<String,Exportable> registry = new HashMap<String,Exportable>();


  /**
   * Register a new exportable object
   * @param desciption label which shows up in the export dialog
   * @param exportable the exportable object
   */
  public static void register(String desciption, Exportable exportable) {
    registry.put(desciption, exportable);
  }

  /**
   * Removes an exportable object from the registry
   * @param desciption label of the exportable
   */
  public static void unregister(String desciption) {
    registry.remove(desciption);
  }

  /**
   * Returns the map containing the exportable objects
   * @return the map containing the exportable objects
   */
  public static HashMap<String,Exportable> getRegistry() {
    return registry;
  }

}
