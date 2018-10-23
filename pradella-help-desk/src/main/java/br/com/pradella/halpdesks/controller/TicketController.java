package br.com.pradella.halpdesks.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pradella.halpdesks.dto.Summary;
import br.com.pradella.halpdesks.entity.ChangesStatus;
import br.com.pradella.halpdesks.entity.Ticket;
import br.com.pradella.halpdesks.entity.User;
import br.com.pradella.halpdesks.enuns.ProfileEnum;
import br.com.pradella.halpdesks.enuns.StatusEnum;
import br.com.pradella.halpdesks.security.jwt.JwtTokenUtil;
import br.com.pradella.halpdesks.security.response.Response;
import br.com.pradella.halpdesks.service.TicketService;
import br.com.pradella.halpdesks.service.UserService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	private ResponseEntity<Response<Ticket>> create(HttpServletRequest request,
			@RequestBody Ticket ticket,
			BindingResult result) {

		Response<Ticket> response = new Response<>();

		try {
			validateCreateTicket(ticket, result);	
			if (result.hasErrors()) {
				result.getAllErrors()
						.forEach(error -> 
							response.getErrors()
							.add(error
									.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			ticket.setStatus(StatusEnum.getStatus("new"));
			ticket.setUser(userFromRequest(request));
			ticket.setDate(new Date());
			ticket.setNumber(generateNumber());
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	
	@PutMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	private ResponseEntity<Response<Ticket>> update(HttpServletRequest request,
			@RequestBody Ticket ticket,	BindingResult result) {

		Response<Ticket> response = new Response<>();
		try {
			validateUpdateTicket(ticket, result);	
			if (result.hasErrors()) {
				result.getAllErrors()
						.forEach(error -> 
							response.getErrors()
							.add(error
									.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(ticket.getId());			
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUser(ticketCurrent.getUser());
			ticket.setDate(ticketCurrent.getDate());
			ticket.setNumber(ticketCurrent.getNumber());
			if (ticketCurrent.getAssignedUser() != null) {
				ticket.setAssignedUser(ticketCurrent.getAssignedUser());
			}
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	private ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
		Response<Ticket> response = new Response<>();
		Ticket ticket = ticketService.findById(id);
		
		if (ticket == null) {
			response.getErrors().add("Register not found");
			return ResponseEntity.badRequest().body(response);
		}
		List<ChangesStatus> changes = new ArrayList<ChangesStatus>();
		Iterable<ChangesStatus> changesCurrent = ticketService.listChangesStatus(ticket.getId());
		
		for (Iterator<ChangesStatus> iterator = changesCurrent.iterator(); iterator.hasNext();) {
			ChangesStatus changesStatus = (ChangesStatus) iterator.next();
			changesStatus.setTicket(null);
			changes.add(changesStatus);
		}
		
		ticket.setChanges(changes);
		response.setData(ticket);
		return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	private ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<>();
		Ticket ticket = ticketService.findById(id);
		if (ticket == null) {
			response.getErrors().add("Register not found");
			return ResponseEntity.badRequest().body(response);
		}
		
		ticketService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	private ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, 
			@PathVariable("page") int page, @PathVariable("count") int count) {
	    Response<Page<Ticket>> response = new Response<>();
	    Page<Ticket> ticket = null;
	    User userRequest = userFromRequest(request);
	    
	    if (userRequest.getProfile().equals(ProfileEnum.ROLE_TECHNICIAN)) {
			ticket = ticketService.listTicket(page, count);
	    } else if (userRequest.getProfile().equals(ProfileEnum.ROLE_CUSTOMER)) {
	    	ticket = ticketService.findbyCurrentUser(page, count, userRequest.getId());
	    }
		
	    response.setData(ticket);
	    return ResponseEntity.ok(response);
	}

	@GetMapping(value = "{page}/{count}/number}/{title}/{status}/{priority}/{assigned}")
	@PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
	private ResponseEntity<Response<Page<Ticket>>> findByParams(HttpServletRequest request, 
												@PathVariable("page") int page,
												@PathVariable("count") int count,
												@PathVariable("number") Integer number,
												@PathVariable("title") String title,
												@PathVariable("status") String status,
												@PathVariable("priority") String priority,
												@PathVariable("assigned") boolean assigned) {
	    title = title.equals("uninformed") ? "" : title;
	    status = status.equals("uninformed") ? "" : status;
	    priority = priority.equals("uninformed") ? "" : priority;
		
		Response<Page<Ticket>> response = new Response<>();
	    Page<Ticket> ticket = null;
	    
	    if (number > 0) {
	    	ticket = ticketService.findbyNumber(page, count, number);
	    } else {
	    	User userFromRequest = userFromRequest(request);
	    	if (userFromRequest.getProfile().equals(ProfileEnum.ROLE_TECHNICIAN)) {
				if (assigned) {
					ticket = ticketService.findbyParameterAndAssignedUser(page, count, title, status, priority, userFromRequest.getId());
				} else {
					ticket = ticketService.findByParameters(page, count, title, status, priority);
				}
			} else if (userFromRequest.getProfile().equals(ProfileEnum.ROLE_CUSTOMER)) {
				ticket = ticketService.findByParametersAndCurrentUser(page, count, title, status, priority, userFromRequest.getId());
			}
	    }
		
	    response.setData(ticket);
	    return ResponseEntity.ok(response);
	}
	
		
	@PutMapping(value = "{id}/{status}")
	@PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
	private ResponseEntity<Response<Ticket>> chanceStatus(@PathVariable("id") String id, 
								@PathVariable("status") String status,
								HttpServletRequest request,
								@RequestBody Ticket ticket,
								BindingResult result) {
		Response<Ticket> response = new Response<>();

		try {
			validateChangeStatus(id, status, result);
			if (result.hasErrors()) {
				result.getAllErrors()
						.forEach(error -> 
							response.getErrors()
							.add(error
									.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(id);			
			ticketCurrent.setStatus(StatusEnum.getStatus(status));
			if (status.equals("Assigned")) {
				ticketCurrent.setAssignedUser(userFromRequest(request));
			}

			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticketCurrent);
			ChangesStatus changesStatus = new ChangesStatus();
			changesStatus.setUser(userFromRequest(request));
			changesStatus.setDateChanges(new Date());
			changesStatus.setStatus(StatusEnum.getStatus(status));
			changesStatus.setTicket(ticketPersisted);
			ticketService.createChangeStatus(changesStatus);
			response.setData(ticketPersisted);
			
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

	    return ResponseEntity.ok(response);
	}

	
	@GetMapping(value = "/summary")
	@PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
	private ResponseEntity<Response<Summary>> findSummary(HttpServletRequest request) {
		Response<Summary> response = new Response<>();
		Summary summary = new Summary();
		int amountNew = 0;
		int amountResolved = 0;
		int amountApproved = 0;
		int amountDisaproved = 0;
		int amountAssigned = 0;
		int amountClose = 0;
		
		Iterable<Ticket> tickets = ticketService.findAll();
		if (tickets != null) {
			for (Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext();) {
				Ticket ticket = (Ticket) iterator.next();
				if (ticket.getStatus().equals(StatusEnum.NEW)) amountNew++;
				if (ticket.getStatus().equals(StatusEnum.RESOLVED)) amountResolved++;
				if (ticket.getStatus().equals(StatusEnum.APPROVED)) amountApproved++;
				if (ticket.getStatus().equals(StatusEnum.DISAPPROVED)) amountDisaproved++;
				if (ticket.getStatus().equals(StatusEnum.ASSIGNED)) amountAssigned++;
				if (ticket.getStatus().equals(StatusEnum.CLOSED)) amountClose++;
			}
		}
		summary.setAmountNew(amountNew);
		summary.setAmountResolved(amountResolved);
		summary.setAmountApproved(amountApproved);
		summary.setAmountDisaproved(amountDisaproved);
		summary.setAmountAssigned(amountAssigned);
		summary.setAmountClose(amountClose);
		response.setData(summary);
	    return ResponseEntity.ok(response);
	}
	
	
	
	
	
	// methodos auxiliares
	private Integer generateNumber() {
		return new Random().nextInt(9999);
	}

	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title no information"));
		}
	}
	
	private void validateUpdateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "Id no information"));
		}
		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title no information"));
		}
	}
	
	private void validateChangeStatus(String id, String status, BindingResult result) {
		if (id == null || id == "" ) {
			result.addError(new ObjectError("Ticket", "Id no information"));
		}
		if (status == null || status == "") {
			result.addError(new ObjectError("Ticket", "Status no information"));
		}
	}
}
