package com.infinite.tm.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infinite.tm.model.HolidayList;

public interface HolidayListRepository extends MongoRepository<HolidayList, String> {

}
