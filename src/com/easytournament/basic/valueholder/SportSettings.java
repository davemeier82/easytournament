package com.easytournament.basic.valueholder;

import com.jgoodies.binding.beans.Model;

public class SportSettings extends Model {
  
  
  public static final String PROPERTY_NUMGAMETIMES = "numGameTimes";
  public static final String PROPERTY_MINPERGAMETIME = "minPerGameTime";
  public static final String PROPERTY_NUMOVERTIMES = "numOvertimes";
  public static final String PROPERTY_MINPEROVERTIME = "minPerOvertime";
  public static final String PROPERTY_POINTPER_H_WIN = "pointPerHomeWin";
  public static final String PROPERTY_POINTPER_H_DRAW = "pointPerHomeDraw";
  public static final String PROPERTY_POINTPER_H_DEFEAT = "pointPerHomeDefeat";
  public static final String PROPERTY_POINTPER_H_WIN_OT = "pointPerHomeWinOvertime";
  public static final String PROPERTY_POINTPER_H_DRAW_OT = "pointPerHomeDrawOvertime";
  public static final String PROPERTY_POINTPER_H_DEFEAT_OT = "pointPerHomeDefeatOvertime";
  public static final String PROPERTY_POINTPER_H_WIN_P = "pointPerHomeWinPenalty";
  public static final String PROPERTY_POINTPER_H_DEFEAT_P = "pointPerHomeDefeatPenalty";
  public static final String PROPERTY_POINTPER_A_WIN = "pointPerAwayWin";
  public static final String PROPERTY_POINTPER_A_DRAW = "pointPerAwayDraw";
  public static final String PROPERTY_POINTPER_A_DEFEAT = "pointPerAwayDefeat";
  public static final String PROPERTY_POINTPER_A_WIN_OT = "pointPerAwayWinOvertime";
  public static final String PROPERTY_POINTPER_A_DRAW_OT = "pointPerAwayDrawOvertime";
  public static final String PROPERTY_POINTPER_A_DEFEAT_OT = "pointPerAwayDefeatOvertime";
  public static final String PROPERTY_POINTPER_A_WIN_P = "pointPerAwayWinPenalty";
  public static final String PROPERTY_POINTPER_A_DEFEAT_P = "pointPerAwayDefeatPenalty";
  
  private int numGameTimes = 2;
  private int minPerGameTime = 45;
  private int numOvertimes = 2;
  private int minPerOvertime = 15;
    
  private int pointPerHomeWin = 3;
  private int pointPerHomeDraw = 1;
  private int pointPerHomeDefeat = 0;
  private int pointPerHomeWinOvertime = 3;
  private int pointPerHomeDrawOvertime = 1;
  private int pointPerHomeDefeatOvertime = 0;
  private int pointPerHomeWinPenalty = 3;
  private int pointPerHomeDefeatPenalty = 0;
  private int pointPerAwayWin = 3;
  private int pointPerAwayDraw = 1;
  private int pointPerAwayDefeat = 0;
  private int pointPerAwayWinOvertime = 3;
  private int pointPerAwayDrawOvertime = 1;
  private int pointPerAwayDefeatOvertime = 0;
  private int pointPerAwayWinPenalty = 3;
  private int pointPerAwayDefeatPenalty = 0;

  public SportSettings() {}
  
  public void setSportSettings(SportSettings t){
    if(t != null)
    {
      this.setNumGameTimes(t.numGameTimes);
      this.setMinPerGameTime(t.minPerGameTime);
      this.setNumOvertimes(t.numOvertimes);
      this.setMinPerOvertime(t.minPerOvertime);
        
      this.setPointPerHomeWin(t.pointPerHomeWin);
      this.setPointPerHomeDraw(t.pointPerHomeDraw);
      this.setPointPerHomeDefeat(t.pointPerHomeDefeat);
      this.setPointPerHomeWinOvertime(t.pointPerHomeWinOvertime);
      this.setPointPerHomeDrawOvertime(t.pointPerHomeDrawOvertime);
      this.setPointPerHomeDefeatOvertime(t.pointPerHomeDefeatOvertime);
      this.setPointPerHomeWinPenalty(t.pointPerHomeWinPenalty);
      this.setPointPerHomeDefeatPenalty(t.pointPerHomeDefeatPenalty);
      this.setPointPerAwayWin(t.pointPerAwayWin);
      this.setPointPerAwayDraw(t.pointPerAwayDraw);
      this.setPointPerAwayDefeat(t.pointPerAwayDefeat);
      this.setPointPerAwayWinOvertime(t.pointPerAwayWinOvertime);
      this.setPointPerAwayDrawOvertime(t.pointPerAwayDrawOvertime);
      this.setPointPerAwayDefeatOvertime(t.pointPerAwayDefeatOvertime);
      this.setPointPerAwayWinPenalty(t.pointPerAwayWinPenalty);
      this.setPointPerAwayDefeatPenalty(t.pointPerAwayDefeatPenalty);
    }
  }

