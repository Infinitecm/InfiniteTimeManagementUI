package com.infinite.tm.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infinite.tm.model.TimeMgmtTimesheetEntry;

public interface TimeMgmtTimeSheetEntryRepository extends MongoRepository<TimeMgmtTimesheetEntry, String> {

	TimeMgmtTimesheetEntry findByEmail(String email);

	


}
