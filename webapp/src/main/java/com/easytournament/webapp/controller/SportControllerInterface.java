package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Sport;

@Local
public interface SportControllerInterface {

  public List<Sport> loadSports();
  
}
