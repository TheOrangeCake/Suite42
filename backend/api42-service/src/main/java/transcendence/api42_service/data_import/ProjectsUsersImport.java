package transcendence.api42_service.data_import;

import lombok.AllArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transcendence.api42_service.data_import.interfaces.EntitySaverInterface;
import transcendence.api42_service.data_import.interfaces.PageFetcherInterface;
import transcendence.api42_service.dto.PageResult;
import transcendence.api42_service.dto.mapper.ProjectsUsersMapper;
import transcendence.api42_service.entity.User;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.repositories.ProjectRepository;
import transcendence.api42_service.repositories.ProjectsUsersRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ProjectsUsersImport {
	private final ProjectsUsersRepository projectsUsersRepository;
	private final ProjectRepository projectRepository;
	private final Api42Fetcher api42Fetcher;
	private final ProjectsUsersMapper projectsUsersMapper;

	@Transactional
	public void initialize(List<User> users, String token, Long lausanneCampusId, String entityName) {
		try (ProgressBar pb = new ProgressBar("Fetching " + entityName, users.size())) {
			for (User user : users) {
				pb.step();
				Long userId = user.getId();
				try {
					initializeUserProjects(user, token, lausanneCampusId, entityName);
				} catch (Exception e) {
					System.err.printf("Failed to initialize projects for user %d: %s%n", userId, e.getMessage());
				}
			}
		}
	}

	@Transactional
	public void initializeUserProjects(User user, String token, Long campusId, String entityName) {
		// Hard coded
		int cursusId = 21;

		initializeDatabase(
				token,
				entityName,
				(t, p, rpp) -> api42Fetcher.fetchProjectsUsersDtoData(t, p, rpp, user.getId(), cursusId, campusId)
						.mapItems(dto -> projectsUsersMapper.mapToProjectsUsers(dto, user, projectRepository)),
				projectsUsersRepository::saveAll
		);
	}


	@SuppressWarnings({"BusyWait", "ReassignedVariable"})
	private <T> void initializeDatabase(
			String token,
			String entityName,
			PageFetcherInterface<T> fetcher,
			EntitySaverInterface<T> saver
	) {
		int page = 1;
		int resultPerPage = 100;

		while (true) {
			try {
				PageResult<T> result = fetcher.fetch(token, page, resultPerPage);
				if (result.items().isEmpty()) break;
				saver.save(result.items());
				if (!result.hasNext()) {
					break;
				}
				page++;
				Thread.sleep(600);
			} catch (ApiCallFailException e) {
				if (e.getStatus().value() == 429) {
					DatabaseImport.handleRateLimit();
				} else if (e.getStatus().value() == 401) {
					System.err.println("Token expired, need a new token. Handle outside this method.");
					break;
				} else {
					DatabaseImport.logAndStop(entityName, e);
					break;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				DatabaseImport.logInterrupted(entityName);
				break;
			}
		}
	}
}