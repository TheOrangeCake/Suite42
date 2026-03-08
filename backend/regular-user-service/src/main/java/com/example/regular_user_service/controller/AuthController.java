package com.example.regular_user_service.controller;

import com.example.regular_user_service.dto.SignInDto;
import com.example.regular_user_service.dto.SignupDto;
import com.example.regular_user_service.dto.UserDto;
import com.example.regular_user_service.dto.mapper.UserMapper;
import com.example.regular_user_service.entities.User;
import com.example.regular_user_service.exception.BadTokenException;
import com.example.regular_user_service.repositories.UserRepository;
import com.example.regular_user_service.services.JwtService;
import com.example.regular_user_service.dto.EnvVariables;
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

import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/regular-user/auth")
public class AuthController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EnvVariables envVariables;
	private final Logger logger;

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupDto signupDto) {
		try {
			User newUser = new User();
			newUser.setUsername(signupDto.username().toLowerCase());
			newUser.setEmail(signupDto.email().toLowerCase());
			newUser.setPassword(passwordEncoder.encode(signupDto.password()));
			newUser.setCustomAvatarUrl(envVariables.getDefaultAvatar());
			newUser.setCustomBannerUrl(envVariables.getDefaultBanner());
			userRepository.save(newUser);
			logger.info("A new user is created. User id: " + newUser.getId());
			UserDto createdUser = UserMapper.mapToResponseDto(newUser, envVariables.getImageDomain());
			return ResponseEntity.ok().body(createdUser);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or Email already existed");
		} catch (RuntimeException e) {
			logger.severe("User creation failed: " + e.getMessage());
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
		Long userId = user.getId();
		logger.info("User " + userId + " logged in");
		return generateResponse(user, userId);
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		try {
			String currentRefreshToken = jwtService.extractRefreshToken(request);
			jwtService.validateJwtToken(currentRefreshToken, "refresh token");
			Long userId = Long.parseLong(jwtService.getUserId(currentRefreshToken, "refresh token"));
			User user = userRepository.findUserById(userId)
					.orElseThrow(() -> new BadTokenException("User not found"));
			jwtService.revokeToken(currentRefreshToken, "refresh token");

			String currentAccessToken = jwtService.extractAccessToken(request);
			jwtService.revokeToken(currentAccessToken, "access token");

			logger.info("User " + userId + " has generated new refresh and access tokens");
			return generateResponse(user, userId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: please sign in again");
		}
	}

	private ResponseEntity<UserDto> generateResponse(User user, Long userId) {
		String newAccessToken = jwtService.generateAccessToken(userId);
		String newRefreshToken = jwtService.generateRefreshToken(userId);
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
				.httpOnly(true)
				.secure(true)
				.sameSite("Strict")
				.path("/")
				.maxAge(7 * 24 * 60 * 60)
				.build();
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.body(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
	}
}