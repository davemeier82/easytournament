package com.easytournament.basic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Tournament;

/**
 * Unit Tests for Organizer.java
 * @author David Meier
 *
 */
public class OrganizerTest {

  @Test
  public void testResetTournament() {
    Organizer org = Organizer.getInstance();
    
    Tournament t = org.getCurrentTournament();
    Calendar cal = new GregorianCalendar();
    t.setBegin(cal);
    t.setEnd(cal);
    
    Tournament t2 = new Tournament();
    t2.setBegin(cal);
    t2.setEnd(cal);
    
    assertEquals(t2, t);
    
    org.getCurrentTournament().setName("Test");
    assertNotSame(t2, t);
    
    t2.setName("Test");
    
    assertEquals(t2, t);
    
    org.resetTournament();
    
    Tournament t3 = org.getCurrentTournament();
    assertEquals(t, t3);
    
    t2.setName(ResourceManager.getText(Text.NEW_TOURNAMENT));
    t2.setBegin(t3.getBegin());
    t2.setEnd(t3.getEnd());
    
    assertEquals(t2, t3);    
  }
}
