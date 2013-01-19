/* ExportRegistryTest.java
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic.export;

import static org.junit.Assert.*;

import org.junit.Test;

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
      public void doExport() {
        //do nothing       
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
