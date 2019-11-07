import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Local
@Path("/ticketActions")
public class TicketActions {

  @EJB
  private LottoService lottoService;

    @POST
    @Path("createTicket")
    public Response handleCreateTicket(Ticket newTicket)
    {
        String numbers = newTicket.getNumbers();
        NumbersValidator numbersValidator = new NumbersValidator();
        int validationResult = numbersValidator.checkNumbersString(numbers,6);
        if(validationResult == 0) {
            Participant participant = lottoService.findOrCreateParticipant(String.valueOf(newTicket.getParticipant().getMsisdn()));
            boolean participantExists = true;
            if (participant == null) {
                participantExists = false;
                participant = lottoService.createNewParticipant(newTicket.getParticipant().getMsisdn());
            }

            Ticket ticket = lottoService.createNewTicket(newTicket);
            participant = lottoService.addTicketToParticipant(participant, ticket);


            lottoService.saveOrUpdateParticipant(participant,participantExists);
            return Response.ok("OK").build();
        }
        else{
            return Response.ok(lottoService.createFailedResponseForNumbersValidation(validationResult)).build();
        }
    }

    @POST
    @Path("editTicket")
    public Response handleEditTicket(Ticket changedTicket) {
        String numbers = changedTicket.getNumbers();
        NumbersValidator numbersValidator = new NumbersValidator();
        int validationResult = numbersValidator.checkNumbersString(numbers, 6);
        if (validationResult == 0) {
            long ticketId = changedTicket.getTicketId();
            Ticket existingTicket = lottoService.findValidTicketById(ticketId);
            existingTicket.setValid(false);
            Ticket editedTicket = lottoService.createEditedTicket(existingTicket,numbers);
            lottoService.saveTicket(editedTicket);
            lottoService.updateTicket(existingTicket);
        }

        return Response.ok("OK").build();
    }

}
