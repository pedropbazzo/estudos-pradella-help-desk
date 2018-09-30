package br.com.pradella.halpdesks.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	static final String CLAIN_KEY_USERNAME = "sub";
	static final String CLAIN_KEY_CREATED = "created";
	static final String CLAIN_KEY_EXPIRED = "exp";
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private int expiration;
	
	
	public String getUserNameFromToken(String token) {
		String userName;
		try {
			final Claims claims = getClaimsFromToken(token);
			userName = claims.getSubject();
		} catch (Exception e) {
			userName = null;
		}
		
		return userName;
	}

	public Date getExpirationFromToken(String token) {
		Date expiration;
		try {
			final Claims claims =  getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
			
		} catch (Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationFromToken(token);
		return expiration.before(new Date());
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIN_KEY_USERNAME, userDetails.getUsername());
		
		final Date createDate = new Date();
		claims.put(CLAIN_KEY_CREATED, createDate);
		
		return doGenerationToken(claims);
	}

	private String doGenerationToken(Map<String, Object> claims) {
		final Date createdDate = (Date) claims.get(CLAIN_KEY_CREATED);
		final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token));
	}
	
	public String refleshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIN_KEY_CREATED, new Date());
			refreshedToken= doGenerationToken(claims);
		} catch (Exception e) {
			refreshedToken = null ; 
		}
		return refreshedToken;
	}
	
	public Boolean vaidateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String userName = getUserNameFromToken(token);
		return (
				userName.equals(user.getUsername())
					&&!isTokenExpired(token)
				);
	}
	
	
}
