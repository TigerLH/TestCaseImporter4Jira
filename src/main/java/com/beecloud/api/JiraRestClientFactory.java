package com.beecloud.api;

import java.net.URI;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.beecloud.auth.Auth;
import com.beecloud.auth.AuthUtil;

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
