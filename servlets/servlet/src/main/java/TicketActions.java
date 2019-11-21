import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Participant;
import entities.IncomingRequestsLog;
import entities.Ticket;
import services.LottoService;
import services.IncomingRequestLogService;
import utils.NumbersValidator;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;

@Local
@Path("/ticketActions")
public class TicketActions {

  @EJB
  private LottoService lottoService;

  @EJB
  private IncomingRequestLogService incomingRequestLogService;

  @Inject
  private IncomingRequestsLog incomingRequestsLog;

    @POST
    @Path("createTicket")
    public Response handleCreateTicket(String requestBody)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Ticket newTicket = null;
        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("create ticket");

        try {
            newTicket = objectMapper.readValue(requestBody, Ticket.class);
        }catch(Exception e){return Response.serverError().build();}

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

            incomingRequestsLog.setResponse("OK");
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);

            return Response.ok("OK").build();
        }
        else{
            String response = lottoService.createFailedResponseForNumbersValidation(validationResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return Response.ok(response).build();
        }
    }

    @POST
    @Path("editTicket")
    public Response handleEditTicket(String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        Ticket changedTicket = null;
        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("edit ticket");

        try {
            changedTicket = objectMapper.readValue(requestBody, Ticket.class);
        }catch(Exception e){return Response.serverError().build();}

        String numbers = changedTicket.getNumbers();
        NumbersValidator numbersValidator = new NumbersValidator();
        int validationResult = numbersValidator.checkNumbersString(numbers, 6);
        if (validationResult == 0) {
            long ticketId = changedTicket.getTicketId();
            Ticket existingTicket = lottoService.findValidTicketById(ticketId);
            if(existingTicket==null)
                return Response.ok("TICKET WAS NOT FOUND").build();
            existingTicket.setValid(false);
            Ticket editedTicket = lottoService.createEditedTicket(existingTicket,numbers);
            lottoService.saveTicket(editedTicket);
            lottoService.updateTicket(existingTicket);
            incomingRequestsLog.setResponse("OK");
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return Response.ok("OK").build();
        }
        else{
            String response = lottoService.createFailedResponseForNumbersValidation(validationResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return Response.ok(response).build();
        }
    }

}
