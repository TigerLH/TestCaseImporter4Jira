package com.beecloud.auth;

import java.net.URI;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraRestClientFactory {
	public static JiraRestClient CreateJiraRestClient() throws Exception{
		Auth auth = AuthUtil.getAuth();
        AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		JiraRestClient restClient = (JiraRestClient) factory
			        .createWithBasicHttpAuthentication(new URI(auth.getBaseUrl()), auth.getUserName(),
			                auth.getPassWord());
        return restClient;
    }
}
