package com.easytournament.tournament.calc;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.easytournament.basic.rule.RuleType;
import com.easytournament.basic.valueholder.Rule;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.Group;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.valueholder.TableEntry;
import com.jgoodies.common.collect.ArrayListModel;

public class AwayDefeatsRuleTest {

  private Group g;
  private Team teamA = new Team(0);
  private Team teamB = new Team(1);
  private Team teamC = new Team(2);
  private Team teamD = new Team(3);
  private Team teamE = new Team(4);
  private Team teamF = new Team(5);

  @Before
  public void setUp() throws Exception {
    Rule.initRules();

    this.g = new Group("A", 6);
    this.g.setDefaultRules(false);
    this.g.setDefaultSettings(false);

    SportSettings settings = new SportSettings();
    settings.setPointPerHomeDefeat(0);
    settings.setPointPerAwayDefeat(1);
    settings.setPointPerHomeDefeatOvertime(2);
    settings.setPointPerAwayDefeatOvertime(3);
    settings.setPointPerHomeDefeatPenalty(4);
    settings.setPointPerAwayDefeatPenalty(5);
    settings.setPointPerHomeDraw(6);
    settings.setPointPerAwayDraw(7);
    settings.setPointPerHomeDrawOvertime(8);
    settings.setPointPerAwayDrawOvertime(9);
    settings.setPointPerHomeWinPenalty(10);
    settings.setPointPerAwayWinPenalty(11);
    settings.setPointPerHomeWinOvertime(12);
    settings.setPointPerAwayWinOvertime(13);
    settings.setPointPerHomeWin(14);
    settings.setPointPerAwayWin(15);
    this.g.setSettings(settings);

    ArrayListModel<Team> teams = new ArrayListModel<Team>();

    teams.add(this.teamA);
    this.g.getPosition(0).setTeam(this.teamA);
    teams.add(this.teamB);
    this.g.getPosition(1).setTeam(this.teamB);
    teams.add(this.teamC);
    this.g.getPosition(2).setTeam(this.teamC);
    teams.add(this.teamD);
    this.g.getPosition(3).setTeam(this.teamD);
    teams.add(this.teamE);
    this.g.getPosition(4).setTeam(this.teamE);
    teams.add(this.teamF);
    this.g.getPosition(5).setTeam(this.teamF);
    this.g.setTeams(teams);

    ArrayList<Position> positions = this.g.getPositions();

    ArrayListModel<ScheduleEntry> schedules = new ArrayListModel<ScheduleEntry>();
    ScheduleEntry se1 = new ScheduleEntry(positions.get(0), positions.get(1));
    schedules.add(se1);
    ScheduleEntry se2 = new ScheduleEntry(positions.get(0), positions.get(2));
    schedules.add(se2);
    ScheduleEntry se3 = new ScheduleEntry(positions.get(0), positions.get(3));
    schedules.add(se3);
    ScheduleEntry se4 = new ScheduleEntry(positions.get(0), positions.get(4));
    schedules.add(se4);
    ScheduleEntry se5 = new ScheduleEntry(positions.get(0), positions.get(5));
    schedules.add(se5);
    ScheduleEntry se6 = new ScheduleEntry(positions.get(1), positions.get(2));
    schedules.add(se6);
    ScheduleEntry se7 = new ScheduleEntry(positions.get(1), positions.get(3));
    schedules.add(se7);
    ScheduleEntry se8 = new ScheduleEntry(positions.get(1), positions.get(4));
    schedules.add(se8);
    ScheduleEntry se9 = new ScheduleEntry(positions.get(1), positions.get(5));
    schedules.add(se9);
    ScheduleEntry se10 = new ScheduleEntry(positions.get(2), positions.get(3));
    schedules.add(se10);
    ScheduleEntry se11 = new ScheduleEntry(positions.get(2), positions.get(4));
    schedules.add(se11);
    ScheduleEntry se12 = new ScheduleEntry(positions.get(2), positions.get(5));
    schedules.add(se12);
    ScheduleEntry se13 = new ScheduleEntry(positions.get(3), positions.get(4));
    schedules.add(se13);
    ScheduleEntry se14 = new ScheduleEntry(positions.get(3), positions.get(5));
    schedules.add(se14);
    ScheduleEntry se15 = new ScheduleEntry(positions.get(4), positions.get(5));
    schedules.add(se15);

    se1.setNumHomeGoals(0);
    se1.setNumAwayGoals(1);
    se1.setGamePlayed(true);

    se2.setNumHomeGoals(0);
    se2.setNumAwayGoals(2);
    se2.setGamePlayed(true);

    se3.setNumHomeGoals(1);
    se3.setNumAwayGoals(3);
    se3.setGamePlayed(true);

    se4.setNumHomeGoals(2);
    se4.setNumAwayGoals(4);
    se4.setGamePlayed(true);

    se5.setNumHomeGoals(2);
    se5.setNumAwayGoals(3);
    se5.setGamePlayed(true);

    se6.setNumHomeGoals(1);
    se6.setNumAwayGoals(2);
    se6.setGamePlayed(true);

    se7.setNumHomeGoals(0);
    se7.setNumAwayGoals(0);
    se7.setGamePlayed(true);

    se8.setNumHomeGoals(1);
    se8.setNumAwayGoals(1);
    se8.setGamePlayed(true);

    se9.setNumHomeGoals(2);
    se9.setNumAwayGoals(2);
    se9.setGamePlayed(true);

    se10.setNumHomeGoals(5);
    se10.setNumAwayGoals(0);
    se10.setGamePlayed(true);

    se11.setNumHomeGoals(4);
    se11.setNumAwayGoals(1);
    se11.setGamePlayed(true);

    se12.setNumHomeGoals(1);
    se12.setNumAwayGoals(0);
    se12.setGamePlayed(true);

    se13.setNumHomeGoals(2);
    se13.setNumAwayGoals(1);
    se13.setGamePlayed(true);

    se14.setNumHomeGoals(1);
    se14.setNumAwayGoals(0);
    se14.setGamePlayed(true);

    se15.setNumHomeGoals(3);
    se15.setNumAwayGoals(5);
    se15.setGamePlayed(true);

    this.g.setSchedules(schedules);
  }

