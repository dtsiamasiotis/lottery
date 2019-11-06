import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_it;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

public class ParticipantDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void addParticipant(Participant participant)
    {
        entityManager.persist(participant);
    }
    public Participant findParticipantByMsisdn(String msisdn)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM participants WHERE msisdn = ?", Participant.class);
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
}
