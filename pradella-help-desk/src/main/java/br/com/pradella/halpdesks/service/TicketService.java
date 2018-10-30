package br.com.pradella.halpdesks.service;

import org.springframework.data.domain.Page;

import br.com.pradella.halpdesks.entity.ChangesStatus;
import br.com.pradella.halpdesks.entity.Ticket;


public interface TicketService {

	Ticket createOrUpdate (Ticket ticket);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listTicket(int page, int count);
	
	ChangesStatus createChangeStatus(ChangesStatus changesStatus);
	
	Iterable<ChangesStatus> listChangesStatus(String ticketId);
	
	Page<Ticket> findByCurrentUser(int page, int count, String userId);
	
	Page<Ticket> findByParameters(int page, int count, String title, String status, String priority);
	
	Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String priority, String UserId);

	Page<Ticket> findbyNumber(int page, int count, Integer number);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status, String priority, String assignedUser);
}