  public int getNumGameTimes() {
    return numGameTimes;
  }

  public void setNumGameTimes(int numGameTimes) {
    int old = this.numGameTimes;
    this.numGameTimes = numGameTimes;
    this.firePropertyChange(PROPERTY_NUMGAMETIMES, old, this.numGameTimes);
  }

  public int getMinPerGameTime() {
    return minPerGameTime;
  }

  public void setMinPerGameTime(int minPerGameTime) {
    int old = this.minPerGameTime;
    this.minPerGameTime = minPerGameTime;
    this.firePropertyChange(PROPERTY_MINPERGAMETIME, old, this.minPerGameTime);
  }

  public int getPointPerHomeWin() {
    return pointPerHomeWin;
  }

  public int getNumOvertimes() {
    return numOvertimes;
  }

  public void setNumOvertimes(int numOvertimes) {
    int old = this.numOvertimes;
    this.numOvertimes = numOvertimes;
    this.firePropertyChange(PROPERTY_NUMOVERTIMES, old, this.numOvertimes);
  }

  public int getMinPerOvertime() {
    return minPerOvertime;
  }

  public void setMinPerOvertime(int minPerOvertime) {
    int old = this.minPerOvertime;
    this.minPerOvertime = minPerOvertime;
    this.firePropertyChange(PROPERTY_MINPEROVERTIME, old, this.minPerOvertime);
  }

  public void setPointPerHomeWin(int pointPerHomeWin) {
    int old = this.pointPerHomeWin;
    this.pointPerHomeWin = pointPerHomeWin;
    this.firePropertyChange(PROPERTY_POINTPER_H_WIN, old, this.pointPerHomeWin);
  }

  public int getPointPerHomeDraw() {
    return pointPerHomeDraw;
  }

  public void setPointPerHomeDraw(int pointPerHomeDraw) {
    int old = this.pointPerHomeDraw;
    this.pointPerHomeDraw = pointPerHomeDraw;
    this.firePropertyChange(PROPERTY_POINTPER_H_DRAW, old, this.pointPerHomeDraw);
  }

  public int getPointPerHomeDefeat() {
    return pointPerHomeDefeat;
  }

  public void setPointPerHomeDefeat(int pointPerHomeDefeat) {
    int old = this.pointPerHomeDefeat;
    this.pointPerHomeDefeat = pointPerHomeDefeat;
    this.firePropertyChange(PROPERTY_POINTPER_H_DEFEAT, old, this.pointPerHomeDefeat);
  }

  public int getPointPerHomeWinOvertime() {
    return pointPerHomeWinOvertime;
  }

