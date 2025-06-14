package com.springsecurity.service;

import com.springsecurity.exception.UserNotFoundException;
import com.springsecurity.model.User;
import com.springsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JWTService jwtService;


    @Override
    public Long save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public User update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("User not found with ID: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(null);
                    return user;
                });
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .peek(user -> user.setPassword(null))
                .toList();
    }

    @Override
    public Page<User> findAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> {
                    user.setPassword(null);
                    return user;
                });
    }

    @Override
    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        return authentication.isAuthenticated() ?
                jwtService.generateToken(user.getUsername()) :
                "Invalid username or password";
    }

}
