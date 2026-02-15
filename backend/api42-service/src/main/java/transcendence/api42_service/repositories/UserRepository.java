package transcendence.api42_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import transcendence.api42_service.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	List<User> findByActiveTrue();
	Page<User> findByLastName(String lastName, Pageable pageable);
	Page<User> findByFirstName(String firstName, Pageable pageable);
	@Query("""
	SELECT u FROM User u
	LEFT JOIN FETCH u.campus
	LEFT JOIN FETCH u.finishedProjects
	LEFT JOIN FETCH u.eligibleProjects
	WHERE u.id = :id
	""")
	Optional<User> findDetailedById(Long id);
}