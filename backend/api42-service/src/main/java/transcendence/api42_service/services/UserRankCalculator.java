package transcendence.api42_service.services;

import lombok.AllArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import transcendence.api42_service.definition.curriculum.CommonCoreCurriculum;
import transcendence.api42_service.definition.curriculum.RankDefinition;
import transcendence.api42_service.definition.project.IndividualProject;
import transcendence.api42_service.definition.project.IndividualRank;
import transcendence.api42_service.definition.project.LastProjectsUsers;
import transcendence.api42_service.definition.project.UserCommonCore;
import transcendence.api42_service.entities.ProjectsUsers;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.repositories.ProjectsUsersRepository;
import transcendence.api42_service.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class UserRankCalculator {
	private final UserRepository userRepository;
	private final ProjectsUsersRepository projectsUsersRepository;
	private final ObjectMapper objectMapper;

	public void calculateUserRank() {
		List<User> activeUsers= userRepository.findByActiveTrue();
		List<ProjectsUsers> activeUsersProjectsUsers = projectsUsersRepository.findByUserIn(activeUsers);
		int totalActiveUser = activeUsers.size();

		try (ProgressBar pb = new ProgressBar("Updating user data", totalActiveUser)) {
			for (User user : activeUsers) {
				pb.step();
				List<ProjectsUsers> usersProjectsUsers = activeUsersProjectsUsers
						.stream()
						.filter(pu -> pu.getUser().equals(user))
						.toList();
				Map<String, ProjectsUsers> lastedProjectsUsersBySlug = usersProjectsUsers.stream()
						.collect(Collectors.toMap(
								projectsUsers -> projectsUsers.getProject().getSlug(),
								projectsUsers -> projectsUsers,
								(a, b) -> a.getMarkedAt().isAfter(b.getMarkedAt()) ? a : b));
				UserCommonCore userCommonCore = calculateUserCommonCore(user, lastedProjectsUsersBySlug);
				try {
					user.setDetailedProfileJson(toJson(userCommonCore));
				} catch (RuntimeException e) {
					System.err.printf("Failed to serialize UserCommonCore and calculate rank for user %s. %s", user.getId().toString(), e);
					user.setDetailedProfileJson(null);
				}
			}
		}
		userRepository.saveAll(activeUsers);
	}

	private IndividualProject setIndividualProject(ProjectsUsers projectsUsers) {
		LastProjectsUsers lastProjectsUsers = new LastProjectsUsers(
				projectsUsers.getOccurrence(),
				projectsUsers.getFinalMark(),
				projectsUsers.getStatus(),
				projectsUsers.getValidated());
		return new IndividualProject(
				projectsUsers.getProject().getId(),
				projectsUsers.getProject().getName(),
				projectsUsers.getProject().getSlug(),
				lastProjectsUsers);
	}

	@SuppressWarnings("ReassignedVariable")
	public UserCommonCore calculateUserCommonCore(User user, Map<String, ProjectsUsers> lastedProjectsUsersBySlug) {
		Integer currentRank = 0;
		List<IndividualRank> ranks = new ArrayList<>();
		Set<String> finishedProjects = new HashSet<>();

		for (Map.Entry<Integer, RankDefinition> entry : CommonCoreCurriculum.RANKS.entrySet()) {
			boolean rankCompleted = true;
			List<IndividualProject> projects = new ArrayList<>();
			for (String projectSlug : entry.getValue().mandatory()) {
				ProjectsUsers projectsUsers = lastedProjectsUsersBySlug.get(projectSlug);
				if (projectsUsers != null) {
					IndividualProject project = setIndividualProject(projectsUsers);
					projects.add(project);
					if (isProjectFinished(project)) {
						finishedProjects.add(projectSlug);
					} else {
						rankCompleted = false;
					}
				} else {
					rankCompleted = false;
				}
			}
			for (Set<String> choiceGroups : entry.getValue().choices()) {
				boolean choicesCompleted = false;
				for (String projectSlug : choiceGroups) {
					ProjectsUsers projectsUsers = lastedProjectsUsersBySlug.get(projectSlug);
					if (projectsUsers != null) {
						IndividualProject project = setIndividualProject(projectsUsers);
						projects.add(project);
						if (isProjectFinished(project)) {
							choicesCompleted = true;
							finishedProjects.add(projectSlug);
						}
					}
				}
				if (!choicesCompleted) {
					rankCompleted = false;
				}
			}
			IndividualRank rank = new IndividualRank(entry.getKey(), projects);
			ranks.add(rank);
			if (!rankCompleted) {
				break;
			} else {
				currentRank++;
			}
		}
		user.setRank(currentRank);
		user.setFinishedProjects(finishedProjects);
		user.setEligibleProjects(eligibleProjects(currentRank, finishedProjects));
		return new UserCommonCore(ranks);
	}

	private boolean isProjectFinished(IndividualProject project) {
		Boolean validated = project.lastProjectsUsers().validated();
		return validated != null && validated;
	}

	private Set<String> eligibleProjects(Integer userRank, Set<String> finishedProjects) {
		Set<String> eligibleProjects = new HashSet<>();
		RankDefinition current = CommonCoreCurriculum.getRank(userRank);
		RankDefinition next = CommonCoreCurriculum.getRank(userRank + 1);

		if (current != null) {
			for (String projectSlug : current.mandatory()) {
				if (!finishedProjects.contains(projectSlug)) {
					eligibleProjects.add(projectSlug);
				}
			}
			for (Set<String> choiceGroup : current.choices()) {
				boolean anyFinished = choiceGroup.stream().anyMatch(finishedProjects::contains);
				if (anyFinished) {
					eligibleProjects.addAll(choiceGroup);
				}
			}
		}
		if (next != null) {
			eligibleProjects.addAll(next.mandatory());
			for (Set<String> choiceGroup : next.choices()) {
				eligibleProjects.addAll(choiceGroup);
			}
		}
		return eligibleProjects;
	}

	private String toJson(UserCommonCore userCommonCore) {
		try {
			return objectMapper.writeValueAsString(userCommonCore);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}