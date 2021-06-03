

package com.infinite.tm.model;

public class TimeMgmtWeekday {

		private String projectName;
	    private Double hours;
	    private String date;
	    private String day;
	    private String hoursPerDay;
		
	
		
		public TimeMgmtWeekday(String projectName, String date, String day, String hoursPerDay) {
			
			this.projectName = projectName;
			this.date = date;
			this.day = day;
			this.hoursPerDay = hoursPerDay;
		}

		public TimeMgmtWeekday(String projectName, Double hours, String date) {
			
			this.projectName = projectName;
			this.hours = hours;
			this.date = date;
		}
		public TimeMgmtWeekday() {
			// TODO Auto-generated constructor stub
		}


		
		public String getDay() {
			return day;
		}
		public String getHoursPerDay() {
			return hoursPerDay;
		}
		public void setHoursPerDay(String hoursPerDay) {
			this.hoursPerDay = hoursPerDay;
		}


		public void setDay(String day) {
			this.day = day;
		}


		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public Double getHours() {
			return hours;
		}

		public void setHours(Double hours) {
			this.hours = hours;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	    
	    
}
