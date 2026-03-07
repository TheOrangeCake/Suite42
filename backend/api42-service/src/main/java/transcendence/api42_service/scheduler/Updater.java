package transcendence.api42_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import transcendence.api42_service.bootstrap.StartupRunner;
import transcendence.api42_service.data_import.DatabaseImport;
import transcendence.api42_service.data_import.oauth.OauthTokenGetter;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.exception.InvalidTokenException;
import transcendence.api42_service.repositories.ProjectsUsersRepository;
import transcendence.api42_service.repositories.UserRepository;
import transcendence.api42_service.services.UserProgressScoreCalculator;
import transcendence.api42_service.services.UserRankCalculator;
import transcendence.api42_service.services.UserTalentPointCalculator;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Updater {
    private final OauthTokenGetter oauthTokenGetter;
    private final DatabaseImport databaseImport;
    private final UserRepository userRepository;
    private final ProjectsUsersRepository projectsUsersRepository;
    private final UserRankCalculator userRankCalculator;
    private final UserProgressScoreCalculator userProgressScoreCalculator;
    private final UserTalentPointCalculator userTalentPointCalculator;
    private final StartupRunner startupRunner;

//    @Scheduled(initialDelay = 20 * 60 * 1000, fixedDelay = 20 * 60 * 1000)
    @Scheduled(cron = "0 0 2 * * ?")
    public void updater() {
        if (!startupRunner.isStartupComplete()) {
            return;
        }
        System.out.println("\nStart updating database");
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
        databaseImport.campusDbPopulate(token);
        databaseImport.userDbPopulate(token);
        databaseImport.projectsUsersDbPopulate(token);
        if (userRepository.count() > 0 && projectsUsersRepository.count() > 0)  {
            userRankCalculator.calculateUserRank();
            userProgressScoreCalculator.calculateUserScore();
            userTalentPointCalculator.calculateUserTalentPoint();
        }
        System.out.println("Database update completed");
    }
}