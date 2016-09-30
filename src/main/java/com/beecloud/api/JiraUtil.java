package com.beecloud.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Field;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.util.concurrent.Promise;
import com.beecloud.auth.Auth;
import com.beecloud.auth.AuthUtil;
import com.beecloud.constansts.CustomFieldsName;
import com.beecloud.constansts.IssueTypes;
import com.beecloud.excel.ExcelParser;
import com.beecloud.model.TestCase;
import com.beecloud.model.TestStep;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class JiraUtil {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private  JiraRestClient restClient = null;
	public JiraUtil(JiraRestClient restClient){
		this.restClient = restClient;
	}
	
	 /**
     * 获取项目列表
     * @param restClient
     * @return
     * @throws Exception
     */
    public  List<String>  getAllProject() throws Exception{
    	List<String> list  = new ArrayList<String>();
    	Iterator<BasicProject> projects = restClient.getProjectClient().getAllProjects().get().iterator();
    	while(projects.hasNext()){
    		BasicProject project = projects.next();
    		list.add(project.getName());
    	}
    	return list;
    }
    
	 /**
     * 添加测试用例
     * @param testCase
     * @throws Exception
     */
    public void AddIssue(String projectName,TestCase testCase) throws Exception{
    	if(testCase.getReporter()==null||testCase.getCaseName()==null||
    			projectName==null||testCase.getPriorityName()==null) {
    		throw new Exception("必填字段不能为空");
    	}
    	String projectKey = this.getProjectKey(projectName);
    	long caseType = this.getTestTypeByProjectName(projectName);
    	String sprntId = this.getFiledIdByName(CustomFieldsName.SPRINT.getCode());
    	long priorityId = this.getPrioritieBykey(testCase.getPriorityName());
    	String reporter = testCase.getReporter();
    	
    	IssueInputBuilder builder = new IssueInputBuilder(projectKey,
    			caseType, testCase.getCaseName());
    	if(testCase.getDescription()!=null){	//允许描述为空
    		builder.setDescription(testCase.getDescription());
        }
        if(testCase.getSprint()!=null){	//允许Sprint为空
        	builder.setFieldValue(sprntId,testCase.getSprint());
        }
        if(testCase.getComponent()!=null){	//允许模块为空
        	builder.setComponentsNames(Arrays.asList(testCase.getComponent()));
        }
        if(testCase.getLabels()!=null){	//允许标签为空
        	builder.setFieldInput(new FieldInput(IssueFieldId.LABELS_FIELD,Arrays.asList(testCase.getLabels())));
        }
        if(testCase.getAssignee()!=null) {	//允许经办人为空
        	builder.setAssigneeName(testCase.getAssignee());
        }
        builder.setPriorityId(priorityId);
        builder.setReporterName(reporter);
        
        IssueInput input = builder.build();
        IssueRestClient client = restClient.getIssueClient();
        BasicIssue issue = client.createIssue(input).claim();
        Issue actual = client.getIssue(issue.getKey()).claim();
        if(testCase.getSteps()!=null) {	    //允许测试步骤为空
        	addSteps(actual,testCase.getSteps());
        }
        if(testCase.getCommit()!=null) {	//允许备注为空
        	client.addComment(actual.getCommentsUri(),Comment.valueOf(testCase.getCommit()));
        }
    } 
	
	/**
	 * 通过名称获取项目key
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private String getProjectKey(String name)throws Exception{
		return this.getBasicProjectByName(name).getKey();
	}
	
	private  BasicProject getBasicProjectByName(String name)throws Exception{
		Iterator<BasicProject> projects = restClient.getProjectClient().getAllProjects().get().iterator();
    	while(projects.hasNext()){
    		BasicProject project = projects.next();
    		if(name.equals(project.getName())){
    			return project;
    		}
    	}
    	return null;
	}
	
	
	/**
	 * 获取项目对应的测试类型ID
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private Long getTestTypeByProjectName(String name)throws Exception{
		BasicProject basicProject = getBasicProjectByName(name);
		Project project = restClient.getProjectClient()
                .getProject(basicProject.getKey()).get();
		Iterator<IssueType> types = project.getIssueTypes().iterator();
		while(types.hasNext()){
			IssueType type = types.next();
			if(IssueTypes.TESTCASE.getCode().equals(type.getName())){
				return type.getId();
			}
		}
		return null;
	}
	
	public List<String> getComponentsByProjectName(String name)throws Exception{
		List<String> list  = new ArrayList<String>();
		BasicProject basicProject = getBasicProjectByName(name);
		Project project = restClient.getProjectClient()
                .getProject(basicProject.getKey()).get();
		Iterator<BasicComponent> iterator = project.getComponents().iterator();
		while(iterator.hasNext()){
			list.add(iterator.toString());
		}
		return list;
	}
	
    
	  /**
	   * 获取优先级
	   * @param restClient
	   * @return
	   * @throws Exception
	   */
    private Long getPrioritieBykey(String key) throws Exception{
	    	Promise<Iterable<Priority>> priorotys = restClient.getMetadataClient().getPriorities();
			Iterator<Priority> iterator= priorotys.get().iterator();
			while (iterator.hasNext()) {
				Priority priority = iterator.next();
				if(key.equals(priority.getName())){
					return priority.getId();
				}
			}
			return null;
	    }
	  
	  /**
	     * 获取字段名称对应的ID
	     * @param restClient
	     * @return
	     * @throws Exception
	     */
	    private  String getFiledIdByName(String name) throws Exception {
	    	Iterator<Field> Fileds = restClient.getMetadataClient().getFields().get().iterator();
	    	while(Fileds.hasNext()){
	    		Field field = Fileds.next();
	    		if(name.equals(field.getName())){
	    			return field.getId();
	    		}
	    	}
	    	return null;
	    }
	     
	    /**
	     * 添加测试步骤
	     * @param issue
	     * @param steps
	     * @throws Exception 
	     */
	    private  void addSteps(Issue issue,List<TestStep> steps) throws Exception{
	    	Auth auth = AuthUtil.getAuth();
	    	String path = auth.getBaseUrl()+"/rest/synapse/1.0/testStep/addTestStep";
	    	Client client = Client.create();	
			client.addFilter(new HTTPBasicAuthFilter(auth.getUserName(), auth.getPassWord()));
			WebResource webResource = client.resource(path);
			long tcId = issue.getId();
			Gson gson = new Gson();
			for(TestStep step:steps){
				step.setTcId(tcId);
				logger.info(gson.toJson(step));
				webResource.type("application/json").post(ClientResponse.class, gson.toJson(step));
			}
	    }
	    
	    public static void main(String[] args) {
	    	JiraRestClient restClient = null;
			try {
				restClient = JiraRestClientFactory.CreateJiraRestClient();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			JiraUtil ju = new JiraUtil(restClient);
			ExcelParser ep = null;
			try {
				ep = new ExcelParser("TestCase.xls");
				List<TestCase> list = ep.transRowsToCase("Test Case Library");
			
				for(TestCase testCase:list) {
					ju.AddIssue("Test Case Library",testCase);
					
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
//	    	String[] steps = new String[3];
//	    	steps[0] = "{\"step\":\"1\",\"data\":\"1\",\"result\":\"1\"}";
//	    	steps[1] = "{\"step\":\"2\",\"data\":\"2\",\"result\":\"2\"}";
//	    	steps[2] = "{\"step\":\"3\",\"data\":\"3\",\"result\":\"3\"}";
//	    	TestCase testCase = new TestCase();
//	    	testCase.setAssignee("admin");
//	    	testCase.setCaseName("AutoAdd");
//	    	testCase.setCommit("commit test");
//	    	//testCase.setComponent(new String[]{"前端"});
//	    	testCase.setDescription("自动添加");
//	    	testCase.setLabels(new String[]{"label1","label2"});
//	    	testCase.setPriorityName("High");
//	    	testCase.setReporter("admin");
//	    	testCase.setSteps(steps);
//	    	JiraRestClient jiraClient = null;
//				 try {
//					jiraClient = JiraRestClientFactory.CreateJiraRestClient();
//				} catch (Exception e1) {
//					System.out.println(e1.getLocalizedMessage());
//				}
//			try {
//				new JiraUtil(jiraClient).AddIssue("abc",testCase);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
}
