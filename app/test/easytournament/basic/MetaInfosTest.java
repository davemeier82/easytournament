package easytournament.basic;

import static org.junit.Assert.*;

import org.junit.Test;

import easytournament.basic.MetaInfos;

/**
 * Unit tests for MetaInfos.java
 * @author David Meier
 *
 */
public class MetaInfosTest {

  @Test
  public void testCompareVersionNr() {
    assertEquals(-1, MetaInfos.compareVersionNr("1.2.3", "2.2.3"));
    assertEquals(-1, MetaInfos.compareVersionNr("1.2.3", "1.3.3"));
    assertEquals(-1, MetaInfos.compareVersionNr("1.2.3", "1.2.4"));
    
    assertEquals( 1, MetaInfos.compareVersionNr("2.2.3", "1.2.3"));
    assertEquals( 1, MetaInfos.compareVersionNr("1.3.3", "1.2.3"));
    assertEquals( 1, MetaInfos.compareVersionNr("1.2.4", "1.2.3"));
    
    assertEquals( 0, MetaInfos.compareVersionNr("1.2.3", "1.2.3"));
  }
}
