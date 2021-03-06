package com.infinite.tm.model;


public class TimeMgmtTimesheetWeekStatusVO {
	
	private int weekNo;
	private int status;
	private String startDate;
	private String endDate;
	
	public TimeMgmtTimesheetWeekStatusVO()
	{
		
	}

	

	public TimeMgmtTimesheetWeekStatusVO(int weekNo, int status, String startDate, String endDate) {
		super();
		this.weekNo = weekNo;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	
	

}
