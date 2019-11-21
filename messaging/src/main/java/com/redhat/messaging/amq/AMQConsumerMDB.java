package com.redhat.messaging.amq;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.messaging.models.Account;
import com.redhat.messaging.models.Order;
import com.redhat.messaging.persistence.Registration;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@MessageDriven(name = "AMQQueueMDB",
        mappedName = "jms/queue/RH/JBOSS/queue",
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "RH.JBOSS.QUEUE"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
        }
)


public class AMQConsumerMDB implements MessageListener {
    private final static Logger LOGGER = Logger.getLogger(AMQConsumerMDB.class.toString());
    ObjectMapper mapper = new ObjectMapper();

    @EJB
    Registration registration;

    private static Map<String,Integer> redeliveryMap = new HashMap<String, Integer>();
    public void onMessage(Message message) {
        try {
            LOGGER.info("[" + message.getJMSMessageID() + "] received from[" + message.getJMSDestination().toString() + "]");
            LOGGER.info("text[" + ((TextMessage) message).getText() + "]");
            storeMessage(message);
            redeliveryCount(message.getJMSMessageID());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void storeMessage(Message message) {
        try {
            String type = message.getStringProperty("TYPE");
            switch(type) {
                case "NORMAL":
                    break;
                case "ACCOUNT":
                    Account account = mapper.readValue(((TextMessage)message).getText(), Account.class);
                    registration.registerAccount(account);
                    break;
                case "ORDER":
                    Order order = mapper.readValue(((TextMessage)message).getText(), Order.class);
                    registration.registerOrder(order);
                    break;
                default:
                    if (((TextMessage)message).getText().toLowerCase().contains("error")) {
                        LOGGER.info("Messagge contains \"error\", EXCEPTION!!!");
                        throw new Exception("Messagge contains \"error\", EXCEPTION!!!");
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void redeliveryCount(String jmsMessageID) {
        if(redeliveryMap.containsKey(jmsMessageID)){
            int redelivery = redeliveryMap.get(jmsMessageID)+1;
            redeliveryMap.put(jmsMessageID,redelivery);
            LOGGER.info("["+jmsMessageID+"] REDELIVERY ["+redelivery+"]");
        }else{
            LOGGER.info("["+jmsMessageID+"] REDELIVERY [1]");
            redeliveryMap.put(jmsMessageID,1);
        }
    }}
