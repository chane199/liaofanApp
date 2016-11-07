package com.liaofan_01.beans;

public class User {
	private boolean male;
	private String username;
	private String school;
	private String phone;
	private String pwd;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isMale() {
		return male;
	}

	public void setGender(boolean isMale) {
		male = isMale;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
