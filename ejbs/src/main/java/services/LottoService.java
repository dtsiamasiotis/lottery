package services;

import dao.ParticipantDAO;
import dao.TicketDAO;
import entities.Participant;
import entities.Ticket;
import services.DrawService;
import utils.NumbersValidatorResult;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class LottoService {
    @Inject
    private ParticipantDAO participantDAO;

    @Inject
    private TicketDAO ticketDAO;

    @EJB
    private DrawService drawService;

    public Participant findOrCreateParticipant(String msisdn)
    {
        return participantDAO.findParticipantByMsisdn(msisdn);
    }

    public void saveParticipant(Participant newParticipant)
    {
        participantDAO.saveParticipant(newParticipant);
    }

    public void saveOrUpdateParticipant(Participant participant,boolean participantExists)
    {
        if(participantExists)
            participantDAO.updateParticipant(participant);
        else
            participantDAO.saveParticipant(participant);
    }
    public Ticket createNewTicket(Ticket newTicket)
    {
        Ticket t = new Ticket();
        t.setNumbers(newTicket.getNumbers());
        t.setDatePlayed(new Date());
        t.setValid(true);
        t.setTicketId(ticketDAO.getNextTicketId());
        t.setDrawId(drawService.getCurrentDrawId());
        return t;
    }

    public Ticket createEditedTicket(Ticket existingTicket, String numbers)
    {
        Ticket t = new Ticket();
        t.setValid(true);
        t.setNumbers(numbers);
        t.setDatePlayed(new Date());
        t.setTicketId(existingTicket.getTicketId());
        t.setParticipant(existingTicket.getParticipant());
        return t;
    }

    public Participant addTicketToParticipant(Participant participant, Ticket ticket)
    {
        participant.addTicket(ticket);
        return participant;
    }

    public Participant createNewParticipant(Long msisdn)
    {
        Participant participant = new Participant();
        participant.setMsisdn(msisdn);
        participant.setFirst_seen(new Date());
        return participant;
    }

    public String createFailedResponseForNumbersValidation(NumbersValidatorResult numbersValidatorResult)
    {
        String response = numbersValidatorResult.name();
        return response;
    }

    public Ticket findValidTicketById(long ticketId)
    {
        Ticket t = ticketDAO.findValidTicketById(ticketId);
        return t;
    }

    public void saveTicket(Ticket ticket)
    {
        ticketDAO.saveTicket(ticket);
    }

    public void updateTicket(Ticket ticket)
    {
        ticketDAO.updateTicket(ticket);
    }

    public void initTicket(Ticket ticket) {
        ticket.setDatePlayed(new Date());
        ticket.setValid(true);
        ticket.setTicketId(ticketDAO.getNextTicketId());
        ticket.setDrawId(drawService.getCurrentDrawId());
    }
}
