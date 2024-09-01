package infootball.user.application;


import infootball.user.domain.DTO.UserTeamsCompetitionsDTO;
import infootball.user.domain.model.User;
import infootball.user.domain.exceptions.UserException;
import infootball.user.infrastructure.outbound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User createUser(User newUser) throws UserException {
        try{
            String encodedPassword = this.passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
            return this.userRepository.save(newUser);
        } catch (RuntimeException e){
            throw new UserException(e.getMessage());
        }

    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }


    public User updateTeamsOfUser(Long id, UserTeamsCompetitionsDTO newUserInfo) throws UserException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new UserException("this user doesn't exist");
        }
        User oldUser  = userOptional.get();
        oldUser.setTeams(newUserInfo.getTeams());
        oldUser.setCompetitions(newUserInfo.getCompetitions());
        return oldUser;
    }


}
