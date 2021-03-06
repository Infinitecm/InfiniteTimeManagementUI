package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtUserDataVO {
	
	private String email;
	private String firstName;
	private String lastName;
	private String lead;
	private String manager;
	private String vendor;
	private String role;
	private List<String> accounts;
	private String leadFirstName;
	private String leadLastName;
	private String submittedBy;
	
	public TimeMgmtUserDataVO()
	{
		
	}

	public TimeMgmtUserDataVO(String email, String firstName, String lastName, String lead, String manager,
			String vendor, String role, List<String> accounts, String leadFirstName, String leadLastName,
			String submittedBy) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lead = lead;
		this.manager = manager;
		this.vendor = vendor;
		this.role = role;
		this.accounts = accounts;
		this.leadFirstName = leadFirstName;
		this.leadLastName = leadLastName;
		this.submittedBy = submittedBy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public String getLeadFirstName() {
		return leadFirstName;
	}

	public void setLeadFirstName(String leadFirstName) {
		this.leadFirstName = leadFirstName;
	}

	public String getLeadLastName() {
		return leadLastName;
	}

	public void setLeadLastName(String leadLastName) {
		this.leadLastName = leadLastName;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	
	
	

}
