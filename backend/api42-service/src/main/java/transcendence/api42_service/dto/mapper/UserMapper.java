package transcendence.api42_service.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import transcendence.api42_service.definition.project.UserCommonCore;
import transcendence.api42_service.dto.*;
import transcendence.api42_service.entities.Campus;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.exception.NoCommonCoreException;

@RequiredArgsConstructor
@Component
public final class UserMapper {
	private final EnvVariables envVariables;

	public UserSimpleResponseDto mapToSimpleDto(User user) {
		if (user == null) {
			return null;
		}
		return new UserSimpleResponseDto(
				user.getId(),
				user.getLogin(),
				user.getFirstName(),
				user.getLastName(),
				user.getIntraUrl(),
				createCustomAvatarUrl(user),
				mapAvatar(user),
				user.getPoolMonth(),
				user.getPoolYear(),
				user.getRank(),
				user.getRankProgressPercent(),
				user.getPerformanceScore(),
				user.getLfg()
		);
	}

	@SuppressWarnings("ReassignedVariable")
	public UserDetailedResponseDto mapToDetailedDto(User user) {
		if (user == null) {
			return null;
		}
		UserCommonCore userCommonCore = null;
		try {
			userCommonCore = fromJson(user.getDetailedProfileJson());
		} catch (NoCommonCoreException e) {
			System.err.println("Invalid UserCommonCore JSON. " + e);
		}
		return new UserDetailedResponseDto(
				user.getId(),
				user.getCampus().getName(),
				user.getEmail(),
				user.getLogin(),
				user.getFirstName(),
				user.getLastName(),
				user.getIntraUrl(),
				createCustomAvatarUrl(user),
				createCustomBannerUrl(user),
				mapAvatar(user),
				user.getPoolMonth(),
				user.getPoolYear(),
				user.getAlumni(),
				user.getActive(),
				user.getRank(),
				user.getRankProgressPercent(),
				user.getPerformanceScore(),
				user.getLfg(),
				user.getFinishedProjects(),
				user.getEligibleProjects(),
				userCommonCore
		);
	}

	public User mapBootStrapRequestToUser(UserRequestDto dto, Campus campus) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setId(dto.id());
		user.setCampus(campus);
		user.setEmail(dto.email());
		user.setLogin(dto.login());
		user.setFirstName(dto.first_name());
		user.setLastName(dto.last_name());
		setIntraURl(user, dto.login());
		user.setCustomAvatarUrl(null);
		user.setCustomBannerUrl(envVariables.getDefaultBanner());
		user.setPoolMonth(dto.pool_month());
		user.setPoolYear(dto.pool_year());
		user.setCreatedAt(dto.created_at());
		user.setUpdatedAt(dto.updated_at());
		user.setAlumni(dto.alumni());
		user.setActive(dto.active());
		user.setRank(null);
		user.setFreezeDays(null);
		user.setRankProgressPercent(null);
		user.setPerformanceScore(null);
		user.setFinishedProjects(null);
		user.setEligibleProjects(null);
		user.setDetailedProfileJson(null);
		user.setPoolResult(null);

		if (dto.image() != null) {
			user.setImageLink(dto.image().link());

			if (dto.image().versions() != null) {
				user.setImageLarge(dto.image().versions().large());
				user.setImageMedium(dto.image().versions().medium());
				user.setImageSmall(dto.image().versions().small());
				user.setImageMicro(dto.image().versions().micro());
			}
		}

		return user;
	}

	private AvatarDto mapAvatar(User user) {
		AvatarVersionsDto versions = new AvatarVersionsDto(
				user.getImageLarge(),
				user.getImageMedium(),
				user.getImageSmall(),
				user.getImageMicro()
		);
		return new AvatarDto(
				user.getImageLink(),
				versions
		);
	}

	private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private UserCommonCore fromJson(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, UserCommonCore.class);
		} catch (Exception e) {
			throw new NoCommonCoreException();
		}
	}

	private void setIntraURl(User user, String login) {
		String baseUrl = "https://intra.42.fr/users/";
		user.setIntraUrl(baseUrl + login);
	}

	private String createCustomBannerUrl(User user) {
		return envVariables.getImageDomain() + user.getCustomBannerUrl();
	}

	private String createCustomAvatarUrl(User user) {
		String customAvatar = user.getCustomAvatarUrl();
		return customAvatar != null ? envVariables.getImageDomain() + customAvatar : null;
	}
}