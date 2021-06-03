package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class TimeMgmtMemberData {
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
	private String lead;
	private String submitCount;
	private String location;
	private String type;
	private String billable;
	private String approvedFlag;

	private List<String> accounts;
	private List<String> projects;
	
	public TimeMgmtMemberData()
	{
		
	}

	public TimeMgmtMemberData(String empId, String emailId, String firstName, String lastName, String role, String rate,
			String vendor, String vendorContact, String createDate, String lead, String submitCount, String location,
			String type, String billable, String approvedFlag, List<String> accounts, List<String> projects) {
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
		this.lead = lead;
		this.submitCount = submitCount;
		this.location = location;
		this.type = type;
		this.billable = billable;
		this.approvedFlag = approvedFlag;
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

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public String getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(String submitCount) {
		this.submitCount = submitCount;
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

	public String getApprovedFlag() {
		return approvedFlag;
	}

	public void setApprovedFlag(String approvedFlag) {
		this.approvedFlag = approvedFlag;
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
