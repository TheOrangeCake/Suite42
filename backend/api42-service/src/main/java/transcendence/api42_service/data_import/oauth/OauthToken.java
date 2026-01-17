package transcendence.api42_service.data_import.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
public final class OauthToken {
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expiresIn")
	private String expiresIn;

	@JsonProperty("created_at")
	private String createdAt;
}