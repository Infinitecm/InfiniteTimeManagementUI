package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class TimeMgmtManagerData {
	
	private String empId;
	@Id
	private String emailId;
	private String firstName;
	private String lastName;
	private String role;
	private String createDate;
	private String location;
	private String type;
	private List<String> accounts;
	private List<String> projects;
	
	public TimeMgmtManagerData()
	{
		
	}

	public TimeMgmtManagerData(String empId, String emailId, String firstName, String lastName, String role,
			String createDate, String location, String type, List<String> accounts, List<String> projects) {
		super();
		this.empId = empId;
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.createDate = createDate;
		this.location = location;
		this.type = type;
		this.accounts = accounts;
		this.projects = projects;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	
	

}
