package dao;

import entities.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class TicketDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public long getNextTicketId()
    {
        Query q = entityManager.createNativeQuery("SELECT nextval('ticket_id_seq')");
        long ticketId = ((java.math.BigInteger)q.getSingleResult()).longValue();
        return ticketId;
    }

    public Ticket findValidTicketById(long ticketId)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM tickets WHERE ticket_id=? AND valid = true",Ticket.class);
        q.setParameter(1,ticketId);
        try {
            Ticket t = (Ticket) q.getSingleResult();
            return t;
        }catch(javax.persistence.NoResultException nre){return null;}

    }

    public Ticket findTicketById(long ticketId)
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM tickets WHERE ticket_id=?",Ticket.class);
        q.setParameter(1,ticketId);
        try {
            Ticket t = (Ticket) q.getSingleResult();
            return t;
        }catch(javax.persistence.NoResultException nre){return null;}
    }

    public void saveTicket(Ticket ticket)
    {
        entityManager.persist(ticket);
    }

    public void updateTicket(Ticket ticket)
    {
        entityManager.merge(ticket);
    }
}
