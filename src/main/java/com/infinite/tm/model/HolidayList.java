package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "HolidayList")

public class HolidayList {
	
	@Id
	private String location;
	private List<TimeMgmtDates> dates;
	
	public  HolidayList()
	{
		
	}

	public HolidayList(String location, List<TimeMgmtDates> dates) {
		super();
		this.location = location;
		this.dates = dates;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<TimeMgmtDates> getDates() {
		return dates;
	}

	public void setDates(List<TimeMgmtDates> dates) {
		this.dates = dates;
	}

	
	
	
	

}
