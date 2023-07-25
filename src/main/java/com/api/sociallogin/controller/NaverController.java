package com.api.sociallogin.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.api.sociallogin.service.impl.NaverService;
import com.api.sociallogin.util.ApiUtil;

@Controller
@RequestMapping("/login/naver")
public class NaverController {

	NaverService naverService = NaverService.getInstance();
	public ApiUtil apiUtil = new ApiUtil();

	@GetMapping
	public String naverLogin() {
		return "redirect:" + naverService.getLoginUrl();
	}

	@GetMapping("/callback")
	public ModelAndView getNaverInfo(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("naver");

		String accessTokenUrl = naverService.getAccessTokenUrl(request);
		String accessToken = apiUtil.getAccessToken(accessTokenUrl);
		HashMap<String, String> userInfo = naverService.getUserInfo(accessToken);

		modelAndView.addObject("email", userInfo.get("email"));
		modelAndView.addObject("token", userInfo.get("token"));
		System.out.println("네이버: " + userInfo.get("email") + " " + userInfo.get("token"));

		return modelAndView;
	}
}
