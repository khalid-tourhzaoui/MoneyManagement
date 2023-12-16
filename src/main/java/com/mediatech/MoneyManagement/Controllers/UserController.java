package com.mediatech.MoneyManagement.Controllers;


import java.security.Principal;




import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.mediatech.MoneyManagement.DTO.UserDto;
import com.mediatech.MoneyManagement.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "Auth/register";
	}
	
	
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
	    // Check if passwords match
	    if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
	        model.addAttribute("error", "Passwords do not match.");
	        return "Auth/register";
	    }

	    // Check if the email already exists
	    if (userService.existsByEmail(userDto.getEmail())) {
	        model.addAttribute("error", "Email already exists. Please choose a different email.");
	        return "Auth/register"; 
	    }
	    // Check if the cin already exists
	    if (userService.existsByCin(userDto.getCin())) {
	        model.addAttribute("error", "CIN already exists. Please choose a different cin.");
	        return "Auth/register"; 
	    }

        userDto.setRole("USER");
	    userService.save(userDto);
	    model.addAttribute("message", "Registered successfully!");
	    return "Auth/login";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("successMessage", "You have successfully reset your password!");
		model.addAttribute("ErrorMessage", "ERRROOOOOOOR!");
		return "Auth/login";
	}
	
	
	
	@GetMapping("/user-dashboard")
	public String userPage(Model model, Principal principal, HttpServletRequest request) {
	    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
	    String currentUrl = request.getRequestURL().toString();
	    model.addAttribute("currentUrl", currentUrl);
	    model.addAttribute("user", userDetails);
	    return "User/UserDashboard";
	}
	
	@GetMapping("/admin-dashboard")
	public String adminPage (Model model, Principal principal, HttpServletRequest request) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		String currentUrl = request.getRequestURL().toString();
	    model.addAttribute("currentUrl", currentUrl);
	    model.addAttribute("user", userDetails);
		return "Admin/AdminDashboard";
	}
	
	/*------------------------------------------------------------------------------------------------*/
	 
}
