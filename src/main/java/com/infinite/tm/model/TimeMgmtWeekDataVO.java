

package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtWeekDataVO {
	private String role;
	private String email;
	private String startDate;
	private String endDate;
	private String account;
	private String project;
	private List<String> leave;
	
	public TimeMgmtWeekDataVO(String role, String email, String startDate, String endDate, String account,
			String project, List<String> leave) {
		super();
		this.role = role;
		this.email = email;
		this.startDate = startDate;
		this.endDate = endDate;
		this.account = account;
		this.project = project;
		this.leave = leave;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public List<String> getLeave() {
		return leave;
	}

	public void setLeave(List<String> leave) {
		this.leave = leave;
	}
	
	
	
}






