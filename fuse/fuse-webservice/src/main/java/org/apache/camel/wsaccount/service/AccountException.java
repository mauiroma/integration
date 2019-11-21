package org.apache.camel.wsaccount.service;

import javax.xml.ws.WebFault;

@WebFault(name = "accountException", targetNamespace = "http://apache.org/account")
public class AccountException extends Exception{
    public AccountException(String message) {
        super(message);
    }
}
