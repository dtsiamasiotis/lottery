package dao;

import entities.Winner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class WinnerDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveWinner(Winner winner)
    {
        entityManager.persist(winner);
    }
}
