package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtTimesheetData {
	
	private TimeMgmtUserDataVO userData;
	private List<TimeMgmtTimesheetWeeklyData> projects;
	private List<TimeMgmtDates> holiday;
	
	public TimeMgmtUserDataVO getUserData() {
		return userData;
	}
	public void setUserData(TimeMgmtUserDataVO userData) {
		this.userData = userData;
	}
	public List<TimeMgmtTimesheetWeeklyData> getProjects() {
		return projects;
	}
	public void setProjects(List<TimeMgmtTimesheetWeeklyData> projects) {
		this.projects = projects;
	}
	public List<TimeMgmtDates> getHoliday() {
		return holiday;
	}
	public void setHoliday(List<TimeMgmtDates> holiday) {
		this.holiday = holiday;
	}
	
	
	
	

}
