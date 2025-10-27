package track.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "pending_users")
public class PendingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_user_id")
    private Integer pendingUserId;

    @Column(name = "user_name", unique = true, nullable = false, length = 50) // login
    private String userName;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 100)//hash
    private String password;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate; // default = now

    @Column(name = "token", unique = true, nullable = false, length = 36)// uuid format
    private String token;

    @Column(name = "token_expiration_date", nullable = false)
    private LocalDateTime tokenExpirationDate;

    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;// default = false
}