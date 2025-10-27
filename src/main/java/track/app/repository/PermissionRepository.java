package track.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import track.app.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}