package br.com.pradella.halpdesks.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.pradella.halpdesks.entity.User;
import br.com.pradella.halpdesks.enuns.ProfileEnum;

public class JwtUserFactory {
	
	private JwtUserFactory() {
	}
	
	public static JwtUser create(User user) {
		return new JwtUser(user.getId(),
				user.getEmail(),
				user.getPassword(),
				mapToGranteAuthorities(user.getProfile()));
	}

	private static Collection<? extends GrantedAuthority> mapToGranteAuthorities(ProfileEnum profileEnum) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
	}
	

}
