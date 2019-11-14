package dao;

import entities.IncomingRequestsLog;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class IncomingRequestsLogDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveRestRequest(IncomingRequestsLog incomingRequestsLog)
    {
        entityManager.persist(incomingRequestsLog);
    }
}
