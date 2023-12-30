package com.mediatech.MoneyManagement.Controllers;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediatech.MoneyManagement.Models.DaretOperation;
import com.mediatech.MoneyManagement.Models.User;
import com.mediatech.MoneyManagement.Repositories.DaretOperationRepository;
import com.mediatech.MoneyManagement.Services.DaretOperationService;
import com.mediatech.MoneyManagement.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


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

	    long inProgressCount = daretOperationRepository.countByStatusAndAdminOffre("Progress", currentUser);
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
	    public String saveOffer(@ModelAttribute("daretOperation") @Valid DaretOperation daretOperation,
	                            BindingResult bindingResult,
	                            @AuthenticationPrincipal UserDetails userDetails,
	                            Model model) {

	        if (bindingResult.hasErrors()) {
	            // If there are validation errors, return to the form page with error messages
	            return "Admin/ajouter-offre";
	        }

	        User currentUser = userService.findByEmail(userDetails.getUsername());
	        daretOperation.setAdminOffre(currentUser);
	        daretOperation.setStatus("Pending");
	        daretOperation.setTourDeRole(1L);

	        daretOperationService.save(daretOperation);

	        return "redirect:/liste-des-offres";
	    }
	//-------------------------------------------------------------------------------------------------------------------------------------------
	    @GetMapping("/edit-offer/{operationId}")
	    public String showUpdateForm(@PathVariable Long operationId, Model model,
	                                 @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
	        // Rest of the code remains the same
	    	 User currentUser = userService.findByEmail(userDetails.getUsername());
			    String currentUrl = request.getRequestURL().toString();
			    model.addAttribute("currentUrl", currentUrl);
			    // Add the authenticated user details to the model
			    model.addAttribute("user", currentUser);
	        // Retrieve the DaretOperation by ID
	        DaretOperation daretOperation = daretOperationService.findById(operationId);

	        // Check if the DaretOperation is editable
	        /*if ("Progress".equals(daretOperation.getStatus())) {
	            // If the DaretOperation is in progress, redirect with a message
	            return "redirect:/liste-des-offres?updateNotAllowed";
	        }*/

	        // If the DaretOperation is not in progress, show the update form
	        model.addAttribute("daretOperation", daretOperation)
	             .addAttribute("pageTitle", "DARET-ADMIN UPDATE OFFER");

	        // Return the view name for the update offer form
	        return "Admin/liste-daret";
	    }

	    //-------------------------------------------------------------------------------------------------------------------------------------------
	    @PostMapping("/edit-offer/{operationId}")
	    public String updateOffer(@PathVariable Long operationId,@ModelAttribute("daretOperation") DaretOperation updatedDaretOperation,
	                              @AuthenticationPrincipal UserDetails userDetails) {
	        // Rest of the code remains the same

	        // Save the updated offer
	        daretOperationService.save(updatedDaretOperation);

	        // Redirect to the list of offers after updating
	        return "redirect:/liste-des-offres?updateSuccess";
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

		//--------------------------------------------------------------------------------------------------------------------------------------------
	    @GetMapping("/liste-offres-pending")
	    public String listPendingOffers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
	        // Get the currently authenticated user details
	        User currentUser = userService.findByEmail(userDetails.getUsername());

	        // Get a list of offers with status "Pending" for all admins
	        List<DaretOperation> pendingOffers = daretOperationService.findPendingOffers();

	        // Add the authenticated user details and the list of pending offers to the model
	        model.addAttribute("user", currentUser)
	        	.addAttribute("pendingOffers", pendingOffers)
	        	.addAttribute("pageTitle", "DARET-ADMIN UPDATE OFFER");
	        System.out.println(pendingOffers);
	        // Return the view name for displaying the list of pending offers
	        return "Admin/liste-offres-pending";
	    }
		//--------------------------------------------------------------------------------------------------------------------------------------------
	    @PostMapping("/liste-offres-pending/add-participant")
	    public String addParticipantToDaretOperation(
	    	@RequestParam("daretOperationId") Long daretOperationId,
	        @RequestParam("userId") Long userId,
	        @RequestParam("paymentType") String paymentType,
	        Model model
	    ) {
	        // Validate payment type (you might want to add more validation)
	        if (!paymentType.equals("Moitier") && !paymentType.equals("Normale") && !paymentType.equals("Double")) {
	            // Handle invalid payment type, e.g., redirect to an error page
	            return "redirect:/error";
	        }

	        // Call the service method to add participant and update placesReservees
	        daretOperationService.addParticipantToDaretOperation(daretOperationId, userId, paymentType);

	        return "redirect:/liste-offres-pending";
	    }


    
}
