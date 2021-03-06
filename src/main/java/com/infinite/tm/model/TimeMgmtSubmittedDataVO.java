package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtSubmittedDataVO {
	private String firstName;
	private String lastName;
	private String email;
	private String role;
	
	private List<TimeMgmtWeeks> weeks;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public List<TimeMgmtWeeks> getWeeks() {
		return weeks;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setWeeks(List<TimeMgmtWeeks> weeks) {
		this.weeks = weeks;
	}
	public TimeMgmtSubmittedDataVO(String firstName, String lastName, String email,String role,
			List<TimeMgmtWeeks> list) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role=role;
		
		this.weeks = list;
	}
	public TimeMgmtSubmittedDataVO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
