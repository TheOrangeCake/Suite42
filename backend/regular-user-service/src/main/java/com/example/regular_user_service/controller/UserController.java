package com.example.regular_user_service.controller;

import com.example.regular_user_service.dto.EnvVariables;
import com.example.regular_user_service.dto.PasswordDeleteDto;
import com.example.regular_user_service.dto.UpdateDto;
import com.example.regular_user_service.dto.UserDto;
import com.example.regular_user_service.dto.mapper.UserMapper;
import com.example.regular_user_service.entities.User;
import com.example.regular_user_service.exception.BadTokenException;
import com.example.regular_user_service.exception.DeleteDefaultException;
import com.example.regular_user_service.repositories.UserRepository;
import com.example.regular_user_service.services.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/regular-user/user")
public class UserController {
	private final UserRepository userRepository;
	private final EnvVariables envVariables;
	private final PasswordEncoder passwordEncoder;
	private final FileUploadService fileUploadService;

	@GetMapping("/profile/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return ResponseEntity.ok(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
	}

	@GetMapping("/profile")
	public ResponseEntity<UserDto> getMyProfile() {
		Long userId = getUserId();
		User user = userRepository.findUserById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return ResponseEntity.ok(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateDto info) {
		Long userId = getUserId();
		User user = userRepository.findUserById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(info.confirm_password(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		if (!info.email().equals(user.getEmail()) && userRepository.existsByEmail(info.email())) {
			return ResponseEntity.badRequest().body("Email is already used");
		} else if (!info.email().equals(user.getEmail())) {
			user.setEmail(info.email());
		}
		user.setFirstName(info.first_name());
		user.setLastName(info.last_name());
		userRepository.save(user);
		return ResponseEntity.ok(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
	}

	@DeleteMapping("/profile")
	public ResponseEntity<String> deleteMyProfile(@Valid @RequestBody PasswordDeleteDto password) {
		Long userId = getUserId();
		User user = userRepository.findUserById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(password.confirm_password(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		if (password.confirm_deletion()) {
			userRepository.delete(user);
			return ResponseEntity.ok("Profile deleted successfully");
		}
		return ResponseEntity.badRequest().body("Deletion not confirmed");
	}

	private Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new BadTokenException("Invalid access token: User Id not found");
		}
		return Long.parseLong(authentication.getPrincipal().toString());
	}

	@PatchMapping("/profile/avatar")
	public ResponseEntity<?> modifyAvatar(@RequestParam("avatar") MultipartFile avatar,
	                                      HttpServletRequest request,
	                                      @RequestParam @NotBlank(message = "Password can not be blank") String confirm_password) {
		Long userId = getUserId();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(confirm_password, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		return modifyImage(user, avatar, request, "avatar");
	}

	@PatchMapping("/profile/banner")
	public ResponseEntity<?> modifyBanner(@RequestParam("banner") MultipartFile banner,
	                                      HttpServletRequest request,
	                                      @RequestParam @NotBlank(message = "Password can not be blank") String confirm_password) {
		Long userId = getUserId();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(confirm_password, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		return modifyImage(user, banner, request, "banner");
	}

	private ResponseEntity<?> modifyImage(User user, MultipartFile image, HttpServletRequest request, String type) {
		if (!request.getContentType().startsWith("multipart/form-data")) {
			return ResponseEntity.badRequest().body("Request must be multipart/form-data");
		}
		if (image.isEmpty() || !fileUploadService.isImage(image)) {
			return ResponseEntity.badRequest().body("Invalid file or no Content-Type header");
		}
		String fileExtension = fileUploadService.getFileExtension(image);
		if (fileExtension == null) {
			return ResponseEntity.badRequest().body("Invalid file extension. Allowed: .gif, .jpg, .jpeg, .png, .webp");
		}
		try {
			String fileName = fileUploadService.uploadImage(user.getId().toString(), image, fileExtension);
			if (type.equals("avatar")) {
				if (!user.getCustomAvatarUrl().equals(envVariables.getDefaultAvatar())) {
					fileUploadService.deleteImage(user.getCustomAvatarUrl());
				}
				user.setCustomAvatarUrl(fileName);
			} else if (type.equals("banner")) {
				if (!user.getCustomBannerUrl().equals(envVariables.getDefaultBanner())) {
					fileUploadService.deleteImage(user.getCustomBannerUrl());
				}
				user.setCustomBannerUrl(fileName);
			} else {
				return ResponseEntity.internalServerError().body("Fail to upload file, something went wrong in the server.");
			}
			userRepository.save(user);
			return ResponseEntity.ok(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
		} catch (IOException | RuntimeException e) {
			System.err.println("Fail to upload file. " + e.getMessage());
			return ResponseEntity.internalServerError().body("Fail to upload file. Something went wrong in the server.");
		}
	}

	@DeleteMapping("/profile/avatar")
	public ResponseEntity<?> deleteAvatar(@RequestParam @NotBlank(message = "Password can not be blank") String confirm_password) {
		Long userId = getUserId();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(confirm_password, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		return deleteImage(user, "avatar");
	}

	@DeleteMapping("/profile/banner")
	public ResponseEntity<?> deleteBanner(@RequestParam @NotBlank(message = "Password can not be blank") String confirmPassword) {
		Long userId = getUserId();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
		}
		return deleteImage(user, "banner");
	}

	private ResponseEntity<?> deleteImage(User user, String type) {
		try {
			if (type.equals("banner")) {
				fileUploadService.deleteImage(user.getCustomBannerUrl());
				user.setCustomBannerUrl(envVariables.getDefaultBanner());
			} else if (type.equals("avatar")) {
				fileUploadService.deleteImage(user.getCustomAvatarUrl());
				user.setCustomAvatarUrl(envVariables.getDefaultAvatar());
			} else
				return ResponseEntity.internalServerError().body("Image deletion failed. Something went wrong in the server");
			userRepository.save(user);
			return ResponseEntity.ok(UserMapper.mapToResponseDto(user, envVariables.getImageDomain()));
		} catch(IOException e) {
			return ResponseEntity.internalServerError().body("Image deletion failed. Something went wrong in the server");
		} catch(SecurityException e) {
			return ResponseEntity.status(403).body(e.getMessage());
		} catch(DeleteDefaultException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}