  public void setPointPerHomeWinOvertime(int pointPerHomeWinOvertime) {
    int old = this.pointPerHomeWinOvertime;
    this.pointPerHomeWinOvertime = pointPerHomeWinOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_H_WIN_OT, old, this.pointPerHomeWinOvertime);
  }

  public int getPointPerHomeDefeatOvertime() {
    return pointPerHomeDefeatOvertime;
  }

  public void setPointPerHomeDefeatOvertime(int pointPerHomeDefeatOvertime) {
    int old = this.pointPerHomeDefeatOvertime;
    this.pointPerHomeDefeatOvertime = pointPerHomeDefeatOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_H_DEFEAT_OT, old, this.pointPerHomeDefeatOvertime);
  }

  public int getPointPerHomeWinPenalty() {
    return pointPerHomeWinPenalty;
  }

  public void setPointPerHomeWinPenalty(int pointPerHomeWinPenalty) {
    int old = this.pointPerHomeWinPenalty;
    this.pointPerHomeWinPenalty = pointPerHomeWinPenalty;
    this.firePropertyChange(PROPERTY_POINTPER_H_WIN_P, old, this.pointPerHomeWinPenalty);
  }

  public int getPointPerHomeDefeatPenalty() {
    return pointPerHomeDefeatPenalty;
  }

  public void setPointPerHomeDefeatPenalty(int pointPerHomeDefeatPenalty) {
    int old = this.pointPerHomeDefeatPenalty;
    this.pointPerHomeDefeatPenalty = pointPerHomeDefeatPenalty;
    this.firePropertyChange(PROPERTY_POINTPER_H_DEFEAT_P, old, this.pointPerHomeDefeatPenalty);
  }

  public int getPointPerAwayWin() {
    return pointPerAwayWin;
  }

  public void setPointPerAwayWin(int pointPerAwayWin) {
    int old = this.pointPerAwayWin;
    this.pointPerAwayWin = pointPerAwayWin;
    this.firePropertyChange(PROPERTY_POINTPER_A_WIN, old, this.pointPerAwayWin);
  }

  public int getPointPerAwayDraw() {
    return pointPerAwayDraw;
  }

  public void setPointPerAwayDraw(int pointPerAwayDraw) {
    int old = this.pointPerAwayDraw;
    this.pointPerAwayDraw = pointPerAwayDraw;
    this.firePropertyChange(PROPERTY_POINTPER_A_DRAW, old, this.pointPerAwayDraw);
  }

  public int getPointPerAwayDefeat() {
    return pointPerAwayDefeat;
  }

  public void setPointPerAwayDefeat(int pointPerAwayDefeat) {
    int old = this.pointPerAwayDefeat;
    this.pointPerAwayDefeat = pointPerAwayDefeat;
    this.firePropertyChange(PROPERTY_POINTPER_A_DEFEAT, old, this.pointPerAwayDefeat);
  }

  public int getPointPerAwayWinOvertime() {
    return pointPerAwayWinOvertime;
  }

  public void setPointPerAwayWinOvertime(int pointPerAwayWinOvertime) {
    int old = this.pointPerAwayWinOvertime;
    this.pointPerAwayWinOvertime = pointPerAwayWinOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_A_WIN_OT, old, this.pointPerAwayWinOvertime);
  }

  public int getPointPerAwayDefeatOvertime() {
    return pointPerAwayDefeatOvertime;
  }

  public void setPointPerAwayDefeatOvertime(int pointPerAwayDefeatOvertime) {
    int old = this.pointPerAwayDefeatOvertime;
    this.pointPerAwayDefeatOvertime = pointPerAwayDefeatOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_A_DEFEAT_OT, old, this.pointPerAwayDefeatOvertime);
  }

  public int getPointPerAwayWinPenalty() {
    return pointPerAwayWinPenalty;
  }

  public void setPointPerAwayWinPenalty(int pointPerAwayWinPenalty) {
    int old = this.pointPerAwayWinPenalty;
    this.pointPerAwayWinPenalty = pointPerAwayWinPenalty;
    this.firePropertyChange(PROPERTY_POINTPER_A_WIN_P, old, this.pointPerAwayWinPenalty);
  }

  public int getPointPerAwayDefeatPenalty() {
    return pointPerAwayDefeatPenalty;
  }

  public void setPointPerAwayDefeatPenalty(int pointPerAwayDefeatPenalty) {
    int old = this.pointPerAwayDefeatPenalty;
    this.pointPerAwayDefeatPenalty = pointPerAwayDefeatPenalty;
    this.firePropertyChange(PROPERTY_POINTPER_A_DEFEAT_P, old, this.pointPerAwayDefeatPenalty);
  }

  public int getPointPerHomeDrawOvertime() {
    return pointPerHomeDrawOvertime;
  }

  public void setPointPerHomeDrawOvertime(int pointPerHomeDrawOvertime) {
    int old = this.pointPerHomeDrawOvertime;
    this.pointPerHomeDrawOvertime = pointPerHomeDrawOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_H_DRAW_OT, old, this.pointPerHomeDrawOvertime);
  }

  public int getPointPerAwayDrawOvertime() {
    return pointPerAwayDrawOvertime;
  }

  public void setPointPerAwayDrawOvertime(int pointPerAwayDrawOvertime) {
    int old = this.pointPerAwayDrawOvertime;
    this.pointPerAwayDrawOvertime = pointPerAwayDrawOvertime;
    this.firePropertyChange(PROPERTY_POINTPER_A_DRAW_OT, old, this.pointPerAwayDrawOvertime);
  }  
}
