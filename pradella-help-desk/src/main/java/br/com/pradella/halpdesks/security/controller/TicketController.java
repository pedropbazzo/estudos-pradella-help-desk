package br.com.pradella.halpdesks.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pradella.halpdesks.entity.ChangesStatus;
import br.com.pradella.halpdesks.entity.Ticket;
import br.com.pradella.halpdesks.entity.User;
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
}
