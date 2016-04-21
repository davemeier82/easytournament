package easytournament.basic.export;

import static org.junit.Assert.*;

import org.junit.Test;

import easytournament.basic.export.ImportRegistry;
import easytournament.basic.export.Importable;

/**
 * Unit tests for ImportRegistry.java
 * @author David Meier
 * 
 */
public class ImportRegistryTest {

  @Test
  public void testGetRegistry() {
    assertEquals(0, ImportRegistry.getRegistry().size());

    Importable e = new Importable() {

      @Override
      public void doImport() {
        // do nothing
      }
    };

    ImportRegistry.register("test", e);

    assertEquals(1, ImportRegistry.getRegistry().size());

    Importable e2 = ImportRegistry.getRegistry().get("test");

    assertEquals(e, e2);

    ImportRegistry.unregister("test");

    assertEquals(0, ImportRegistry.getRegistry().size());
  }
}
