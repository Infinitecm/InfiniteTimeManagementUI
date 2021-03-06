package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class TimeMgmtTeamData {
	
	private String empId;
	@Id
	private String emailId;
	private String firstName;
	private String lastName;
	private String role;
	private String rate;
	private String vendor;
	private String vendorContact;
	private String createDate;
	private String manager;
	private String location;
	private String type;
	private String billable;
	private List<String> accounts;
	private List<String> projects;
	
	public TimeMgmtTeamData()
	{
		
	}

	public TimeMgmtTeamData(String empId, String emailId, String firstName, String lastName, String role, String rate,
			String vendor, String vendorContact, String createDate, String manager, String location, String type,
			String billable, List<String> accounts, List<String> projects) {
		super();
		this.empId = empId;
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.rate = rate;
		this.vendor = vendor;
		this.vendorContact = vendorContact;
		this.createDate = createDate;
		this.manager = manager;
		this.location = location;
		this.type = type;
		this.billable = billable;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
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

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
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
