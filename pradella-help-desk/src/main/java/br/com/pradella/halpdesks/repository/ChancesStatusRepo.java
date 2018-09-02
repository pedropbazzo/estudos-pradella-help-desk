package br.com.pradella.halpdesks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.pradella.halpdesks.entity.ChangesStatus;

public interface ChancesStatusRepo extends MongoRepository<ChangesStatus, String>{

	
	Iterable<ChangesStatus> findByTicketIdOrderByDateChangesDesc(String ticketId);

}
