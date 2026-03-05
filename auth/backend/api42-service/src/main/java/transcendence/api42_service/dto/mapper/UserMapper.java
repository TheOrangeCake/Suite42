package transcendence.api42_service.dto.mapper;

import tools.jackson.databind.ObjectMapper;
import transcendence.api42_service.definition.project.UserCommonCore;
import transcendence.api42_service.dto.*;
import transcendence.api42_service.entity.Campus;
import transcendence.api42_service.entity.User;
import transcendence.api42_service.exception.NoCommonCoreException;

public final class UserMapper {
	public static UserSimpleResponseDto mapToSimpleDto(User user) {
		if (user == null) {
			return null;
		}
		return new UserSimpleResponseDto(
				user.getId(),
				user.getLogin(),
				user.getFirstName(),
				user.getLastName(),
				user.getIntraUrl(),
				mapAvatar(user),
				user.getRank(),
				user.getRankProgressPercent()
		);
	}

	@SuppressWarnings("ReassignedVariable")
	public static UserDetailedResponseDto mapToDetailedDto(User user) {
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
				mapAvatar(user),
				user.getPoolMonth(),
				user.getPoolYear(),
				user.getAlumni(),
				user.getActive(),
				user.getRank(),
				user.getRankProgressPercent(),
				user.getFinishedProjects(),
				user.getEligibleProjects(),
				userCommonCore
		);
	}

	public static User mapBootStrapRequestToUser(UserRequestDto dto, Campus campus) {
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
		user.setPoolMonth(dto.pool_month());
		user.setPoolYear(dto.pool_year());
		user.setCreatedAt(dto.created_at());
		user.setUpdatedAt(dto.updated_at());
		user.setAlumni(dto.alumni());
		user.setActive(dto.active());
		user.setRank(null);
		user.setRankProgressPercent(null);
		user.setFinishedProjects(null);
		user.setEligibleProjects(null);
		user.setDetailedProfileJson(null);

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

	private static AvatarDto mapAvatar(User user) {
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

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private  static UserCommonCore fromJson(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, UserCommonCore.class);
		} catch (Exception e) {
			throw new NoCommonCoreException();
		}
	}

	private static void setIntraURl(User user, String login) {
		String baseUrl = "https://intra.42.fr/users/";
		user.setIntraUrl(baseUrl + login);
	}
}