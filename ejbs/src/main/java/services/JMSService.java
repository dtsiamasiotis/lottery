package services;

import utils.QueueType;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import javax.jms.Destination;
import javax.jms.JMSContext;

@Stateless
public class JMSService {
    @Inject
    private JMSContext jmsctx;

    @Resource(name="java:/queue/CHARGES_QUEUE")
    private Destination chargesQueue;

    public void send(Serializable s, QueueType queueType)
    {
        javax.jms.JMSProducer p = jmsctx.createProducer();
        try{
            switch(queueType){
                case CHARGE:
                    p.send(chargesQueue,s);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
