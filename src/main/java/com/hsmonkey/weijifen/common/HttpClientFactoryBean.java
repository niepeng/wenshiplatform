package com.hsmonkey.weijifen.common;
//package com.yubaoseo.itemmaster.common;
//import org.apache.commons.httpclient.Credentials;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
//import org.apache.commons.httpclient.UsernamePasswordCredentials;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.InitializingBean;
//
//
//public class HttpClientFactoryBean implements InitializingBean, FactoryBean {
//	private HttpClient httpClient;
//	private String username;
//	private String password;
//	private String authenticationHost;
//
//	public HttpClient getHttpClient() {
//		return httpClient;
//	}
//
//	public void setHttpClient(HttpClient httpClient) {
//		this.httpClient = httpClient;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getAuthenticationHost() {
//		return authenticationHost;
//	}
//
//	public void setAuthenticationHost(String authenticationHost) {
//		this.authenticationHost = authenticationHost;
//	}
//
//	public void afterPropertiesSet() throws Exception {
//		// 构造HttpClient对象
//		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
////		httpClient.getParams().setParameter("username", username);
////		httpClient.getParams().setParameter("password", password);
////		System.out.println("afterPropertiesSet->username=" + username + ", password=" + password);
//
//		httpClient.getState().setAuthenticationPreemptive(true);
//		Credentials credentials = new UsernamePasswordCredentials(username, password);
//		httpClient.getState().setCredentials(null, authenticationHost, credentials);
//
//	}
//
//	public Object getObject() throws Exception {
//		return httpClient;
//	}
//
//	public Class getObjectType() {
//		return HttpClient.class;
//	}
//
//	public boolean isSingleton() {
//		return true;
//	}
//
//}