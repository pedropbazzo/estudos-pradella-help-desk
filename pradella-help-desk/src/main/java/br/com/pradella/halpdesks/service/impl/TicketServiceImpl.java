package br.com.pradella.halpdesks.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pradella.halpdesks.entity.ChangesStatus;
import br.com.pradella.halpdesks.entity.Ticket;
import br.com.pradella.halpdesks.repository.ChancesStatusRepo;
import br.com.pradella.halpdesks.repository.TicketRepo;
import br.com.pradella.halpdesks.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService{

	@Autowired
	private TicketRepo ticketRepo;
	
	@Autowired
	private ChancesStatusRepo chancesStatusRepo; 
	
	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketRepo.save(ticket);
	}

	@Override
	public Ticket findById(String id) {
		return this.ticketRepo.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.ticketRepo.delete(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findAll(pages);
	}

	@Override
	public ChangesStatus createChangeStatus(ChangesStatus changesStatus) {
		return this.chancesStatusRepo.save(changesStatus);
	}

	@Override
	public Iterable<ChangesStatus> listChangesStatus(String ticketId) {
		return this.chancesStatusRepo.findByTicketIdOrderByDateChangesDesc(ticketId);
	}

	@Override
	public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findByUserIdOrderByDateDesc(pages, userId);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String title, String status, String priority) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingOrderByDateDesc(title, status, priority, pages);
	}                          

	@Override
	public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String priority, String userId) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndUserIdOrderByDateDesc(
				title, status, priority, userId, pages);
	}

	@Override
	public Page<Ticket> findbyNumber(int page, int count, Integer number) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findByNumber(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepo.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status, String priority, String assignedUser) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepo.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndAssignedUserIdOrderByDateDesc(
				title, status, priority, assignedUser, pages);
	}

	
}
