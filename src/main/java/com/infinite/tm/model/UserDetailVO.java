package com.infinite.tm.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

public class UserDetailVO {
    @NotNull(message = "firstName cannot be null or empty.")
    @NotEmpty(message = "firstName cannot be null or empty.")
	private String firstName;
	
	@NotNull(message = "lastName cannot be null or empty. ")
	@NotEmpty(message = "lastName cannot be null or empty. ")
	private String lastName;
	
	@NotNull(message = "emailId cannot be null or empty. ")
	@NotEmpty(message = "emailId cannot be null or empty. ")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z.]+$", message = "Invalid emailId address. ")
	private String emailId;
	@Id
	private String empId;
	@NotNull(message = "role cannot be null or empty. ")
	@NotEmpty(message = "role cannot be null or empty. ")
	private String role;
	
	private String rate;
	private String vendor;
	private String vendorContact;
	
	private String lead;
	private String manager;
	private String billable;

	private String isUser;
	private String isLead;
	private List<String> accounts;
	private List<String> projectList;
	private String newLead;
	private String isManager;
	private String isToolAdmin;
	private String action;
	private String createDate;
	private String location;
	private String type;
	
	public UserDetailVO()
	{
		
	}

	public UserDetailVO(
			@NotNull(message = "firstName cannot be null or empty.") @NotEmpty(message = "firstName cannot be null or empty.") String firstName,
			@NotNull(message = "lastName cannot be null or empty. ") @NotEmpty(message = "lastName cannot be null or empty. ") String lastName,
			@NotNull(message = "emailId cannot be null or empty. ") @NotEmpty(message = "emailId cannot be null or empty. ") @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z.]+$", message = "Invalid emailId address. ") String emailId,
			String empId,
			@NotNull(message = "role cannot be null or empty. ") @NotEmpty(message = "role cannot be null or empty. ") String role,
			String rate, String vendor, String vendorContact, String lead, String manager, String billable,
			String isUser, String isLead, List<String> accounts, List<String> projectList, String newLead,
			String isManager, String isToolAdmin, String action, String createDate, String location, String type) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.empId = empId;
		this.role = role;
		this.rate = rate;
		this.vendor = vendor;
		this.vendorContact = vendorContact;
		this.lead = lead;
		this.manager = manager;
		this.billable = billable;
		this.isUser = isUser;
		this.isLead = isLead;
		this.accounts = accounts;
		this.projectList = projectList;
		this.newLead = newLead;
		this.isManager = isManager;
		this.isToolAdmin = isToolAdmin;
		this.action = action;
		this.createDate = createDate;
		this.location = location;
		this.type = type;
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

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
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

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}

	public String getIsLead() {
		return isLead;
	}

	public void setIsLead(String isLead) {
		this.isLead = isLead;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public List<String> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<String> projectList) {
		this.projectList = projectList;
	}

	public String getNewLead() {
		return newLead;
	}

	public void setNewLead(String newLead) {
		this.newLead = newLead;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getIsToolAdmin() {
		return isToolAdmin;
	}

	public void setIsToolAdmin(String isToolAdmin) {
		this.isToolAdmin = isToolAdmin;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	
	
	
}
