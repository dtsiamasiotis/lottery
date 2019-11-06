import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class LottoService {
    @Inject
    private ParticipantDAO participantDAO;

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

    public String createFailedResponseForNumbersValidation(int validationResult)
    {
        String response="";
        switch(validationResult) {
            case 1:
                response = "INVALID_NUMBER_OF_SELECTIONS";
            case 2:
                response = "INVALID_RANGE_OF_SELECTIONS";
            case 3:
                response = "DUPLICATE_SELECTION";
            case 4:
                response = "ALPHANUMERIC_SELECTION";
            case 5:
                response = "INVALID_GENERIC_SELECTION";
        }

        return response;
    }
}
