package com.beecloud.auth;

public class Auth {
	/**
	 * Base Url for Jira 
	 */
	private String baseUrl;
	/**
	 * Jira userName
	 */
	private String userName;
	/**
	 * Jira PassWord
	 */
	private String passWord;
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Override
	public String toString() {
		return "Auth [baseUrl=" + baseUrl + ", userName=" + userName + ", passWord=" + passWord + "]";
	}
	
}
