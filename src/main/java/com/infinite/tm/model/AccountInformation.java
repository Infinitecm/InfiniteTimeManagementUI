package com.infinite.tm.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AccountInformation")

	
public class AccountInformation {
	@Id
	private String account;
	private List<String> projects;
	
	public AccountInformation(String account, List<String> projects) {
		super();
		this.account = account;
		this.projects = projects;
	}
	public AccountInformation() {
		// TODO Auto-generated constructor stub
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public List<String> getProjects() {
		return projects;
	}
	public void setProjects(List<String> projects) {
		this.projects = projects;
	}
	
	
}

