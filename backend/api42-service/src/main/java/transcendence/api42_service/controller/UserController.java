package transcendence.api42_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import transcendence.api42_service.dto.UserDetailedResponseDto;
import transcendence.api42_service.dto.UserSimpleResponseDto;
import transcendence.api42_service.repositories.specification.UserSpecifications;
import transcendence.api42_service.dto.mapper.UserMapper;
import transcendence.api42_service.entity.User;
import transcendence.api42_service.repositories.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("v1/42users")
@AllArgsConstructor
@RestController
public class UserController {
	private final UserRepository userRepository;

	private static final Set<String> ALL_USERS_ALLOWED_SORTS = Set.of(
			"rank",
			"poolYear",
			"rankProgressPercent"
	);

	@GetMapping()
	public Page<UserSimpleResponseDto> getUsers(
			@RequestParam String campusName,
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
								? Sort.by("rank")
								: Sort.by(orders)
				));
		Pageable safePageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize),
				sort);
		Specification<User> spec = Specification
				.where(UserSpecifications.isActive())
				.and(UserSpecifications.hasCampusName(campusName))
				.and(UserSpecifications.hasPoolMonth(poolMonth))
				.and(UserSpecifications.hasPoolYear(poolYear))
				.and(UserSpecifications.hasRank(rank))
				.and(UserSpecifications.hasEligibleProject(eligibleProject))
				.and(UserSpecifications.hasFinishedProjects(finishedProjects))
				.and(UserSpecifications.hasLfg(lfg));
		return userRepository.findAll(spec, safePageable)
				.map(UserMapper::mapToSimpleDto);
	}

	@GetMapping("/id/{id}")
	public UserDetailedResponseDto getUserById(@PathVariable Long id) {
		User user = this.userRepository.findDetailedById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return UserMapper.mapToDetailedDto(user);
	}

	@GetMapping("/last_name/{lastName}")
	@SuppressWarnings("Duplicates")
	public Page<UserSimpleResponseDto> getUserByLastName(
			@PathVariable String lastName,
			@PageableDefault(size = 25) Pageable pageable) {
		int maxSize = 50;
		Pageable noSortPageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize)
		);
		Page<User> usersPage = this.userRepository.findByLastName(lastName, noSortPageable);
		return usersPage.map(UserMapper::mapToSimpleDto);
	}

	@GetMapping("/first_name/{firstName}")
	@SuppressWarnings("Duplicates")
	public Page<UserSimpleResponseDto> getUserByFirstName(
			@PathVariable String firstName,
			@PageableDefault(size = 25) Pageable pageable) {
		int maxSize = 50;
		Pageable noSortPageable = PageRequest.of(
				pageable.getPageNumber(),
				Math.min(pageable.getPageSize(), maxSize)
		);
		Page<User> usersPage = this.userRepository.findByFirstName(firstName, noSortPageable);
		return usersPage.map(UserMapper::mapToSimpleDto);
	}

	@PatchMapping("/{id}/lfg")
	@Transactional
	public ResponseEntity<?> modifyLFG(@PathVariable Long id, @RequestParam String lfg) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (lfg.equals("none")) {
			user.setLfg(lfg);
			userRepository.save(user);
			return ResponseEntity.noContent().build();
		}
		Set<String> eligibleProjects= user.getEligibleProjects();
		boolean validateLFG = eligibleProjects.stream().anyMatch(s -> s.equals(lfg));
		if (validateLFG) {
			user.setLfg(lfg);
			userRepository.save(user);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(400).body("Invalid lfg project. Only eligible projects or none are accepted.");
	}
}