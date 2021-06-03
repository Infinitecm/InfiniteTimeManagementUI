package com.infinite.tm.model;

public class HolidaysVO {
	private String location;
	private String date;
	private String day;
	private String reason;

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public HolidaysVO(String location, String date, String day, String reason) {
		super();
		this.location = location;
		this.date = date;
		this.day = day;
		this.reason = reason;
	}


}
