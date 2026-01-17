package transcendence.api42_service.dto.mapper;

import transcendence.api42_service.dto.ProjectResponseDto;
import transcendence.api42_service.entity.Project;

public final class ProjectMapper {
	public static ProjectResponseDto mapToResponseDto(Project project) {
		return new ProjectResponseDto(
				project.getId(),
				project.getName(),
				project.getSlug(),
				project.getRank()
		);
	}
}