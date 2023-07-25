package com.api.sociallogin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.api.sociallogin.service.ApiService;
import com.api.sociallogin.util.ApiKey;
import com.api.sociallogin.util.ApiUtil;
import com.google.gson.JsonObject;

@Service
public class NaverService extends ApiService {

	private static NaverService instance;
	public ApiUtil apiUtil = new ApiUtil();
	public ApiKey apiKey = new ApiKey();

	private String clientSecret;

	public static NaverService getInstance() {
		if (instance == null)
			instance = new NaverService();
		return instance;
	}

	public NaverService() {
		clientId = apiKey.getNaverClientId();
		clientSecret = apiKey.getNaverClientSecret();
		redirectURI = apiKey.getNaverRedirectURI();
		state = apiUtil.generateRandomState();
	}

	@Override
	public String getLoginUrl() {
		return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" +
			clientId + "&redirect_uri=" + redirectURI + "&state=" + state;
	}

	@Override
	public String getAccessTokenUrl(HttpServletRequest request) {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" +
			clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectURI + "&code=" + code + "&state="
			+ state;
	}

	@Override
	public HashMap<String, String> getUserInfo(String accessToken) {
		HashMap<String, String> userInfo = new HashMap<>();

		String header = "Bearer " + accessToken;
		String profileUrl = "https://openapi.naver.com/v1/nid/me";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);

		String responseBody = apiUtil.get(profileUrl, requestHeaders);
		// response 전체를 parsing
		JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);
		// response.response 정보만 parsing
		JsonObject responseData = profileObject.get("response").getAsJsonObject();

		userInfo.put("token", responseData.get("id").getAsString());
		userInfo.put("email", responseData.get("email").getAsString());

		return userInfo;
	}
}
