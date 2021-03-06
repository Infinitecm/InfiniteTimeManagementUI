package com.infinite.tm.model;

public class ChangePassword {
	
	private String emailId;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	
	public ChangePassword()
	{
		
	}

	public ChangePassword(String emailId, String oldPassword, String newPassword, String confirmPassword) {
		super();
		this.emailId = emailId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	

	
}
