package track.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import track.app.model.PendingUser;

import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {

    Optional<PendingUser> findByToken(String token);
}