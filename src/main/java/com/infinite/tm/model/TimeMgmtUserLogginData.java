package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class TimeMgmtUserLogginData {
	private String empId;
	private String firstName;
	private String lastName;
	@Id
	private String emailId;  
	private String role;
	private String createDate;
	private String location;
	private List<AccountInformation> accountInfo;
	private String status;
	private int StatusCode;
	
	public TimeMgmtUserLogginData()
	{
		
	}

	public TimeMgmtUserLogginData(String empId, String firstName, String lastName, String emailId, String role,
			 String createDate, String location, List<AccountInformation> accountInfo, String status,
			int statusCode) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.role = role;
		this.createDate = createDate;
		this.location = location;
		this.accountInfo = accountInfo;
		this.status = status;
		StatusCode = statusCode;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public List<AccountInformation> getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(List<AccountInformation> accountInfo) {
		this.accountInfo = accountInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	
	

}
