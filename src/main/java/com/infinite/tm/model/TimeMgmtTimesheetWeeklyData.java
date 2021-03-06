package com.infinite.tm.model;

public class TimeMgmtTimesheetWeeklyData {
	
	private String project;
	private double monday;
	private double tuesday;
	private double wednesday;
	private double thursday;
	private double friday;
	private int status;
	
	public TimeMgmtTimesheetWeeklyData()
	{
		
	}

	public TimeMgmtTimesheetWeeklyData(String project, double monday, double tuesday, double wednesday, double thursday,
			double friday, int status) {
		super();
		this.project = project;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.status = status;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public double getMonday() {
		return monday;
	}

	public void setMonday(double monday) {
		this.monday = monday;
	}

	public double getTuesday() {
		return tuesday;
	}

	public void setTuesday(double tuesday) {
		this.tuesday = tuesday;
	}

	public double getWednesday() {
		return wednesday;
	}

	public void setWednesday(double wednesday) {
		this.wednesday = wednesday;
	}

	public double getThursday() {
		return thursday;
	}

	public void setThursday(double thursday) {
		this.thursday = thursday;
	}

	public double getFriday() {
		return friday;
	}

	public void setFriday(double friday) {
		this.friday = friday;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
