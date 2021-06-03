package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtReportData {
	private String empId;
	private String empName;
	private String account;
	private String project;
	private String isBillable;
	private String location;
	private List<TimeMgmtWeekday> hours;
	private double totalHours;
	private List<Double> weeks;
	private String billRate;
	private String amount;
	
	private int noOfLeaves;
	private int noOfWorkingDays;
	
	public int getNoOfWorkingDays() {
		return noOfWorkingDays;
	}
	public void setNoOfWorkingDays(int noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}
	public String getIsBillable() {
		return isBillable;
	}
	public void setIsBillable(String isBillable) {
		this.isBillable = isBillable;
	}
	public int getNoOfLeaves() {
		return noOfLeaves;
	}
	public void setNoOfLeaves(int noOfLeaves) {
		this.noOfLeaves = noOfLeaves;
	}
	public double getTotalHours() {
		return totalHours;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public List<Double> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<Double> weeks) {
		this.weeks = weeks;
	}
	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public List<TimeMgmtWeekday> getHours() {
		return hours;
	}
	public void setHours(List<TimeMgmtWeekday> hours) {
		this.hours = hours;
	}

	public String getBillRate() {
		return billRate;
	}
	public void setBillRate(String billRate) {
		this.billRate = billRate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	

	public TimeMgmtReportData(String empId, String empName, String account, String project, String location,
			List<TimeMgmtWeekday> hours, double totalHours, List<Double> weeks, String billRate, String amount,
			String isBillable, int noOfLeaves, int noOfWorkingDays) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.account = account;
		this.project = project;
		this.location = location;
		this.hours = hours;
		this.totalHours = totalHours;
		this.weeks = weeks;
		this.billRate = billRate;
		this.amount = amount;
		this.isBillable = isBillable;
		this.noOfLeaves = noOfLeaves;
		this.noOfWorkingDays = noOfWorkingDays;
	}
	public TimeMgmtReportData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}








