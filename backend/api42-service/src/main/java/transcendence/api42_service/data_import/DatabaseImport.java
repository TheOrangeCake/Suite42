package transcendence.api42_service.data_import;

import me.tongfei.progressbar.ProgressBar;
import org.springframework.stereotype.Service;
import transcendence.api42_service.dto.PageResult;
import transcendence.api42_service.exception.ApiCallFailException;
import transcendence.api42_service.data_import.interfaces.EntitySaverInterface;
import transcendence.api42_service.data_import.interfaces.PageFetcherInterface;

@Service
public class DatabaseImport {

	@SuppressWarnings({"BusyWait", "ReassignedVariable"})
	public <T> void initializeDatabase(
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
