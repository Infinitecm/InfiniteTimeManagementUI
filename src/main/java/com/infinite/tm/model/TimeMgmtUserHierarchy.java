package com.infinite.tm.model;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userHierarchy")
public class TimeMgmtUserHierarchy {
	
	private String empId;
	private String firstName;
	private String lastName;
	@Id
	private String emailId;
	private String role;
	private String rate;
	private String vendor;
	private String vendorContact;
	private String createDate;
	private String location;
	private String type;
	//private String password;
	private List<LeadDetailData> teams;
	private List<String> accounts;
	private List<String> projects;
	private List<AccountInformation> accountInfo;
	
	public TimeMgmtUserHierarchy()
	{
		
	}

	public TimeMgmtUserHierarchy(String empId, String firstName, String lastName, String emailId, String role,
			String rate, String vendor, String vendorContact, String createDate, String location, String type,
			List<LeadDetailData> teams, List<String> accounts, List<String> projects,
			List<AccountInformation> accountInfo) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.role = role;
		this.rate = rate;
		this.vendor = vendor;
		this.vendorContact = vendorContact;
		this.createDate = createDate;
		this.location = location;
		this.type = type;
		this.teams = teams;
		this.accounts = accounts;
		this.projects = projects;
		this.accountInfo = accountInfo;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendorContact() {
		return vendorContact;
	}

	public void setVendorContact(String vendorContact) {
		this.vendorContact = vendorContact;
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

	public List<LeadDetailData> getTeams() {
		return teams;
	}

	public void setTeams(List<LeadDetailData> teams) {
		this.teams = teams;
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

	public List<AccountInformation> getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(List<AccountInformation> accountInfo) {
		this.accountInfo = accountInfo;
	}

	

		
	
	
}