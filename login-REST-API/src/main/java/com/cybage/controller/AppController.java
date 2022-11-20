package com.cybage.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cybage.model.User;
import com.cybage.repository.UserRepository;
import com.cybage.service.UserServices;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserServices service;

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());

		return "signup_form";
	}

	/*
	 * @PostMapping("/process_register") public String processRegister(User user) {
	 * BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); String
	 * encodedPassword = passwordEncoder.encode(user.getPassword());
	 * user.setPassword(encodedPassword);
	 * 
	 * userRepo.save(user);
	 * 
	 * return "register_success"; }
	 */
	@PostMapping("/process_register")
	public String processRegister(User user, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		service.register(user, getSiteURL(request));
		return "register_success";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code, Model model) {
		/*
		 * boolean verified = service.verify(code); String pageTitle = verified ?
		 * "Verification Succeeded!" : "Verification Failed";
		 * model.addAttribute("pageTitle", pageTitle); return "register/" + (verified ?
		 * "verify_success" : "verify_fail");
		 */
		if (service.verify(code) ) {
			return "verify_success";
		} else {
			return "verify_fail";
		}

	}
}
