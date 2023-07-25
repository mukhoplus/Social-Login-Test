package com.api.sociallogin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.api.sociallogin.service.ApiService;
import com.api.sociallogin.util.ApiKey;
import com.api.sociallogin.util.ApiUtil;
import com.google.gson.JsonObject;

public class GoogleService extends ApiService {

	private static GoogleService instance;
	public ApiUtil apiUtil = new ApiUtil();
	public ApiKey apiKey = new ApiKey();

	public static GoogleService getInstance() {
		if (instance == null)
			instance = new GoogleService();
		return instance;
	}

	public GoogleService() {
		clientId = apiKey.getGoogleClientId();
		redirectURI = apiKey.getGoogleRedirectURI();
		state = apiUtil.generateRandomState();
	}

	@Override
	public String getLoginUrl() {
		return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId + "&redirect_uri=" + redirectURI
			+ "&response_type=code&scope=email";
	}

	@Override
	public String getAccessTokenUrl(HttpServletRequest request) {
		String code = request.getParameter("code");
		return "https://oauth2.googleapis.com/token?code=" + code + "&client_id=" + clientId + "&client_secret="
			+ apiKey.getGoogleClientSecret() + "&redirect_uri=" + redirectURI + "&grant_type=authorization_code";
	}

	@Override
	public HashMap<String, String> getUserInfo(String accessToken) {
		HashMap<String, String> userInfo = new HashMap<>();

		String header = "Bearer " + accessToken;
		String profileUrl = "https://www.googleapis.com/userinfo/v2/me";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);

		String responseBody = apiUtil.get(profileUrl, requestHeaders);
		// response 전체를 parsing
		JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);

		userInfo.put("token", accessToken);
		userInfo.put("email", profileObject.get("email").getAsString());

		return userInfo;
	}
}
