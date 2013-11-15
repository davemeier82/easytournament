package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.easytournament.webapp.entity.Country;

@Stateless(name = "countrycontroller")
public class CountryController implements CountryControllerInterface {

  @PersistenceContext
  private EntityManager em;

  @SuppressWarnings("unchecked")
  @Override
  public List<Country> loadCountries() {
    Query q = em.createQuery("from Country");
    List<?> results = q.getResultList();
    return (List<Country>)results;
  }

}
