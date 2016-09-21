/**
 * 
 */
package com.beecloud.constansts;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年9月21日 下午4:04:39
 * @version v1.0
 */
public enum TestCaseFields {
	CASENAME("用例名称"),
	REPORTER("报告人"),
	ASSIGNEE("经办人"),
	DESCRIPTION("描述"),
	COMPONENT("模块"),
	LABELS("标签"),
	PRORITY("优先级"),
	TESTSTEP("测试步骤"),
	SPRINT("Sprint"),
	COMMIT("备注");
	private String code;
	private TestCaseFields(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
