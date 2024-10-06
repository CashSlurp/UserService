package ru.egorov.userservice.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.egorov.userservice.dto.UserDTO;
import ru.egorov.userservice.entity.User;
import ru.egorov.userservice.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public User create(UserDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setConfirmPassword(null);
        kafkaTemplate.send("user-creation", dto.getUsername());
        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .confirmPassword(dto.getConfirmPassword())
                .build());
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            System.out.println(passwordEncoder.encode(password));
            return true;
        }
        else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found. ID: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
