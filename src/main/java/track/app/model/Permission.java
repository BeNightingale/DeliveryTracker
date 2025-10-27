package track.app.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false, updatable = false)
    private Integer permissionId;

    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;

    @Column(name = "permission", nullable = false, length = 50)
    private String role;

    public static Permission createNewPermissionAndAddPermissionToUser(User user, String role) {
        Permission permission = new Permission(null, user, role);
        user.addPermission(permission);
        return permission;
    }
}
