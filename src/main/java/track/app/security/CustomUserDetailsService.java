package track.app.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import track.app.model.User;
import track.app.repository.UserRepository;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        final User user = userRepository
                .findByUserName(userName)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "W bazie danych nie znaleziono użytkownika o nazwie: %s.".formatted(userName)
                        )
                );
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getEnabled(),
                true, true, true,
                user.getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getRole()))
                        .toList()
        );
    }
}