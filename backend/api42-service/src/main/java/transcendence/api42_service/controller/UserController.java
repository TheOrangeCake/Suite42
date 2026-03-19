package transcendence.api42_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import transcendence.api42_service.dto.EnvVariables;
import transcendence.api42_service.dto.UserDetailedResponseDto;
import transcendence.api42_service.dto.UserSimpleResponseDto;
import transcendence.api42_service.exception.BadTokenException;
import transcendence.api42_service.exception.DeleteDefaultException;
import transcendence.api42_service.repositories.specification.UserSpecifications;
import transcendence.api42_service.dto.mapper.UserMapper;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.repositories.UserRepository;
import transcendence.api42_service.services.FileUploadService;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestMapping("v1/42users")
@AllArgsConstructor
@RestController
public class UserController {
	private final UserRepository userRepository;
	private final FileUploadService fileUploadService;
	private final UserMapper userMapper;
	private final EnvVariables envVariables;
	private final Logger logger;

	private static final Set<String> ALL_USERS_ALLOWED_SORTS = Set.of(
			"rank",
			"poolYear",
			"performanceScore",
			"rankProgressPercent"
	);

	@GetMapping()
	public Page<UserSimpleResponseDto> getUsers(
			@RequestParam String campusName,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String poolMonth,
			@RequestParam(required = false) String poolYear,
			@RequestParam(required = false) Integer rank,
			@RequestParam(required = false) String eligibleProject,
			@RequestParam(required = false) Set<String> finishedProjects,
			@RequestParam(required = false) String lfg,
			@PageableDefault(size = 25) Pageable pageable) {
		int maxSize = 50;
		Sort sort = pageable
				.getSort()
				.stream()
				.filter(order -> ALL_USERS_ALLOWED_SORTS.contains(order.getProperty()))
				.collect(Collectors.collectingAndThen(
						Collectors.toList(),
						orders -> orders.isEmpty()
								? Sort.by("performanceScore")
								: Sort.by(orders)
				));
		Pageable safePageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize),
				sort);
		Specification<User> spec = Specification
				.where(UserSpecifications.isActive())
				.and(UserSpecifications.betweenRank(0, 6))
				.and(UserSpecifications.searchByNameOrLogin(search))
				.and(UserSpecifications.hasCampusName(campusName))
				.and(UserSpecifications.hasPoolMonth(poolMonth))
				.and(UserSpecifications.hasPoolYear(poolYear))
				.and(UserSpecifications.hasRank(rank))
				.and(UserSpecifications.hasEligibleProject(eligibleProject))
				.and(UserSpecifications.hasFinishedProjects(finishedProjects))
				.and(UserSpecifications.hasLfg(lfg));
		return userRepository.findAll(spec, safePageable)
				.map(userMapper::mapToSimpleDto);
	}

	@GetMapping("/profile/{id}")
	public UserDetailedResponseDto getUserById(@PathVariable Long id) {
		if (id == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
		}
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return userMapper.mapToDetailedDto(user);
	}

	@GetMapping("/profile/login/{login}")
	public UserDetailedResponseDto getUserByLogin(@PathVariable String login) {
		User user = this.userRepository.findByLogin(login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return userMapper.mapToDetailedDto(user);
	}

	@GetMapping("/last_name/{lastName}")
	@SuppressWarnings("Duplicates")
	public Page<UserSimpleResponseDto> getUserByLastName(
			@PathVariable String lastName,
			@PageableDefault(size = 25) Pageable pageable) {
		if (lastName == null || lastName.trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name cannot be null or empty");
		}
		int maxSize = 50;
		Pageable noSortPageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize)
		);
		Page<User> usersPage = this.userRepository.findByLastName(lastName, noSortPageable);
		return usersPage.map(userMapper::mapToSimpleDto);
	}

	@GetMapping("/first_name/{firstName}")
	@SuppressWarnings("Duplicates")
	public Page<UserSimpleResponseDto> getUserByFirstName(
			@PathVariable String firstName,
			@PageableDefault(size = 25) Pageable pageable) {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name cannot be null or empty");
		}
		int maxSize = 50;
		Pageable noSortPageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize)
		);
		Page<User> usersPage = this.userRepository.findByFirstName(firstName, noSortPageable);
		return usersPage.map(userMapper::mapToSimpleDto);
	}

	private Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new BadTokenException("Invalid access token: User Id not found");
		}
		return Long.parseLong(authentication.getPrincipal().toString());
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getUserProfile() {
		Long userId;
		try {
			userId = getUserId();
		} catch (BadTokenException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(userMapper.mapToDetailedDto(user));
	}


	@PatchMapping("/lfg")
	public ResponseEntity<?> modifyLFG(@RequestParam String lfg) {
		if (lfg == null || lfg.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("LFG project cannot be null or empty");
		}
		Long userId;
		try {
			userId = getUserId();
		} catch (BadTokenException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (lfg.equals("none")) {
			user.setLfg(lfg);
			userRepository.save(user);
			return ResponseEntity.ok(userMapper.mapToDetailedDto(user));
		}
		Set<String> eligibleProjects= user.getEligibleProjects();
		boolean validateLFG = eligibleProjects != null && eligibleProjects.stream().anyMatch(lfg::equals);
		if (validateLFG) {
			user.setLfg(lfg);
			userRepository.save(user);
			return ResponseEntity.ok(userMapper.mapToDetailedDto(user));
		}
		return ResponseEntity.status(400).body("Invalid lfg project. Only eligible projects or none are accepted.");
	}

	@PatchMapping("/avatar")
	public ResponseEntity<?> modifyAvatar(@RequestParam("avatar") MultipartFile avatar, HttpServletRequest request) {
			return modifyImage(avatar, request, "avatar");
	}

	@PatchMapping("/banner")
	public ResponseEntity<?> modifyBanner(@RequestParam("banner") MultipartFile banner, HttpServletRequest request) {
		return modifyImage(banner, request, "banner");
	}

	private ResponseEntity<?> modifyImage(MultipartFile image, HttpServletRequest request, String type) {
		if (image == null || image.isEmpty()) {
			return ResponseEntity.badRequest().body("No file provided or file is empty");
		}
		if (!request.getContentType().startsWith("multipart/form-data")) {
			return ResponseEntity.status(400).body("Request must be multipart/form-data");
		}
		Long userId;
		try {
			userId = getUserId();
		} catch (BadTokenException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (image.isEmpty() || !fileUploadService.isImage(image)) {
			return ResponseEntity.status(400).body("Invalid file or no Content-Type header");
		}
		String fileExtension = fileUploadService.getFileExtension(image);
		if (fileExtension == null) {
			return ResponseEntity.status(400).body("Invalid file extension. Allowed: .gif, .jpg, .jpeg, .png, .webp");
		}
		try {
			String fileName = fileUploadService.uploadImage(user.getId().toString(), image, fileExtension);
			if (type.equals("avatar")) {
				if (user.getCustomAvatarUrl() != null) {
					fileUploadService.deleteImage(user.getCustomAvatarUrl());
				}
				user.setCustomAvatarUrl(fileName);
			} else if (type.equals("banner")) {
				if (user.getCustomBannerUrl() != null && !user.getCustomBannerUrl().equals(envVariables.getDefaultBanner())) {
					fileUploadService.deleteImage(user.getCustomBannerUrl());
				}
				user.setCustomBannerUrl(fileName);
			} else {
				logger.severe("Fail to upload file, something went wrong in the server.");
				return ResponseEntity.status(500).body("Fail to upload file, something went wrong in the server.");
			}
			userRepository.save(user);
			return ResponseEntity.ok(userMapper.mapToDetailedDto(user));
		} catch(IOException | RuntimeException e) {
			logger.severe("Fail to upload file. " + e.getMessage());
			return ResponseEntity.status(500).body("Fail to upload file. Something went wrong in the server.");
		}
	}

	@DeleteMapping("/avatar")
	public ResponseEntity<?> deleteAvatar() {
		return deleteImage("avatar");
	}

	@DeleteMapping("/banner")
	public ResponseEntity<?> deleteBanner() {
		return deleteImage("banner");
	}

	private ResponseEntity<?> deleteImage(String type) {
		Long userId;
		try {
			userId = getUserId();
		} catch (BadTokenException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		try {
			if (type.equals("banner")) {
				fileUploadService.deleteImage(user.getCustomBannerUrl());
				user.setCustomBannerUrl(envVariables.getDefaultBanner());
			} else if (type.equals("avatar")) {
				fileUploadService.deleteImage(user.getCustomAvatarUrl());
				user.setCustomAvatarUrl(null);
			} else
				return ResponseEntity.status(500).body("Image deletion failed. Something went wrong in the server");
			userRepository.save(user);
			return ResponseEntity.ok(userMapper.mapToDetailedDto(user));
		} catch(IOException e) {
			logger.severe("Fail to delete file, something went wrong in the server.");
			return ResponseEntity.status(500).body("Image deletion failed. Something went wrong in the server");
		} catch(SecurityException e) {
			return ResponseEntity.status(403).body("Access denied");
		} catch(DeleteDefaultException e) {
			return ResponseEntity.status(400).body("No image specified or can not delete default image");
		}
	}
}