package com.mediatech.MoneyManagement.Controllers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mediatech.MoneyManagement.Models.DaretOperation;
import com.mediatech.MoneyManagement.Models.User;
import com.mediatech.MoneyManagement.Repositories.DaretOperationRepository;
import com.mediatech.MoneyManagement.Services.DaretOperationService;
import com.mediatech.MoneyManagement.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class DaretOperationController {
	@Autowired
    private DaretOperationService daretOperationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private DaretOperationRepository daretOperationRepository;

	@GetMapping("/liste-des-offres")
	public String listeOffres(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
	    // Get the currently authenticated user details
	    User currentUser = userService.findByEmail(userDetails.getUsername());

	    // Retrieve all DaretOperations created by the user
	    List<DaretOperation> userDaretOperations = daretOperationService.findByAdminOffre(currentUser);
	    String currentUrl = request.getRequestURL().toString();
	    model.addAttribute("currentUrl", currentUrl);
	    // Add the authenticated user details to the model
	    model.addAttribute("user", currentUser);

	    // Add the list of DaretOperations to the model
	    model.addAttribute("userDaretOperations", userDaretOperations);
	 // Get counts for each status
        long inProgressCount = daretOperationRepository.countByStatusAndAdminOffre("In Progress", currentUser);
        long pendingCount = daretOperationRepository.countByStatusAndAdminOffre("Pending", currentUser);
        long closedCount = daretOperationRepository.countByStatusAndAdminOffre("Closed", currentUser);
        long totalOffersCount = daretOperationRepository.countByAdminOffre(currentUser);


        // Add counts to the model
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("closedCount", closedCount);
        model.addAttribute("totalOffersCount", totalOffersCount);
     
        // Return the view name
	    return "Admin/liste-offres";
	}

    
}
