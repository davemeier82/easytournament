package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Country;

@Local
public interface CountryControllerInterface {

  public List<Country> loadCountries();
  
}
