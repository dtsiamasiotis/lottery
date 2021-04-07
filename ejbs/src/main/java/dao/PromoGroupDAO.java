package dao;

import entities.PromoGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PromoGroupDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public PromoGroup findPromoGroupById(long id)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM promoGroups WHERE id = ? FOR UPDATE", PromoGroup.class);
        q.setParameter(1,id);
        try {
            PromoGroup promoGroup = (PromoGroup) q.getSingleResult();
            return promoGroup;
        }catch(javax.persistence.NoResultException nre) {return null;}

    }
}
