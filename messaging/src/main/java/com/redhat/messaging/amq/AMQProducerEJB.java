package com.redhat.messaging.amq;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.*;
import java.util.Objects;
import java.util.logging.Logger;

@TransactionManagement(value= TransactionManagementType.CONTAINER)
@Stateless
public class AMQProducerEJB{

    @Resource(lookup = "java:/RH.JBOSS.QUEUE")
    private Queue queue;

    @Resource(lookup = "java:/RemoteJmsXA")
    private ConnectionFactory connectionFactory;

    private final static Logger LOGGER = Logger.getLogger(AMQProducerEJB.class.toString());

    public String send(String type, String text){
        return produceMessage(type, text, queue);
    }


    private String produceMessage(String type, String message,  Destination destinationQueue) {
        String result = "ok";
        String messageText = Objects.isNull(message) ? ""+System.currentTimeMillis():message;
        LOGGER.info("SEND INVOKED on type ["+message+"] and text ["+messageText+"]");
        Connection connection = null;
        try {
            LOGGER.info("Obtain Connection");
            connection = connectionFactory.createConnection();
            LOGGER.info(connection.toString());
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Message jmsMessage = session.createTextMessage(messageText);
            jmsMessage.setStringProperty("TYPE", type);
            LOGGER.info(destinationQueue.toString());
            MessageProducer producer = session.createProducer(destinationQueue);
            producer.send(jmsMessage);
            result = jmsMessage.getJMSMessageID();
        } catch (JMSException e) {
            e.printStackTrace();
            result = e.getMessage();
        }finally{
            LOGGER.info("RESULT ["+result+"]");
            try {
                connection.close();
            } catch (JMSException e) {
                LOGGER.warning("Error when try to close connection");
                //ignored
            }
        }
        return result;
    }
}