package br.com.pradella.halpdesks.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import br.com.pradella.halpdesks.entity.ChangesStatus;
import br.com.pradella.halpdesks.entity.Ticket;

@Component
public interface TicketService {

	Ticket createOrUpdate (Ticket ticket);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listTicket(int page, int count);
	
	ChangesStatus createChangeStatus(ChangesStatus changesStatus);
	
	Iterable<ChangesStatus> listChangesStatus(String ticketId);
	
	Page<Ticket> findbyCurrentUser(int page, int count, String userId);
	
	Page<Ticket> findByParameters(int page, int count, String title, String status, String priority);
	
	Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String priority, String UserId);

	Page<Ticket> findbyNumber(int page, int count, Integer number);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findbyParameterAndAssignedUser(int page, int count, String title, String status, String priority, String assignedUser);
}
