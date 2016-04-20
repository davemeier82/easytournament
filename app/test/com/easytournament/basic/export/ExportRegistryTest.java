package com.easytournament.basic.export;

import static org.junit.Assert.*;

import org.junit.Test;

import com.easytournament.basic.navigationitem.NavigationItem;

/**
 * Unit tests for ExportRegistry.java
 * @author David Meier
 *
 */
public class ExportRegistryTest {

  @Test
  public void testGetRegistry() {
    
    assertEquals(0, ExportRegistry.getRegistry().size());
    
    Exportable e = new Exportable() {
      
      @Override
      public void doExport(boolean activeModule) {
        //do nothing       
      }

      @Override
      public NavigationItem getModule() {
        return null;
      }
    };
    
    ExportRegistry.register("test", e);
    
    assertEquals(1, ExportRegistry.getRegistry().size());
    
    Exportable e2 = ExportRegistry.getRegistry().get("test");
    
    assertEquals(e, e2);
    
    ExportRegistry.unregister("test");
    
    assertEquals(0, ExportRegistry.getRegistry().size());    
  }
}
