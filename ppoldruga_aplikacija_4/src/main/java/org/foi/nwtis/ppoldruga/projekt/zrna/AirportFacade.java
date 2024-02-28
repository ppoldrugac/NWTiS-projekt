/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ppoldruga.projekt.zrna;

import java.util.List;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.ppoldruga.projekt.jpa.Airports;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author dkermek
 */
@Stateless
public class AirportFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("AirportFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(Airports airport) {
    em.persist(airport);
  }

  public void edit(Airports airport) {
    em.merge(airport);
  }

  public void remove(Airport Airport) {
    em.remove(em.merge(Airport));
  }

  public Airports find(Object id) {
    return em.find(Airports.class, id);
  }

  public List<Airports> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    cq.select(cq.from(Airports.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Airports> findAll(int odBroja, int broj) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    cq.select(cq.from(Airports.class));
    TypedQuery<Airports> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Airports> rt = cq.from(Airports.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
