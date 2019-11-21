package com.redhat.messaging.persistence;

import com.redhat.messaging.models.Account;
import com.redhat.messaging.models.Order;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class Registration {

    private final static Logger LOGGER = Logger.getLogger(Registration.class.toString());

    @PersistenceContext(unitName="primary")
    private EntityManager em;

    public void registerAccount(Account account) throws Exception {
        LOGGER.info("Registering Account" + account.getName());
        em.persist(account);
    }

    public void registerOrder(Order order) throws Exception {
        LOGGER.info("Registering Order" + order.getDescription());
        em.persist(order);
    }

}
