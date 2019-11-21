package org.apache.camel.wsaccount.service;

import org.apache.camel.wsaccount.model.Account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accounts", propOrder = {
        "accounts"
})
public class AccountList {
    private final List<Account> accounts;

    public AccountList(Iterable<Account> ac) {

        this.accounts = StreamSupport.stream(ac.spliterator(), false).collect(Collectors.toList());
    }

    public List<Account> getList() {
        return accounts;
    }
}
