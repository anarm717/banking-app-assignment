package az.company.auth.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import az.company.auth.entity.User;
import az.company.auth.repository.UserRepository;

import java.util.Optional;

/**

 The UserJWTService class provides methods to interact with User data stored in a UserRepository.
 This class provides functionality to retrieve User data by their username.
 */

@Service
@RequiredArgsConstructor
public class UserJWTService{

    @Autowired
    UserRepository userRepository;

    /**
     * Returns the User object associated with the provided username.
     *
     * @param username the username associated with the User object.
     * @return an Optional User object containing the User with the provided username, or an empty Optional if not found.
     */
    public Optional<User> getByUsername(@NonNull String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }


}