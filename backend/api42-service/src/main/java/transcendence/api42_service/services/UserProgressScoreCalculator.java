package transcendence.api42_service.services;

import lombok.AllArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transcendence.api42_service.definition.curriculum.CommonCoreCurriculum;
import transcendence.api42_service.definition.curriculum.RankDefinition;
import transcendence.api42_service.entity.User;
import transcendence.api42_service.repositories.UserRepository;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Transactional
@Service
public class UserProgressScoreCalculator {
	private final UserRepository userRepository;

	@SuppressWarnings("ReassignedVariable")
	public void calculateUserScore() {
		List<User> activeUsers = userRepository.findByActiveTrue();
		int totalActiveUsers = activeUsers.size();
		try (ProgressBar pb = new ProgressBar("Calculating user current rank progress", totalActiveUsers)) {
			for (User user : activeUsers) {
				pb.step();
				int rankProgress = 0;
				Set<String> finishedProjects= user.getFinishedProjects();
				RankDefinition current = CommonCoreCurriculum.getRank(user.getRank());
				if (current != null) {
					int currentRankProjectsCount = current.mandatory().size() + current.choices().size();
					if (currentRankProjectsCount == 0) {
						user.setRankProgressPercent(rankProgress);
						break;
					}
					int currentRankFinishedCount = 0;
					for (String projectSlug : finishedProjects) {
						if (current.mandatory().contains(projectSlug)) {
							currentRankFinishedCount++;
							continue;
						}
						for (Set<String> choiceGroup : current.choices()) {
							if (choiceGroup.contains(projectSlug)) {
								currentRankFinishedCount++;
								break;
							}
						}
					}
					rankProgress = (int) Math.ceil((double)currentRankFinishedCount / currentRankProjectsCount * 100);
				}

				user.setRankProgressPercent(rankProgress);
			}
		}
		userRepository.saveAll(activeUsers);
	}
}