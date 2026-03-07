package transcendence.api42_service.bootstrap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.exception.InvalidTokenException;
import transcendence.api42_service.repositories.CampusRepository;
import transcendence.api42_service.repositories.ProjectsUsersRepository;
import transcendence.api42_service.repositories.UserRepository;
import transcendence.api42_service.data_import.DatabaseImport;
import transcendence.api42_service.data_import.oauth.OauthTokenGetter;
import transcendence.api42_service.services.UserProgressScoreCalculator;
import transcendence.api42_service.services.UserRankCalculator;
import transcendence.api42_service.services.UserTalentPointCalculator;

@RequiredArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {
    private final OauthTokenGetter oauthTokenGetter;
    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final ProjectsUsersRepository projectsUsersRepository;
    private final DatabaseImport databaseImport;
    private final UserRankCalculator userRankCalculator;
    private final UserProgressScoreCalculator userProgressScoreCalculator;
    private final UserTalentPointCalculator userTalentPointCalculator;

    @Getter
    private boolean startupComplete = false;

    // Hard coded Campus Lausanne id for now due to excess api call per hour for all campus
    @Override
    public void run(String @NonNull ... args) {
        System.out.println("\nHello!");
        try {
            System.out.println("Getting Oauth token.");
            oauthTokenGetter.retrieveToken();
        } catch (ApiCallFailException e) {
            System.err.println("API error during Oauth Token request: " + e.getMessage());
            return;
        } catch (InvalidTokenException e) {
            System.err.println("Invalid Oauth token for api call, check uid, secret or token refresh");
            return;
        }
        String token = oauthTokenGetter.getToken();
        if (campusRepository.count() > 0) {
            System.out.println("Campus database already initialized. Skip API fetch for Campus.");
        } else {
            databaseImport.campusDbPopulate(token);
        }
        if (userRepository.count() > 0) {
            System.out.println("User database already initialized. Skip API fetch for User.");
        } else {
            databaseImport.userDbPopulate(token);
        }
        if (projectsUsersRepository.count() > 0) {
            System.out.println("ProjectsUsers database already initialized. Skip API fetch for ProjectsUsers.");
        } else {
            databaseImport.projectsUsersDbPopulate(token);
        }
        if (userRepository.count() > 0 && projectsUsersRepository.count() > 0)  {
            userRankCalculator.calculateUserRank();
            userProgressScoreCalculator.calculateUserScore();
            userTalentPointCalculator.calculateUserTalentPoint();
        }
        this.startupComplete = true;
        System.out.println("Database initialization completed");
    }
}