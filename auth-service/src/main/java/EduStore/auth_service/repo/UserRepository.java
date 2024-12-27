package EduStore.auth_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import EduStore.auth_service.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
