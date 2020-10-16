package mdbs;

import events.PromoEvent;
import utils.HttpAdapter;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
        name = "promoMDB",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/PROMO_QUEUE"),
        }
)

public class PromoMDB implements MessageListener {

    @EJB
    private HttpAdapter httpAdapter;

    @Override
    public void onMessage(Message message) {
        try {
            PromoEvent promoEvent = (PromoEvent) message.getBody(PromoEvent.class);
            httpAdapter.sendPromoToMsisdn(promoEvent);
            System.out.println("charge event received");
        }catch(Exception e){e.printStackTrace();
        }
    }
}
