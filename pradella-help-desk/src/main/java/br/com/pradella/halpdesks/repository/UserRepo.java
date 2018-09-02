package br.com.pradella.halpdesks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.pradella.halpdesks.entity.User;

public interface UserRepo extends MongoRepository<User, String> {

	User findByEmail(String email);  
	
}
