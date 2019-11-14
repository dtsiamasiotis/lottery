package dao;

import entities.OutgoingRequestsLog;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class OutgoingRequestsLogDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveRestRequest(OutgoingRequestsLog outgoingRequestsLog)
    {
        entityManager.persist(outgoingRequestsLog);
    }
}
