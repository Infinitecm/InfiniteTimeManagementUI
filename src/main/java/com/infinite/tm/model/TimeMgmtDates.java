package com.infinite.tm.model;

public class TimeMgmtDates {
	
	private String date;
	private String Reason;
	
	public TimeMgmtDates()
	{
		
	}
	
	public TimeMgmtDates(String date, String reason) {
		super();
		this.date = date;
		Reason = reason;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	
	

}
