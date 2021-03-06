package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtSubmitDataVO 
{

	private String email;
	private int weekNo;
	private TimeMgmtDays days;
	private int status;
	private String startDate;
	private String endDate;
	private String submittedBy;
	private String submittedByRole;
	private List<String> leave;
	
	public TimeMgmtSubmitDataVO()
	{
		
	}

	public TimeMgmtSubmitDataVO(String email, int weekNo, TimeMgmtDays days, int status, String startDate,
			String endDate, String submittedBy, String submittedByRole, List<String> leave) {
		super();
		this.email = email;
		this.weekNo = weekNo;
		this.days = days;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.submittedBy = submittedBy;
		this.submittedByRole = submittedByRole;
		this.leave = leave;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}

	public TimeMgmtDays getDays() {
		return days;
	}

	public void setDays(TimeMgmtDays days) {
		this.days = days;
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

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public String getSubmittedByRole() {
		return submittedByRole;
	}

	public void setSubmittedByRole(String submittedByRole) {
		this.submittedByRole = submittedByRole;
	}

	public List<String> getLeave() {
		return leave;
	}

	public void setLeave(List<String> leave) {
		this.leave = leave;
	}

	
	
}
