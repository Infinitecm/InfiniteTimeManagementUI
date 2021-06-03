package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtDays {
	
	  	private List<TimeMgmtWeekday> monday;
	    private List<TimeMgmtWeekday> tuesday;
	    private List<TimeMgmtWeekday> wednesday;
	    private List<TimeMgmtWeekday> thursday;
	    private List<TimeMgmtWeekday> friday;
	    
	    public TimeMgmtDays() {
			// TODO Auto-generated constructor stub
		}

		public TimeMgmtDays(List<TimeMgmtWeekday> monday, List<TimeMgmtWeekday> tuesday,
				List<TimeMgmtWeekday> wednesday, List<TimeMgmtWeekday> thursday, List<TimeMgmtWeekday> friday) {
			super();
			this.monday = monday;
			this.tuesday = tuesday;
			this.wednesday = wednesday;
			this.thursday = thursday;
			this.friday = friday;
		}

		public List<TimeMgmtWeekday> getMonday() {
			return monday;
		}

		public void setMonday(List<TimeMgmtWeekday> monday) {
			this.monday = monday;
		}

		public List<TimeMgmtWeekday> getTuesday() {
			return tuesday;
		}

		public void setTuesday(List<TimeMgmtWeekday> tuesday) {
			this.tuesday = tuesday;
		}

		public List<TimeMgmtWeekday> getWednesday() {
			return wednesday;
		}

		public void setWednesday(List<TimeMgmtWeekday> wednesday) {
			this.wednesday = wednesday;
		}

		public List<TimeMgmtWeekday> getThursday() {
			return thursday;
		}

		public void setThursday(List<TimeMgmtWeekday> thursday) {
			this.thursday = thursday;
		}

		public List<TimeMgmtWeekday> getFriday() {
			return friday;
		}

		public void setFriday(List<TimeMgmtWeekday> friday) {
			this.friday = friday;
		}
	    
	    

}
