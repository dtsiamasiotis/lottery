package services;

import dao.PromoGroupDAO;
import entities.Participant;
import entities.PromoGroup;
import events.PromoEvent;
import utils.QueueType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PromoService {

    @Inject
    private PromoGroupDAO promoGroupDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private JMSService jmsService;

    public void sendPromo(String promoCommand) {
        String commandParts[] = promoCommand.split(" ");
        PromoGroup promoGroup = findPromoGroupByid(Long.parseLong(commandParts[1]));
        String sqlQuery = promoGroup.getSqlQuery();
        List<Long> msisdns = fetchPromoMsisdns(sqlQuery);
        if(msisdns != null)
            for(Long msisdn: msisdns) {
                PromoEvent promoEvent = new PromoEvent();
                promoEvent.setMsisdn(String.valueOf(msisdn));
                promoEvent.setMessage("testing promo");
                jmsService.send(promoEvent, QueueType.PROMO);
            }

    }

    public PromoGroup findPromoGroupByid(long id) {
        return promoGroupDAO.findPromoGroupById(id);
    }

    private List<Long> fetchPromoMsisdns(String sqlQuery) {
        Query q = entityManager.createQuery(sqlQuery);
        try {
            List<Long> msisdns = q.getResultList();
            return msisdns;
        }catch(javax.persistence.NoResultException nre) {return null;}
    }
}
