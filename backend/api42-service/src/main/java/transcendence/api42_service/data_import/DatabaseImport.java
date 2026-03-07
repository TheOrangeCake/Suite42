package transcendence.api42_service.data_import;

import lombok.RequiredArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.stereotype.Service;
import transcendence.api42_service.dto.PageResult;
import transcendence.api42_service.dto.mapper.CampusMapper;
import transcendence.api42_service.dto.mapper.UserMapper;
import transcendence.api42_service.entities.Campus;
import transcendence.api42_service.entities.User;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.data_import.interfaces.EntitySaverInterface;
import transcendence.api42_service.data_import.interfaces.PageFetcherInterface;
import transcendence.api42_service.repositories.CampusRepository;
import transcendence.api42_service.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DatabaseImport {
	private final Api42Fetcher api42Fetcher;
	private final CampusMapper campusMapper;
	private final CampusRepository campusRepository;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final ProjectsUsersImport projectsUsersImport;

	public void campusDbPopulate(String token) {
		String entityName = "CAMPUSES";
		populateDatabase(
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

	public void userDbPopulate(String token) {
		String entityName = "USERS";

		// Hardcoded
		Campus lausanneCampus = getLausanneCampus();

		if (lausanneCampus != null) {
			Long lausanneCampusId = lausanneCampus.getId();
			populateDatabase(
					token,
					entityName,
					(t, p, rpp) -> api42Fetcher.fetchUserDtoData(t, p, rpp, lausanneCampusId)
							.mapItems(dto -> userMapper.mapRequestToUser(dto, lausanneCampus)),
					userRepository::saveAll
			);
		} else {
			System.out.println("There is no Lausanne campus in database, USERS database will be empty");
		}
	}

	public void projectsUsersDbPopulate(String token) {
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

	@SuppressWarnings({"BusyWait", "ReassignedVariable"})
	public <T> void populateDatabase(
			String token,
			String entityName,
			PageFetcherInterface<T> fetcher,
			EntitySaverInterface<T> saver
	) {
		int page = 1;
		int resultPerPage = 100;

		try (ProgressBar pb = new ProgressBar("Fetching " + entityName, -1)) {
			while (true) {
				pb.step();
				try {
					PageResult<T> result = fetcher.fetch(token, page, resultPerPage);
					if (result.items().isEmpty()) break;
					saver.save(result.items());
					pb.maxHint((int) Math.ceil((double) result.xTotal() / resultPerPage));
					if (!result.hasNext()) {
						break;
					}
					page++;
					Thread.sleep(600);
				} catch (ApiCallFailException e) {
					if (e.getStatus().value() == 429) {
						handleRateLimit();
					} else if (e.getStatus().value() == 401) {
						System.err.println("Token expired, need a new token. Handle outside this method.");
						break;
					} else {
						logAndStop(entityName, e);
						break;
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logInterrupted(entityName);
					break;
				}
			}
		}
	}

	public static void handleRateLimit() {
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Sleep interrupted, stopping initialization.");
		}
	}

	public static void logAndStop(String entityName, ApiCallFailException e) {
		System.err.printf("API error while initializing %s: %s. Stopping initialization.%n", entityName, e.getMessage());
	}

	public static void logInterrupted(String entityName) {
		System.err.printf("Initialization of %s interrupted. Stopping.%n", entityName);
	}
}
