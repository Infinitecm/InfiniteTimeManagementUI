package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timeSheetEntry")
public class TimeMgmtTimesheetEntry {
	 	@Id
	    private String email;
	 	private String empId;
	    private String firstName;
	    private String lastName;
	    private String leadFirstName;
	    private String leadLastName;
	    private String lead;
	    private String manager;
	    private String vendor;
	    private String role;
	    private List<TimeMgmtWeeks> weeks;
	    private List<String> accounts;
	    private List<String> projects;
		private List<AccountInformation> accountInfo;
		private String billable;
	    private String temppwd;
	    private String createDate;
	    private String location;
	    private String type;
	    
	    public TimeMgmtTimesheetEntry() {
		}

		public TimeMgmtTimesheetEntry(String email, String empId, String firstName, String lastName,
				String leadFirstName, String leadLastName, String lead, String manager, String vendor, String role,
				List<TimeMgmtWeeks> weeks, List<String> accounts, List<String> projects,
				List<AccountInformation> accountInfo, String billable, String temppwd, String createDate,
				String location, String type) {
			super();
			this.email = email;
			this.empId = empId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.leadFirstName = leadFirstName;
			this.leadLastName = leadLastName;
			this.lead = lead;
			this.manager = manager;
			this.vendor = vendor;
			this.role = role;
			this.weeks = weeks;
			this.accounts = accounts;
			this.projects = projects;
			this.accountInfo = accountInfo;
			this.billable = billable;
			this.temppwd = temppwd;
			this.createDate = createDate;
			this.location = location;
			this.type = type;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

		public List<TimeMgmtWeeks> getWeeks() {
			return weeks;
		}

		public void setWeeks(List<TimeMgmtWeeks> weeks) {
			this.weeks = weeks;
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

		public String getBillable() {
			return billable;
		}

		public void setBillable(String billable) {
			this.billable = billable;
		}

		public String getTemppwd() {
			return temppwd;
		}

		public void setTemppwd(String temppwd) {
			this.temppwd = temppwd;
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
