import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Participant;
import entities.IncomingRequestsLog;
import entities.Ticket;
import services.LottoService;
import services.IncomingRequestLogService;
import utils.NumbersValidator;
import utils.NumbersValidatorResult;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.inject.Inject;
import javax.validation.Valid;
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

  @Inject
  private NumbersValidator numbersValidator;

  @Inject
  private Utils utils;

    @POST
    @Path("createTicket")
    public Response handleCreateTicket(@Valid CreateTicketRequest createTicketRequest)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
          requestBody = objectMapper.writeValueAsString(createTicketRequest);
        } catch(Exception e) {return Response.serverError().build();}

        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("create ticket");

        String numbers = createTicketRequest.getNumbers();
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbers,6);

        if(NumbersValidatorResult.VALID_SELECTION.equals(numbersValidatorResult)) {
            Participant participant = lottoService.findOrCreateParticipant(createTicketRequest.getMsisdn());
            boolean participantExists = true;
            if (participant == null) {
                participantExists = false;
                participant = lottoService.createNewParticipant(Long.valueOf(createTicketRequest.getMsisdn()));
            }

            Ticket ticket = utils.requestToNewTicket(createTicketRequest);

            participant = lottoService.addTicketToParticipant(participant, ticket);
            lottoService.saveOrUpdateParticipant(participant,participantExists);

            incomingRequestsLog.setResponse("OK");
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);

            return Response.ok("OK").build();
        }
        else{
            String response = lottoService.createFailedResponseForNumbersValidation(numbersValidatorResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return Response.ok(response).build();
      }
    }

    @POST
    @Path("editTicket")
    public Response handleEditTicket(@Valid EditTicketRequest editTicketRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(editTicketRequest);
        }catch(Exception e){return Response.serverError().build();}

        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("edit ticket");

        String numbers = editTicketRequest.getNumbers();
        NumbersValidator numbersValidator = new NumbersValidator();
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbers, 6);
        if (NumbersValidatorResult.VALID_SELECTION.equals(numbersValidatorResult)) {
            long ticketId = Long.valueOf(editTicketRequest.getTicketId());
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
            String response = lottoService.createFailedResponseForNumbersValidation(numbersValidatorResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return Response.ok(response).build();
        }
    }


}
