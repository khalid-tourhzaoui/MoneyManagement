package com.mediatech.MoneyManagement.Controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	UserDetailsService userDetailsService;
	
	@Autowired
    private DaretOperationRepository daretOperationRepository;
	

	@GetMapping("/liste-des-offres")
	public String listeOffres(
	    @RequestParam(name = "status", defaultValue = "All") String status,
	    Model model,
	    @AuthenticationPrincipal UserDetails userDetails,
	    HttpServletRequest request
	) {
	    User currentUser = userService.findByEmail(userDetails.getUsername());
	    List<DaretOperation> userDaretOperations;

	    if (status.equals("All")) {
	        userDaretOperations = daretOperationService.findByAdminOffre(currentUser);
	    } else {
	        userDaretOperations = daretOperationService.findByAdminOffreAndStatus(currentUser, status);
	    }

	    long inProgressCount = daretOperationRepository.countByStatusAndAdminOffre("In Progress", currentUser);
	    long pendingCount = daretOperationRepository.countByStatusAndAdminOffre("Pending", currentUser);
	    long closedCount = daretOperationRepository.countByStatusAndAdminOffre("Closed", currentUser);
	    long totalOffersCount = daretOperationRepository.countByAdminOffre(currentUser);

	    model.addAttribute("currentUrl", request.getRequestURL().toString())
	    	.addAttribute("user", currentUser)
		    .addAttribute("userDaretOperations", userDaretOperations)
		    .addAttribute("inProgressCount", inProgressCount)
		    .addAttribute("pendingCount", pendingCount)
		    .addAttribute("closedCount", closedCount)
		    .addAttribute("totalOffersCount", totalOffersCount)
		    .addAttribute("selectedStatus", status)
        	.addAttribute("pageTitle", "DARET-ADMIN OFFER LIST");

	    return "Admin/liste-offres";
	}

	//-------------------------------------------------------------------------------------------------------------------------------------------
	    @GetMapping("/ajouter-offre")
		public String AddOffre(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
	    	// Get the currently authenticated user details
		    User currentUser = userService.findByEmail(userDetails.getUsername());
		    String currentUrl = request.getRequestURL().toString();
		    model.addAttribute("currentUrl", currentUrl);
		    // Add the authenticated user details to the model
		    model.addAttribute("user", currentUser);
	        DaretOperation daretOperation = new DaretOperation();
	        model.addAttribute("daretOperation", daretOperation)
        		.addAttribute("pageTitle", "DARET-ADMIN ADD OFFER");

	        // Return the view name for the add offer form
	        return "Admin/ajouter-offre";
	    }
	//------------------------------------------------------------------------------------------------------------------------------------------
	    @PostMapping("/ajouter-offre")
	    public String saveOffer(@ModelAttribute("daretOperation") DaretOperation daretOperation, 
	                            @AuthenticationPrincipal UserDetails userDetails, 
	                            Model model) {

	        // Get the currently authenticated user details
	        User currentUser = userService.findByEmail(userDetails.getUsername());

	        // Set the current user as the adminOffre for the offer
	        daretOperation.setAdminOffre(currentUser);

	        // Set default status as "Pending"
	        daretOperation.setStatus("Pending");
	        // Initialiser le tour de rôle comme 1
	        daretOperation.setTourDeRole(1L);
	       	        
	        // Save the offer
	        daretOperationService.save(daretOperation);

	        // Redirect to the list of offers
	        return "redirect:/liste-des-offres";
	    }
	//--------------------------------------------------------------------------------------------------------------------------------------------
	    @PostMapping("/delete-daret")
	    public String deleteDaret(@RequestParam Long operationId) {
	        // Retrieve the DaretOperation by ID
	        DaretOperation daretOperation = daretOperationService.findById(operationId);

	        // Check if the DaretOperation is in progress
	        if ("In Progress".equals(daretOperation.getStatus())) {
	            // Display a SweetAlert for cancellation
	            return "redirect:/liste-des-offres?deleteCanceled";
	        }

	        // Implement your service method to delete the DaretOperation by ID
	        daretOperationService.deleteDaretById(operationId);

	        // Redirect to the list view after deletion
	        return "redirect:/liste-des-offres?deleteSuccess";
	    }


    
}
