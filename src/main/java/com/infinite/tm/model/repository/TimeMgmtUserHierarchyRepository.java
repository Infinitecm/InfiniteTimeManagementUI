package com.infinite.tm.model.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.infinite.tm.model.TimeMgmtUserHierarchy;
import com.infinite.tm.model.repository.custom.TimeMgmtUserHierarchyRepositoryCustom;

public interface TimeMgmtUserHierarchyRepository extends MongoRepository<TimeMgmtUserHierarchy,Long>, TimeMgmtUserHierarchyRepositoryCustom{

	List<TimeMgmtUserHierarchy> findByEmailId(String emailId);
	
}
