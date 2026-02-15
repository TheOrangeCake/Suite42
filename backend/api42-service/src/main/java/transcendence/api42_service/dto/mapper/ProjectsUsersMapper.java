package transcendence.api42_service.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import transcendence.api42_service.definition.curriculum.CommonCoreCurriculum;
import transcendence.api42_service.dto.ProjectsUsersRequestDto;
import transcendence.api42_service.dto.ProjectsUsersResponseDto;
import transcendence.api42_service.entities.Project;
import transcendence.api42_service.entities.ProjectsUsers;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.repositories.ProjectRepository;

import java.util.Optional;

@AllArgsConstructor
@Component
public final class ProjectsUsersMapper {
	private final ProjectMapper projectMapper;

	public ProjectsUsersResponseDto mapToProjectsUsersResponseDto(ProjectsUsers projectsUsers) {
		if (projectsUsers == null) {
			return null;
		}
		return new ProjectsUsersResponseDto(
				projectsUsers.getId(),
				projectsUsers.getOccurrence(),
				projectsUsers.getFinalMark(),
				projectsUsers.getStatus(),
				projectsUsers.getValidated(),
				projectsUsers.getMarkedAt(),
				projectMapper.mapToResponseDto(projectsUsers.getProject())
		);
	}

	// TODO: move Project repo save() outside of mapper for clearer role separation
	public ProjectsUsers mapToProjectsUsers(
			ProjectsUsersRequestDto projectsUsersDto,
			User user,
			ProjectRepository projectRepository) {
		if (projectsUsersDto == null) {
			return null;
		}
		ProjectsUsers projectsUsers = new ProjectsUsers();
		projectsUsers.setId(projectsUsersDto.id());
		projectsUsers.setOccurrence(projectsUsersDto.occurrence());
		projectsUsers.setFinalMark(projectsUsersDto.final_mark());
		projectsUsers.setStatus(projectsUsersDto.status());
		projectsUsers.setValidated(projectsUsersDto.validated());
		projectsUsers.setMarkedAt(projectsUsersDto.marked_at());
		projectsUsers.setUser(user);

		Optional<Project> projectOptional = projectRepository.findById(projectsUsersDto.project().id());
		Project project;
		if (projectOptional.isPresent()) {
			project = projectOptional.get();
		} else {
			project = new Project();
			project.setId(projectsUsersDto.project().id());
			project.setName(projectsUsersDto.project().name());
			project.setSlug(projectsUsersDto.project().slug());
			project.setRank(CommonCoreCurriculum.getProjectRank(projectsUsersDto.project().slug()));
			projectRepository.save(project);
		}
		projectsUsers.setProject(project);

		return projectsUsers;
	}
}