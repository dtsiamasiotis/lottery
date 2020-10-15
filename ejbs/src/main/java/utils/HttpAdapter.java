package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.OutgoingRequestsLog;
import entities.Winner;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import services.OutgoingRequestLogService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Stateless
public class HttpAdapter {

    @EJB
    private PropertiesReader propertiesReader;

    @EJB
    private OutgoingRequestLogService outgoingRequestLogService;

    public void sendInformWinnersRequest(Winner winner) {

        String url = propertiesReader.getProperty("informWinners.url");
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);

        WinnerRequest winnerRequest = WinnerRequest.builder()
                .drawId(winner.getDrawId())
                .msisdn(winner.getTicket().getParticipant().getMsisdn())
                .prizeAmount(winner.getPrize().getAmount())
                .ticketId(winner.getTicket().getTicketId())
                .winningNumbers(winner.getWinningNumbers())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        Date requestTstamp = new Date();
        Response response = target.request().post(Entity.entity(winnerRequest, MediaType.APPLICATION_JSON));
        String responseBody = response.readEntity(String.class);
        Date responseTstamp = new Date();
        response.close();
        String requestBody = new String();

        try {
            requestBody = objectMapper.writeValueAsString(winnerRequest);
        }catch(Exception e){}

        logSendInformWinnersRequest(requestTstamp, requestBody, responseBody, responseTstamp);

    }

    private void logSendInformWinnersRequest(Date requestTstamp, String requestBody, String responseBody, Date responseTstamp) {

        OutgoingRequestsLog outgoingRequestsLog = new OutgoingRequestsLog();
        outgoingRequestsLog.setOutgoingRequestTstamp(requestTstamp);
        outgoingRequestsLog.setOutgoingRequest(requestBody);
        outgoingRequestsLog.setResponse(responseBody);
        outgoingRequestsLog.setResponseTstamp(responseTstamp);
        outgoingRequestLogService.saveOutgoingRequestLog(outgoingRequestsLog);

    }

}
