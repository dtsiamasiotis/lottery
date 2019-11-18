package dao;

import entities.Charge;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ChargeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveCharge(Charge charge){entityManager.persist(charge);}
}
