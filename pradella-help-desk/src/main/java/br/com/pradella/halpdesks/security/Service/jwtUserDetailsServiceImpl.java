package br.com.pradella.halpdesks.security.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pradella.halpdesks.entity.User;
import br.com.pradella.halpdesks.security.jwt.JwtUserfactory;
import br.com.pradella.halpdesks.service.UserService;

@Service
public class jwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.FindbyEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with userName '$s'.", email));
		} else {
			return JwtUserfactory.create(user);
		}
	}

}
