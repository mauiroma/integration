package org.apache.camel.wsaccount.service;


public interface AccountService {

    public void ping(String ping);

    public AccountRespone findAccount(AccountRequest request) throws AccountException;
    
}
