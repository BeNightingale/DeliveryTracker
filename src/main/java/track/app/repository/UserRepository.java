package track.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import track.app.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
}
