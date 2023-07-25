package com.api.logintest.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.api.logintest.service.ApiService;
import com.api.logintest.util.ApiKey;
import com.api.logintest.util.ApiUtil;
import com.google.gson.JsonObject;

@Service
public class KakaoService extends ApiService {

	private static KakaoService instance;

	public ApiUtil apiUtil = new ApiUtil();
	public ApiKey apiKey = new ApiKey();

	public static KakaoService getInstance() {
		if (instance == null)
			instance = new KakaoService();
		return instance;
	}

	public KakaoService() {
		clientId = apiKey.getKakaoClientId();
		redirectURI = apiKey.getKakaoRedirectURI();
		state = apiUtil.generateRandomState();
	}

	@Override
	public String getLoginUrl() {
		return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectURI
			+ "&response_type=code&scope=account_email&state=" + state;
	}

	@Override
	public String getAccessTokenUrl(HttpServletRequest request) {
		String code = request.getParameter("code");
		return "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=" + clientId
			+ "&redirect_uri=" + redirectURI + "&code=" + code;
	}

	@Override
	public HashMap<String, String> getUserInfo(String accessToken) {
		HashMap<String, String> userInfo = new HashMap<>();

		String header = "Bearer " + accessToken;
		String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);

		String responseBody = apiUtil.get(userInfoUrl, requestHeaders);
		// response 전체를 parsing
		JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);
		// response.response 정보만 parsing
		JsonObject responseData = profileObject.get("kakao_account").getAsJsonObject();

		userInfo.put("token", accessToken);
		userInfo.put("email", responseData.get("email").getAsString());

		return userInfo;
	}
}
