package easytournament.basic.export;

import java.util.HashMap;

/**
 * Class to register objects that can be imported via the 
 * import dialog
 * @author David Meier
 *
 */
public class ImportRegistry {

  /**
   * Map of the importable objects
   */
  protected static HashMap<String,Importable> registry = new HashMap<String,Importable>();

  /**
   * Register a new importable object
   * @param description label which shows up in the import dialog
   * @param importable the importable object
   */
  public static void register(String description, Importable importable) {
    registry.put(description, importable);
  }

  /**
   * Removes an importable object from the registry
   * @param desciption label of the importable
   */
  public static void unregister(String desciption) {
    registry.remove(desciption);
  }

  /**
   * Returns the map containing the importable objects
   * @return the map containing the importable objects
   */
  public static HashMap<String,Importable> getRegistry() {
    return registry;
  }
}
