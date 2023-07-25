package com.api.sociallogin.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.sociallogin.service.impl.GoogleService;
import com.api.sociallogin.util.ApiUtil;

@Controller
@RequestMapping("/login/google")
public class GoogleController {

	GoogleService googleService = GoogleService.getInstance();
	ApiUtil apiUtil = new ApiUtil();

	@GetMapping
	public String googleLogin() {
		return "redirect:" + googleService.getLoginUrl();
	}

	@GetMapping("/callback")
	public String getGoogleInfo(HttpServletRequest request, Model model) {
		String accessTokenUrl = googleService.getAccessTokenUrl(request);
		String accessToken = apiUtil.getAccessToken(accessTokenUrl);
		HashMap<String, String> userInfo = googleService.getUserInfo(accessToken);

		model.addAttribute("email", userInfo.get("email"));
		model.addAttribute("token", userInfo.get("token"));
		System.out.println("구글: " + userInfo.get("email") + " " + userInfo.get("token"));
		return "google";
	}

}
