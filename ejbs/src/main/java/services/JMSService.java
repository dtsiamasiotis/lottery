package services;

import utils.QueueType;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.Serializable;
import javax.jms.Destination;
import javax.jms.JMSContext;

public class JMSService {
    @Inject
    private JMSContext jmsctx;

    @Resource(name="jms/chargesQueue")
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
