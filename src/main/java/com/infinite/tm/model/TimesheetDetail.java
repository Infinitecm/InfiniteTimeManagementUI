package com.infinite.tm.model;

public class TimesheetDetail {
	
	private String email;
	private String endDate;
	private String leadEmail;
	private String role;
	private String startDate;
	private int weekNo;
	private String approverRole;
	private String location;
	public TimesheetDetail(String endDate, String startDate, String location) {
		super();
		this.endDate = endDate;
		this.startDate = startDate;
		this.location = location;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getApproverRole() {
		return approverRole;
	}


	public void setApproverRole(String approverRole) {
		this.approverRole = approverRole;
	}


	public TimesheetDetail()
	{
		
	}
	
	
	public TimesheetDetail(String email, String endDate, String startDate, int weekNo, String approverRole) {
		super();
		this.email = email;
		this.endDate = endDate;
		this.startDate = startDate;
		this.weekNo = weekNo;
		this.approverRole = approverRole;
	}


	public TimesheetDetail(String email, String endDate, String startDate, int weekNo) {
		super();
		this.email = email;
		this.endDate = endDate;
		this.startDate = startDate;
		this.weekNo = weekNo;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getLeadEmail() {
		return leadEmail;
	}
	public void setLeadEmail(String leadEmail) {
		this.leadEmail = leadEmail;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public int getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}
	
	

}
