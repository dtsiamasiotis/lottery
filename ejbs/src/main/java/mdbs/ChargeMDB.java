package mdbs;

import dao.ChargeDAO;
import dao.TicketDAO;
import entities.Charge;
import events.ChargeEvent;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
        name = "FailedPackagePurchaseMDB",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/CHARGES_QUEUE"),
        }
)
public class ChargeMDB implements MessageListener {

    @Inject
    private ChargeDAO chargeDAO;

    @Inject
    private TicketDAO ticketDAO;

    @Override
    public void onMessage(Message message) {
        try {
            ChargeEvent chargeEvent = (ChargeEvent) message.getBody(ChargeEvent.class);
            Charge charge = new Charge();
            charge.setAmount(chargeEvent.getAmount());
            charge.setMsisdn(chargeEvent.getMsisdn());
            charge.setTicket(ticketDAO.findTicketById(chargeEvent.getTicketId()));
            chargeDAO.saveCharge(charge);
            System.out.println("charge event received");
        }catch(Exception e){e.printStackTrace();
        }
    }
}
