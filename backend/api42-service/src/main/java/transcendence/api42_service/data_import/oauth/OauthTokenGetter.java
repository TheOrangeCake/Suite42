package transcendence.api42_service.data_import.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.exception.InvalidTokenException;

@Getter
@Service
public class OauthTokenGetter {
    private volatile String token;
    private final RestClient restClient;

    @Value("${api42.secret}")
    private String apiSecret;

    @Value("${api42.uid}")
    private String apiUID;

    public OauthTokenGetter(RestClient restClient) {
        this.restClient = restClient;
    }

    public void retrieveToken() {
        OauthToken result = restClient.post()
                .uri("oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("grant_type=client_credentials" +
                        "&client_id=" + apiUID +
                        "&client_secret=" + apiSecret)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new ApiCallFailException(response.getStatusCode());
                }))
                .body(OauthToken.class);
        if (result == null || result.getAccessToken() == null) {
            throw new InvalidTokenException();
        }
        token = result.getAccessToken();
    }
}