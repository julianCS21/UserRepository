package infootball.user.application;

import infootball.user.domain.model.User;
import infootball.user.infrastructure.outbound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userInDB = userRepository.findByEmail(email);
        if (userInDB.isEmpty()) {
            throw new UsernameNotFoundException("This user doesn't exist");
        }
        User loadUser = userInDB.get();




        return org.springframework.security.core.userdetails.User.builder()
                .username(loadUser.getEmail())
                .password(loadUser.getPassword())
                .build();
    }
}
