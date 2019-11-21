package org.apache.camel.wsaccount.service;

import org.apache.camel.wsaccount.model.Account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountRespone", propOrder = {
        "account"
})
public class AccountRespone {

    private Account account;

    public AccountRespone() {
    }

    public AccountRespone(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
