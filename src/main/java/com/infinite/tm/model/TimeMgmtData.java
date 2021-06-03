package com.infinite.tm.model;

import java.util.List;

public class TimeMgmtData {

	private String status;
	private String statusCode;
	private List<TimeMgmtManagerData> managerList;
	private List<TimeMgmtTeamData> tlList;
	private List<TimeMgmtMemberData> memberList;
	
	public TimeMgmtData()
	{
		
	}

	public TimeMgmtData(String status, String statusCode, List<TimeMgmtManagerData> managerList,
			List<TimeMgmtTeamData> tlList, List<TimeMgmtMemberData> memberList) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.managerList = managerList;
		this.tlList = tlList;
		this.memberList = memberList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public List<TimeMgmtManagerData> getManagerList() {
		return managerList;
	}

	public void setManagerList(List<TimeMgmtManagerData> managerList) {
		this.managerList = managerList;
	}

	public List<TimeMgmtTeamData> getTlList() {
		return tlList;
	}

	public void setTlList(List<TimeMgmtTeamData> tlList) {
		this.tlList = tlList;
	}

	public List<TimeMgmtMemberData> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<TimeMgmtMemberData> memberList) {
		this.memberList = memberList;
	}
	
	
	
}
