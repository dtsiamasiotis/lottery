import dao.ParticipantDAO;
import entities.Participant;
import entities.Ticket;
import org.junit.jupiter.api.Test;

public class ParticipantTest {
    @Test
    public void checkAddParticipant() {
        Participant participant = new Participant();
        participant.setMsisdn(121324);
        Ticket ticket = new Ticket();
        ticket.setNumbers("1,2,3,4");
        ticket.setParticipant(participant);
        participant.addTicket(ticket);
        ParticipantDAO participantDAO = new ParticipantDAO();
       // participantDAO.addParticipant(participant);
    }
}
