package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LeadDetail")
public class LeadDetailData {
	private String empId;
	private String firstName;
	private String lastName;
	@Id
	private String emailId;
	private String lead;
	private String role;
	private String rate;
	private String vendor;
	private String vendorContact;
	private String createDate;
	private String location;
	private String type;
	private String billable;
	private List<String> accounts;
	private List<String> projects;
	private List<AccountInformation> accountInfo;
	private List<UserDetailData> members;

	
	

	 public LeadDetailData()
	 {
		 
	 }




	public LeadDetailData(String empId, String firstName, String lastName, String emailId, String lead, String role,
			String rate, String vendor, String vendorContact, String createDate, String location, String type,
			String billable, List<String> accounts, List<String> projects, List<AccountInformation> accountInfo,
			List<UserDetailData> members) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.lead = lead;
		this.role = role;
		this.rate = rate;
		this.vendor = vendor;
		this.vendorContact = vendorContact;
		this.createDate = createDate;
		this.location = location;
		this.type = type;
		this.billable = billable;
		this.accounts = accounts;
		this.projects = projects;
		this.accountInfo = accountInfo;
		this.members = members;
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




	public String getLead() {
		return lead;
	}




	public void setLead(String lead) {
		this.lead = lead;
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




	public List<AccountInformation> getAccountInfo() {
		return accountInfo;
	}




	public void setAccountInfo(List<AccountInformation> accountInfo) {
		this.accountInfo = accountInfo;
	}




	public List<UserDetailData> getMembers() {
		return members;
	}




	public void setMembers(List<UserDetailData> members) {
		this.members = members;
	}




	
}
