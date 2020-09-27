package dao;

import entities.Participant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class ParticipantDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void addParticipant(Participant participant)
    {
        entityManager.persist(participant);
    }
    public Participant findParticipantByMsisdn(String msisdn)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM participants WHERE msisdn = ? FOR UPDATE", Participant.class);
        q.setParameter(1,Long.parseLong(msisdn));
        try {
            Participant participant = (Participant) q.getSingleResult();
            return participant;
        }catch(javax.persistence.NoResultException nre) {return null;}

    }

    public void saveParticipant(Participant newParticipant)
    {
        entityManager.persist(newParticipant);
    }

    public void updateParticipant(Participant participant)
    {
        entityManager.merge(participant);
    }

    public void lockParticipant(long msisdn) {
        Query q = entityManager.createNativeQuery("SELECT COUNT(*) from pg_advisory_xact_lock(?)");
        q.setParameter(1,msisdn);
        try {
            List<Object> result = q.getResultList();
        }catch(javax.persistence.NoResultException nre) {return;}

    }
}
