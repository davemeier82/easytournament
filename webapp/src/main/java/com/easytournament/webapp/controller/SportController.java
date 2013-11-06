package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.easytournament.webapp.entity.Sport;

@Stateless(name = "sportcontroller")
public class SportController implements SportControllerInterface {

  @PersistenceContext
  private EntityManager em; 
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Sport> loadSports() {
    Query q = em.createQuery("from Sport");
    List<?> results = q.getResultList();
    return (List<Sport>)results;
  }

}
