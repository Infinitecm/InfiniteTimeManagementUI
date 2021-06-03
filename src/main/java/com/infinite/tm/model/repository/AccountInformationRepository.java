package com.infinite.tm.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infinite.tm.model.AccountInformation;

public interface AccountInformationRepository extends MongoRepository<AccountInformation, String> {  

}
