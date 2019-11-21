package org.apache.camel.wsaccount.service;

import org.apache.camel.wsaccount.model.Account;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete


public interface AccountRepository extends CrudRepository<Account, Integer> {

}

