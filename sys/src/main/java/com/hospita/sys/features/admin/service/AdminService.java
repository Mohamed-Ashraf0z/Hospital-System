package com.hospita.sys.features.admin.service;

import com.hospita.sys.features.admin.dto.UserDto;
import com.hospita.sys.features.admin.repo.AdminUserRepository;
import com.hospita.sys.features.auth.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminUserRepository userRepository;

    public AdminService(AdminUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto freezeUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFrozen(true);
            userRepository.save(user);
            return new UserDto(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }
}
