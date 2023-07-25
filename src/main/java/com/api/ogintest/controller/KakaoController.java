package com.api.ogintest.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.ogintest.service.impl.KakaoService;
import com.api.ogintest.util.ApiUtil;

@Controller
@RequestMapping("/login/kakao")
public class KakaoController {

	KakaoService kakaoService = KakaoService.getInstance();
	ApiUtil apiUtil = new ApiUtil();

	@GetMapping
	public String kakaoLogin() {
		return "redirect:" + kakaoService.getLoginUrl();
	}

	@GetMapping("/callback")
	public String getKakaoInfo(HttpServletRequest request, Model model) {
		String accessTokenUrl = kakaoService.getAccessTokenUrl(request);
		String accessToken = apiUtil.getAccessToken(accessTokenUrl);
		HashMap<String, String> userInfo = kakaoService.getUserInfo(accessToken);

		model.addAttribute("email", userInfo.get("email"));
		model.addAttribute("token", userInfo.get("token"));
		System.out.println("카카오: " + userInfo.get("email") + " " + userInfo.get("token"));
		return "kakao";
	}
}
