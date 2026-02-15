package com.example.regular_user_service.controller;

import com.example.regular_user_service.dto.SignInDto;
import com.example.regular_user_service.dto.SignupDto;
import com.example.regular_user_service.dto.UserDto;
import com.example.regular_user_service.dto.mapper.UserMapper;
import com.example.regular_user_service.entities.User;
import com.example.regular_user_service.repositories.UserRepository;
import com.example.regular_user_service.services.JwtService;
import com.example.regular_user_service.dto.EnvVariables;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/regular-user/auth")
public class AuthController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EnvVariables envVariables;

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupDto signupDto) {
		try {
			User newUser = new User();
			newUser.setUsername(signupDto.username().toLowerCase());
			newUser.setEmail(signupDto.email().toLowerCase());
			newUser.setPassword(passwordEncoder.encode(signupDto.password()));
			newUser.setCustomAvatarUrl(envVariables.getDefaultAvatar());
			newUser.setCustomBannerUrl(envVariables.getDefaultBanner());
			UserDto createdUser = UserMapper.mapToResponseDto(userRepository.save(newUser), envVariables.getImageDomain());
			return ResponseEntity.ok().body(createdUser);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or Email already existed");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error, user creation failed. " + e.getMessage());
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@Valid @RequestBody SignInDto signInDto) {
		String login = signInDto.login().toLowerCase();
		User user = userRepository.findUserByEmailOrUsername(login, login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username, email or password"));
		if (!passwordEncoder.matches(signInDto.password(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username, email or password");
		}
		String accessToken = jwtService.generateAccessToken(user.getId());
		String refreshToken = jwtService.generateRefreshToken(user.getId());
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(true)
				.sameSite("Strict")
				.path("/")
				.maxAge(7 * 24 * 60 * 60)
				.build();
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.body(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		try {
			String currentRefreshToken = extractRefreshToken(request);
			if (currentRefreshToken == null)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found. Please sign in.");
			jwtService.validateJwtToken(currentRefreshToken, "refresh token");
			Long userId = Long.parseLong(jwtService.getUserId(currentRefreshToken, "refresh token"));
			String newAccessToken = jwtService.generateAccessToken(userId);
			String newRefreshToken = jwtService.generateRefreshToken(userId);
			ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
					.httpOnly(true)
					.secure(true)
					.sameSite("Strict")
					.path("/")
					.maxAge(7 * 24 * 60 * 60)
					.build();
			User user = userRepository.findUserById(userId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
			return ResponseEntity.ok()
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
					.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
					.body(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token: please sign in again");
		}
	}

	private String extractRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("refreshToken"))
					return cookie.getValue();
			}
		}
		return null;
	}
}