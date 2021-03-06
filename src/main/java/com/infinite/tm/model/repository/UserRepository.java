package com.infinite.tm.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infinite.tm.model.User;

public interface UserRepository extends MongoRepository<User,String>{

	User findByEmailId(String emailId);


}
