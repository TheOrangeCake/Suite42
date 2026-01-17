package transcendence.api42_service.dto;

public record UserSimpleResponseDto(
		Long id,
		String login,
		String first_name,
		String last_name,
		String intra_url,
		AvatarDto image,
		Integer rank,
		Integer rank_progress_percent
) {
}