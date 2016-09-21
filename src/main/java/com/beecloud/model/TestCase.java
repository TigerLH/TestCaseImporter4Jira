/**
 * 
 */
package com.beecloud.model;

import java.util.Arrays;
import java.util.List;


public class TestCase {
	/**
	 * 用例名称
	 */
	private String caseName;
	/**
	 * 报告人
	 */
	private String reporter;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 优先级
	 */
	private String priorityName;
	/**
	 * 模块
	 */
	private String[] component;
	/**
	 * 标签
	 */
	private String[] labels;
	/**
	 * 经办人
	 */
	private String assignee;
	/**
	 * Sprint名称
	 */
	private String sprint;
	/**
	 * 测试步骤
	 */
	private String[] steps;
	/**
	 * 备注信息
	 */
	private String commit;
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public String[] getComponent() {
		return component;
	}
	public void setComponent(String[] component) {
		this.component = component;
	}
	public String[] getLabels() {
		return labels;
	}
	public void setLabels(String[] labels) {
		this.labels = labels;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getSprint() {
		return sprint;
	}
	public void setSprint(String sprint) {
		this.sprint = sprint;
	}
	public String[] getSteps() {
		return steps;
	}
	public void setSteps(String[] steps) {
		this.steps = steps;
	}
	public String getCommit() {
		return commit;
	}
	public void setCommit(String commit) {
		this.commit = commit;
	}
	@Override
	public String toString() {
		return "TestCase [caseName=" + caseName + ", reporter=" + reporter + ", description=" + description
				+ ", priorityName=" + priorityName + ", component=" + Arrays.toString(component) + ", labels="
				+ Arrays.toString(labels) + ", assignee=" + assignee + ", sprint=" + sprint + ", steps="
				+ Arrays.toString(steps) + ", commit=" + commit + "]";
	}
	
}
