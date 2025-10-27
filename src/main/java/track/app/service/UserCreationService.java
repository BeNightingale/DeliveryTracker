package track.app.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import track.app.model.PendingUser;
import track.app.model.Permission;
import track.app.model.User;
import track.app.model.dto.UserDto;
import track.app.repository.PendingUserRepository;
import track.app.repository.PermissionRepository;
import track.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter
@Slf4j
public class UserCreationService {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PendingUserRepository pendingUserRepository;
    private final SendingMailService sendingMailService;

    public PendingUser createPendingUser(UserDto userDto) {
        final String userName = userDto.getUserName();
        final String email = userDto.getEmail();
        final String password = userDto.getPassword();
        // validate userName - chars, length, already exists?
        // validate password - chars, length
        // validate email string -> chars, format, length
        log.info("[START] Creating pendingUser: name {}, email {}.", userName, email);
        final String passwordHash = new BCryptPasswordEncoder().encode(password);
        log.error("Password hash for pendingUser is {}", passwordHash);
        final LocalDateTime now = LocalDateTime.now();
        final UUID uuid = UUID.randomUUID();
        final PendingUser pendingUser = new PendingUser(
                null, userName, email, passwordHash, now, uuid.toString(), now.plusDays(1), false
        );
        final PendingUser savedPendingUser = pendingUserRepository.save(pendingUser);
        log.info("[END] Created pendingUser: id {}, name {}, email {}, creationTime {}.",
                savedPendingUser.getPendingUserId(), userName, email, now
        );
        sendingMailService.send(email, uuid.toString());
        return savedPendingUser;
    }

    public void createUser(String token) {
        if (StringUtils.isEmpty(token)) {
            log.error("No token!");
            throw new IllegalArgumentException("Empty token!");
        }
        final PendingUser pendingUser = pendingUserRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalStateException("PendingUser with token %s dos not exist!".formatted(token)));
        final LocalDateTime to = pendingUser.getTokenExpirationDate();
        if (to.isBefore(LocalDateTime.now())) {
            log.error("Deleting pendingUser data for user: pendingUserId = {}, userName = {}.", pendingUser.getPendingUserId(), pendingUser.getUserName());
            pendingUserRepository.delete(pendingUser);
            throw new IllegalStateException("Token is expired! Account for user %s will not be created.".formatted(pendingUser.getUserName()));
        }
        final String userName = pendingUser.getUserName();
        final String email = pendingUser.getEmail();
        log.error("[START] Creating user: name {}, email {}.", userName, email);
        log.error("User password hash: {}", pendingUser.getPassword());
        final User user = new User(
                null, userName, email, pendingUser.getPassword(), true, new ArrayList<>()
        );
        final User savedUser = userRepository.save(user);
        log.error("User password hash after save: {}", savedUser.getPassword());
        final int userId = savedUser.getUserId();
        log.error("New user saved in database: id {}, name {}, email {}.", userId, userName, email);
        log.error("Creating permission for a new user (id = {}).", userId);
        final Permission permission = Permission.createNewPermissionAndAddPermissionToUser(savedUser, "ROLE_USER");
        final Permission savedPermission = permissionRepository.save(permission);
        log.error("Created permission with permissionId {} and role: 'ROLE_USER' for a new user (userId {}).",
                savedPermission.getPermissionId(), userId
        );
        log.info("Deleting pendingUser: id ={}, name ={}.", pendingUser.getPendingUserId(), pendingUser.getUserName());
        pendingUserRepository.delete(pendingUser);
        log.error("[END] User created: id = {}, name = {}, email = {}, .", userId, userName, email);
    }
}