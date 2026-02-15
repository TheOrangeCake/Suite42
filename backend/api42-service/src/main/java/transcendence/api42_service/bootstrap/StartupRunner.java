package transcendence.api42_service.bootstrap;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import transcendence.api42_service.dto.mapper.CampusMapper;
import transcendence.api42_service.dto.mapper.UserMapper;
import transcendence.api42_service.entities.Campus;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.exception.InvalidTokenException;
import transcendence.api42_service.repositories.CampusRepository;
import transcendence.api42_service.repositories.ProjectsUsersRepository;
import transcendence.api42_service.repositories.UserRepository;
import transcendence.api42_service.data_import.Api42Fetcher;
import transcendence.api42_service.data_import.DatabaseImport;
import transcendence.api42_service.data_import.ProjectsUsersImport;
import transcendence.api42_service.data_import.oauth.OauthTokenGetter;
import transcendence.api42_service.services.UserProgressScoreCalculator;
import transcendence.api42_service.services.UserRankCalculator;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {
    private final Api42Fetcher api42Fetcher;
    private final OauthTokenGetter oauthTokenGetter;
    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final ProjectsUsersRepository projectsUsersRepository;
    private final ProjectsUsersImport projectsUsersImport;
    private final DatabaseImport databaseImport;
    private final UserRankCalculator userRankCalculator;
    private final UserProgressScoreCalculator userProgressScoreCalculator;
    private final UserMapper userMapper;
    private final CampusMapper campusMapper;

    // Hard coded Campus Lausanne id for now due to excess api call per hour for all campus
    @Override
    public void run(String @NonNull ... args) {
        System.out.println("\nHello!");
        System.out.println("Getting Oauth token.");
        try {
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
            campusDbInitialization(token);
        }
        if (userRepository.count() > 0) {
            System.out.println("User database already initialized. Skip API fetch for User.");
        } else {
            userDbInitialization(token);
        }
        if (projectsUsersRepository.count() > 0) {
            System.out.println("ProjectsUsers database already initialized. Skip API fetch for ProjectsUsers.");
        } else {
            projectsUsersDbInitialization(token);
        }
        if (userRepository.count() > 0 && projectsUsersRepository.count() > 0)  {
            userRankCalculator.calculateUserRank();
            userProgressScoreCalculator.calculateUserScore();
        }
        System.out.println("Database initialization completed");
    }

    private void campusDbInitialization(String token) {
        String entityName = "CAMPUSES";
        databaseImport.initializeDatabase(
                token,
                entityName,
                (t, p, rpp) -> api42Fetcher.fetchCampusDtoData(t, p, rpp)
                        .mapItems(campusMapper::mapToCampus),
                campusRepository::saveAll
        );
    }

    private Campus getLausanneCampus() {
        Optional<Campus> lausanneCampusOptional = campusRepository.findByName("Lausanne");
	    return lausanneCampusOptional.orElse(null);
    }

    private void userDbInitialization(String token) {
        String entityName = "USERS";

        // Hardcoded
        Campus lausanneCampus = getLausanneCampus();

        if (lausanneCampus != null) {
            Long lausanneCampusId = lausanneCampus.getId();
            databaseImport.initializeDatabase(
                    token,
                    entityName,
                    (t, p, rpp) -> api42Fetcher.fetchUserDtoData(t, p, rpp, lausanneCampusId)
                            .mapItems(dto -> userMapper.mapBootStrapRequestToUser(dto, lausanneCampus)),
                    userRepository::saveAll
            );
        } else {
            System.out.println("There is no Lausanne campus in database, USERS database will be empty");
        }
    }

    private void projectsUsersDbInitialization(String token) {
        String entityName = "PROJECTS_USERS";

        // Hardcoded
        Campus lausanneCampus = getLausanneCampus();

        if (userRepository.count() > 0 && lausanneCampus != null) {
            Long lausanneCampusId = lausanneCampus.getId();
            List<User> activeUsers = userRepository.findByActiveTrue();
            projectsUsersImport.initialize(activeUsers, token, lausanneCampusId, entityName);
        } else {
            System.out.println("There is no user in the user database, PROJECTS_USERS database will be empty");
        }
    }
}