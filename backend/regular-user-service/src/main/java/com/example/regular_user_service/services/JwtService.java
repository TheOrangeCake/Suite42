package com.example.regular_user_service.services;

import com.example.regular_user_service.exception.BadTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HexFormat;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Getter
@Service
public class JwtService {
	private final SecretKey secretKey;

	public JwtService(@Value("${REGULAR_USER_JWT_KEY}") String hexKey) {
		byte[] keyBytes = HexFormat.of().parseHex(hexKey);
		this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	public String generateAccessToken(Long id) {
		return Jwts.builder()
				.issuer("Regular User Service")
				.subject(id.toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
				.signWith(secretKey)
				.compact();
	}

	public String generateRefreshToken(Long id) {
		return Jwts.builder()
				.issuer("Regular User Service")
				.subject(id.toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
				.signWith(secretKey)
				.compact();
	}

	public void validateJwtToken(String JwtToken, String type) {
		try {
			if (!Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(JwtToken)
					.getPayload()
					.getIssuer()
					.equals("Regular User Service"))
				throw new BadTokenException("Invalid " + type + ": wrong issuer");
		} catch (ExpiredJwtException e) {
			if (type.equals("access token"))
				throw new BadTokenException("Invalid " + type + ": expired, please renew at /v1/user-regular/auth/refresh-token");
			else
				throw new BadTokenException("Invalid " + type + ": expired, please sign in again");
		} catch (JwtException e) {
			throw new BadTokenException("Invalid "  + type +  ": bad format or signature");
		}
	}

	public String getUserId(String jwtToken, String type) {
		String userId = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(jwtToken)
					.getPayload()
					.getSubject();
		if (userId == null || userId.isEmpty()) {
			throw new BadTokenException("Invalid "  + type +  ": user ID not found");
		}
		return userId;
	}
}