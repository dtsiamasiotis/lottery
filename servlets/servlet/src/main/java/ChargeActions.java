import com.fasterxml.jackson.databind.ObjectMapper;
import entities.IncomingRequestsLog;
import entities.Ticket;
import events.ChargeEvent;
import services.IncomingRequestLogService;
import services.JMSService;
import utils.QueueType;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;

@Local
@Path("/chargeActions")
public class ChargeActions {

    @EJB
    private IncomingRequestLogService incomingRequestLogService;

    @Inject
    private IncomingRequestsLog incomingRequestsLog;

    @EJB
    private JMSService jmsService;

    @POST
    @Path("/addCharge")
    public Response handleAddCharge(String requestBody)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ChargeEvent newChargeEvent = null;
        incomingRequestsLog.setIncomingRequest(requestBody);
        incomingRequestsLog.setIncomingRequestTstamp(new Date());

        try {
            newChargeEvent = objectMapper.readValue(requestBody, ChargeEvent.class);
        }catch(Exception e){return Response.serverError().build();}

        jmsService.send(newChargeEvent, QueueType.CHARGE);
        return Response.ok("OK").build();
    }
}
