/**
 * 
 */
package com.beecloud.model;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年9月30日 上午9:53:31
 * @version v1.0
 */
public class TestStep {
	/**
	 * 关联的用例Id
	 */
	private Long tcId;
	/**
	 * 测试步骤
	 */
	private String step;
	/**
	 * 测试数据
	 */
	private String stepData;
	/**
	 * 期望结果
	 */
	private String expectedResult;
	public Long getTcId() {
		return tcId;
	}
	public void setTcId(Long tcId) {
		this.tcId = tcId;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getStepData() {
		return stepData;
	}
	public void setStepData(String stepData) {
		this.stepData = stepData;
	}
	public String getExpectedResult() {
		return expectedResult;
	}
	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
	@Override
	public String toString() {
		return "TestStep [tcId=" + tcId + ", step=" + step + ", stepData="
				+ stepData + ", expectedResult=" + expectedResult + "]";
	}
	
}
