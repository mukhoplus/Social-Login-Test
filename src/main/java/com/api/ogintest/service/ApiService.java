package com.api.ogintest.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public abstract class ApiService {

	protected String clientId;
	protected String redirectURI;
	protected String state;

	public abstract String getLoginUrl();

	public abstract String getAccessTokenUrl(HttpServletRequest request);

	public abstract HashMap<String, String> getUserInfo(String accessToken);
}
