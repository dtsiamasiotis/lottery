package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import entities.Prize;

@Stateless
public class PrizeService {
    @PersistenceContext
    private EntityManager entityManager;

    public Prize findPrizeByNumbersCount(int numbersCount)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM prizes WHERE numbersCount=?", Prize.class);
        q.setParameter(1, numbersCount);
        Prize prizeFound = (Prize)q.getSingleResult();
        return prizeFound;
    }
}
