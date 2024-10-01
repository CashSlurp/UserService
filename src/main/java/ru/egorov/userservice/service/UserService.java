package ru.egorov.userservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egorov.userservice.dto.UserDTO;
import ru.egorov.userservice.entity.User;
import ru.egorov.userservice.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(UserDTO dto) {
        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .confirmPassword(dto.getConfirmPassword())
                .build());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found. ID: " + id));
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
