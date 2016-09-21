package com.beecloud.model;

public enum CustomFieldsName {
	TESTSTEP("测试详细信息"),
	SPRINT("Sprint");
	private String code;
	private CustomFieldsName(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
