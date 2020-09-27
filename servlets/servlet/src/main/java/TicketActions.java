import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Participant;
import entities.IncomingRequestsLog;
import entities.Ticket;
import org.jboss.ejb3.annotation.TransactionTimeout;
import services.LottoService;
import services.IncomingRequestLogService;
import utils.NumbersValidator;
import utils.NumbersValidatorResult;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Random;

@Stateless
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
    @Produces("application/json")
    @Path("createTicket")
    @TransactionTimeout(3000)
    public CreateTicketResponse handleCreateTicket(@Valid CreateTicketRequest createTicketRequest)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = "";
        try {
          requestBody = objectMapper.writeValueAsString(createTicketRequest);
        } catch(Exception e) {
        //return Response.serverError().build();
        }

        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("create ticket");

        String numbers = createTicketRequest.getNumbers();
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbers,6);
        long start = System.currentTimeMillis();
        if(NumbersValidatorResult.VALID_SELECTION.equals(numbersValidatorResult)) {
            lottoService.lockParticipant(Long.parseLong(createTicketRequest.getMsisdn()));
            String msisdn = String.valueOf(Integer.parseInt(createTicketRequest.getMsisdn()));
            Participant participant = lottoService.findParticipant(msisdn);
            boolean participantExists = true;
            if (participant == null) {
                participantExists = false;
                participant = lottoService.createNewParticipant(Long.valueOf(msisdn));
            }

            Ticket ticket = utils.requestToNewTicket(createTicketRequest);

            participant = lottoService.addTicketToParticipant(participant, ticket);
            lottoService.saveOrUpdateParticipant(participant,participantExists);
            long end = System.currentTimeMillis();
            incomingRequestsLog.setResponse("OK");
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);

            System.out.println(ticket.getTicketId()+":"+(end-start));
            return CreateTicketResponse.builder().errorCode("00").ticketId(String.valueOf(ticket.getTicketId())).build();
        }
        else{
            String response = lottoService.createFailedResponseForNumbersValidation(numbersValidatorResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return CreateTicketResponse.builder().errorCode("01").errorDescription(response).build();
      }
    }

    @POST
    @Produces("application/json")
    @Path("editTicket")
    public EditTicketResponse handleEditTicket(@Valid EditTicketRequest editTicketRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(editTicketRequest);
        }catch(Exception e){return EditTicketResponse.builder().errorCode("02").errorDescription("Error parsing request").build();}

        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());
        incomingRequestsLog.setRequestType("edit ticket");

        String numbers = editTicketRequest.getNumbers();
        NumbersValidator numbersValidator = new NumbersValidator();
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbers, 6);
        long ticketId = Long.valueOf(editTicketRequest.getTicketId());

        if (NumbersValidatorResult.VALID_SELECTION.equals(numbersValidatorResult)) {
            Ticket existingTicket = lottoService.findValidTicketById(ticketId);
            if(existingTicket==null)
                return EditTicketResponse.builder().errorCode("03").errorDescription("TICKET WAS NOT FOUND").build();
            existingTicket.setValid(false);
            Ticket editedTicket = lottoService.createEditedTicket(existingTicket,numbers);
            lottoService.saveTicket(editedTicket);
            lottoService.updateTicket(existingTicket);
            incomingRequestsLog.setResponse("OK");
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return EditTicketResponse.builder().errorCode("00").newNumbers(numbers).ticketId(String.valueOf(ticketId)).build();
        }
        else{
            String response = lottoService.createFailedResponseForNumbersValidation(numbersValidatorResult);
            incomingRequestsLog.setResponse(response);
            incomingRequestsLog.setResponseTstamp(new Date());
            incomingRequestLogService.saveIncomingRequestLog(incomingRequestsLog);
            return EditTicketResponse.builder().errorCode("01").errorDescription(response).ticketId(String.valueOf(ticketId)).build();
        }
    }


}