  @After
  public void tearDown() throws Exception {
    // do nothing
  }

  @Test
  public void descending() {

    ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
    Rule rule = Rule.ruleMap.get(RuleType.AWAY_DEFEATS_RULE);
    rule.setAscending(false);
    rules.add(rule);
    this.g.setRules(rules);

    Calculator.calcTableEntries(this.g, false);

    ArrayList<TableEntry> table = this.g.getTable();
    TableEntry first = table.get(0);
    TableEntry second = table.get(1);
    TableEntry third = table.get(2);
    TableEntry fourth = table.get(3);
    TableEntry fifth = table.get(4);
    TableEntry sixth = table.get(5);

    assertEquals(this.teamE, first.getTeam());
    assertEquals(this.teamF, second.getTeam());
    assertEquals(this.teamD, third.getTeam());
    assertEquals(this.teamA, fourth.getTeam());
    assertEquals(this.teamB, fifth.getTeam());
    assertEquals(this.teamC, sixth.getTeam());

    assertEquals(2, first.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(2, second.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(1, third.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(0, fourth.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(0, fifth.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(0, sixth.getValue(Values.AWAY_DEFEATS).intValue());
  }

  @Test
  public void ascending() {

    ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
    Rule rule = Rule.ruleMap.get(RuleType.AWAY_DEFEATS_RULE);
    rule.setAscending(true);
    rules.add(rule);
    this.g.setRules(rules);

    Calculator.calcTableEntries(this.g, false);

    ArrayList<TableEntry> table = this.g.getTable();
    TableEntry first = table.get(0);
    TableEntry second = table.get(1);
    TableEntry third = table.get(2);
    TableEntry fourth = table.get(3);
    TableEntry fifth = table.get(4);
    TableEntry sixth = table.get(5);

    assertEquals(this.teamA, first.getTeam());
    assertEquals(this.teamB, second.getTeam());
    assertEquals(this.teamC, third.getTeam());
    assertEquals(this.teamD, fourth.getTeam());
    assertEquals(this.teamE, fifth.getTeam());
    assertEquals(this.teamF, sixth.getTeam());

    assertEquals(0, first.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(0, second.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(0, third.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(1, fourth.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(2, fifth.getValue(Values.AWAY_DEFEATS).intValue());
    assertEquals(2, sixth.getValue(Values.AWAY_DEFEATS).intValue());
  }

}
