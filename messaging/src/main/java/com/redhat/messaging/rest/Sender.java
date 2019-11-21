package com.redhat.messaging.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.messaging.amq.AMQProducerEJB;
import com.redhat.messaging.models.Account;
import com.redhat.messaging.models.Order;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("sender")
public class Sender {

    @EJB
    private AMQProducerEJB amqProducerEJB;

    @GET
    @Path("/send")
    public String send(@QueryParam("text")String text){
        return amqProducerEJB.send("NORMAL",text);
    }


    @POST
    @Path("/addAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addAccount(Account account){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return amqProducerEJB.send("ACCOUNT",mapper.writeValueAsString(account));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @POST
    @Path("/addOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addAccount(Order order){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return amqProducerEJB.send("ORDER",mapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


}
