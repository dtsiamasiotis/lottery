package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DrawDAO;
import dao.WinnerDAO;
import entities.Draw;
import entities.OutgoingRequestsLog;
import entities.Ticket;
import entities.Winner;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import utils.WinnerRestModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Singleton
public class DrawService {

    private int drawId;

    @PersistenceContext
    private EntityManager entityManager;

    private Draw currentDraw;

    @Inject
    private DrawDAO drawDAO;

    @Inject
    private WinnerDAO winnerDAO;

    @EJB
    private PrizeService prizeService;

    @EJB
    private OutgoingRequestLogService outgoingRequestLogService;

    @PostConstruct
    public void init()
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM draws ORDER BY id DESC LIMIT 1", Draw.class);
        try{
            currentDraw = (Draw)q.getSingleResult();
        }catch(javax.persistence.NoResultException nre){currentDraw = null;}

    }

    public long getCurrentDrawId()
    {
        return currentDraw.getId();
    }

    public void performDraw()
    {
        String winningNumbers = generateLuckyNumbers(12);
        currentDraw.setWinningNumbers(winningNumbers);
        drawDAO.updateDraw(currentDraw);

    }

    public void createDraw()
    {
        Draw newDraw = new Draw();
        currentDraw = newDraw;
        currentDraw.setDrawTime(new Date());
        drawDAO.saveDraw(newDraw);
    }

    public String generateLuckyNumbers(int numbersToBeGenerated) {
        Random numbersGenerator = new Random();

        String numbersStr = "";

        List<Integer> availableNumbers = new ArrayList<Integer>();
        List<Integer> luckyNumbers = new ArrayList<Integer>();

        int k = 0;
        for (k = 0; k < 80; k++)
            availableNumbers.add(k + 1);

        int i = 0;
        for (i = 0; i < numbersToBeGenerated; i++) {
            Integer next = numbersGenerator.nextInt(80 - i);
            luckyNumbers.add(availableNumbers.remove(next.intValue()));
        }

        //Collections.sort(luckyNumbers);
        int numbersSize = luckyNumbers.size();
        for (i = 0; i < numbersSize - 1; i++) {
            numbersStr += luckyNumbers.get(i).toString() + ",";
        }

        numbersStr += luckyNumbers.get(numbersSize - 1);

        return numbersStr;
    }

    public void findWinners()
    {
        List<Ticket> candidates = fetchTicketsToCheck();
        candidates.stream().map(Ticket->findUserEntryDrawCommonNumbers(Ticket)).filter(t->t.getWinner()!=null).forEach(t->saveWinnerToDatabase(t));
    }

    public List<Ticket> fetchTicketsToCheck()
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM tickets WHERE draw_id=?",Ticket.class);
        q.setParameter(1, currentDraw.getId());
        List<Ticket> ticketsPlayed = q.getResultList();
        return ticketsPlayed;
    }



    public void removeArrayNumbers(ArrayList<Short> array, int minIndex, int maxIndex) {
        for (int j = maxIndex; j >= minIndex; j--) {
            array.remove(j);
        }
    }

    public ArrayList<Short> getTicketNumbersFromString(String numbers) throws Exception {
        ArrayList<Short> playedNumbers = new ArrayList<Short>();

        StringTokenizer strTok = new StringTokenizer(numbers, ",");
        while (strTok.hasMoreTokens()) {
            String currNumber = strTok.nextToken();
            playedNumbers.add(Short.parseShort(currNumber));
        }

        return playedNumbers;
    }

    public Ticket findUserEntryDrawCommonNumbers(Ticket t)
    {
        short foundNumbers = 0;
        boolean hasToBreak = false;
        ArrayList<Short> matchedNumbers = new ArrayList<Short>();
        ArrayList<Short> numbersPlayed = new ArrayList<Short>();
        ArrayList<Short> numbersDrawn = new ArrayList<Short>();
        try {
            numbersPlayed = getTicketNumbersFromString(t.getNumbers());
            numbersDrawn = getTicketNumbersFromString(currentDraw.getWinningNumbers());
        }catch(Exception e){}

        Collections.sort(numbersPlayed);
        Collections.sort(numbersDrawn);


        for (int i = 0; i < numbersPlayed.size(); i++) {
            for (int j = numbersDrawn.size() - 1; j >= 0; j--) {
                if (numbersPlayed.get(numbersPlayed.size() - 1) < numbersDrawn.get(0)) {
                    hasToBreak = true;
                    break;
                }
                if (numbersPlayed.get(0) > numbersDrawn.get(numbersDrawn.size() - 1)) {
                    hasToBreak = true;
                    break;
                }

                if (numbersPlayed.get(i) == numbersDrawn.get(j)) {
                    foundNumbers++;
                    matchedNumbers.add(numbersPlayed.get(i));
                    removeArrayNumbers(numbersDrawn, 0, j);
                    break;
                }
            }

            if (hasToBreak) {
                break;
            }

            numbersPlayed.remove(0);
            i = -1;
        }


        if(foundNumbers>=3) {
            Winner newWinner = new Winner();
            newWinner.setDrawId(currentDraw.getId());
            newWinner.setTicket(t);
            newWinner.setWinningNumbersCount(foundNumbers);
            newWinner.setWinningNumbers(buildWinningNumbersString(matchedNumbers));
            newWinner.setPrize(prizeService.findPrizeByNumbersCount(foundNumbers));
            t.setWinner(newWinner);

        }
        return t;
    }

    public void saveWinnerToDatabase(Ticket t)
    {
        winnerDAO.saveWinner(t.getWinner());
    }

    public String buildWinningNumbersString(ArrayList<Short> matchedNumbers)
    {
        String winningNumbersStr = "";
        for(int i=0;i<matchedNumbers.size();i++)
        {
            winningNumbersStr += String.valueOf(matchedNumbers.get(i));
            if(i<matchedNumbers.size()-1)
                winningNumbersStr +=",";
        }

        return winningNumbersStr;
    }

    public void informWinners()
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM winners WHERE draw_id=?",Winner.class);
        q.setParameter(1,getCurrentDrawId());
        List<Winner> winners = q.getResultList();

        for(Winner winner:winners)
        {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target("http://localhost:4000/informWinners");
            String amount = String.valueOf(winner.getPrize().getAmount());
            String msisdn = String.valueOf(winner.getTicket().getParticipant().getMsisdn());
            String requestBody = "{\"msisdn\":"+msisdn+",\"amount\":\""+amount+"\"}";
            WinnerRestModel winnerRestModel = createWinnerRestModel(winner);
            OutgoingRequestsLog outgoingRequestsLog = new OutgoingRequestsLog();
            ObjectMapper objectMapper = new ObjectMapper();
            outgoingRequestsLog.setOutgoingRequestTstamp(new Date());
            Response response = target.request().post(Entity.entity(winnerRestModel, MediaType.APPLICATION_JSON));
            String responseBody = response.readEntity(String.class);
            response.close();

            try {
                String jsonRequest = objectMapper.writeValueAsString(winnerRestModel);
                outgoingRequestsLog.setOutgoingRequest(jsonRequest);
            }catch(Exception e){}

            outgoingRequestsLog.setResponse(responseBody);
            outgoingRequestsLog.setResponseTstamp(new Date());
            outgoingRequestLogService.saveOutgoingRequestLog(outgoingRequestsLog);
        }
    }

    public WinnerRestModel createWinnerRestModel(Winner winner)
    {
        WinnerRestModel winnerRestModel = new WinnerRestModel();
        winnerRestModel.setDrawId(winner.getDrawId());
        winnerRestModel.setMsisdn(winner.getTicket().getParticipant().getMsisdn());
        winnerRestModel.setPrizeAmount(winner.getPrize().getAmount());
        winnerRestModel.setTicketId(winner.getTicket().getTicketId());
        winnerRestModel.setWinningNumbers(winner.getWinningNumbers());
        return winnerRestModel;
    }
}

