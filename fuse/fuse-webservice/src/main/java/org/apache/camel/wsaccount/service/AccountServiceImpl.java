package org.apache.camel.wsaccount.service;

import org.apache.camel.wsaccount.model.Account;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService(targetNamespace = "http://apache.org/account",
        portName = "AccountPort",
        serviceName = "AccountService"
)
public class AccountServiceImpl implements AccountService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AccountRepository accountRepository;

    @WebMethod
    public void ping(@WebParam(name = "ping") String ping) {
        System.out.println("Received [" + ping + "] current time " + System.currentTimeMillis());
    }

    @WebMethod
    @WebResult(name = "accountResponse", partName = "response")
    public AccountRespone findAccount(
            @WebParam(partName = "request", name = "accountRequest")
                    AccountRequest request) throws AccountException {

        if (request.getId() == 10) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (request.getId() == 20) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (request.getId() == 1000) {
            throw new AccountException("Error reading account");
        }

        Account ac = accountRepository.findOne(request.getId());

        //Account account = new Account(request.getId(), "Jhon-" + request.getId(), "Brown");
        //System.out.println("Sending back " + account.getName() + ", " + account.getSurname());
        return new AccountRespone(ac);
    }

    @WebMethod
    @WebResult(name = "accountlist", partName = "response")
    public AccountList findAll() throws AccountException {

        accountRepository.save(new Account(5,"Lando", "Carlissia"));
        Iterable<Account> ac = accountRepository.findAll();

        //Account account = new Account(request.getId(), "Jhon-" + request.getId(), "Brown");
        //System.out.println("Sending back " + account.getName() + ", " + account.getSurname());
        return new AccountList(ac);
    }


}
