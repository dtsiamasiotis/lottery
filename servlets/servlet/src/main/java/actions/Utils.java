package actions;

import actions.CreateTicketRequest;
import dao.TicketDAO;
import entities.Ticket;
import services.DrawService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class Utils {
    @Inject
    private TicketDAO ticketDAO;

    @EJB
    private DrawService drawService;

    public Ticket requestToNewTicket(CreateTicketRequest createTicketRequest) {
        Ticket t = new Ticket();
        t.setNumbers(createTicketRequest.getNumbers());
        t.setDatePlayed(new Date());
        t.setValid(true);
        t.setTicketId(ticketDAO.getNextTicketId());
        t.setDrawId(drawService.getCurrentDrawId());
        return t;
    }
}
