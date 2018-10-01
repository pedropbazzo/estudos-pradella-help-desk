package br.com.pradella.halpdesks.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pradella.halpdesks.entity.User;
import br.com.pradella.halpdesks.repository.UserRepo;
import br.com.pradella.halpdesks.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public User findByEmail(String email) {
		return this.userRepo.findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		return this.userRepo.save(user);
	}

	@Override
	public User findById(String id) {
		return this.userRepo.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.userRepo.delete(id);
	}

	@Override
	public Page<User> findAll(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.userRepo.findAll(pages);
	}

}
