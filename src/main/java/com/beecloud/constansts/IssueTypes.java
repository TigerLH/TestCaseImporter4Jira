/**
 * 
 */
package com.beecloud.constansts;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年9月30日 上午9:40:28
 * @version v1.0
 */
public enum IssueTypes {
	TESTCASE("测试用例");
	private String code;
	private IssueTypes(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
