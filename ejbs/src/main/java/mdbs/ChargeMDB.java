package mdbs;

import dao.ChargeDAO;
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
    private Charge charge;

    @Override
    public void onMessage(Message message) {
        try {
            ChargeEvent chargeEvent = (ChargeEvent) message.getBody(ChargeEvent.class);
            charge.setAmount(chargeEvent.getAmount());
            charge.setMsisdn(chargeEvent.getMsisdn());
            //charge.setTicket(chargeEvent.getTicketId());
            System.out.println("charge event received");
        }catch(Exception e){}
    }
}
