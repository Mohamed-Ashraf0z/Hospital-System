package com.hospita.sys.features.admin.service;

import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PermissionService {

    private final AuthRepo authRepo;

    public PermissionService(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public boolean grantPermission(Long userId, String permission) {
        Optional<User> userOpt = authRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getPermissions().add(permission);
            authRepo.save(user);
            return true;
        }
        return false;
    }

    public boolean revokePermission(Long userId, String permission) {
        Optional<User> userOpt = authRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPermissions().contains(permission)) {
                user.getPermissions().remove(permission);
                authRepo.save(user);
                return true;
            }
        }
        return false;
    }

    public Set<String> getUserPermissions(Long userId) {
        Optional<User> userOpt = authRepo.findById(userId);
        return userOpt.map(User::getPermissions).orElse(null);
    }

    public Set<String> getAllSystemPermissions() {
        return Set.of(
            // Doctor Permissions
            "UPDATE_BIO",
            "ADD_AVAILABILITY",
            "GET_AVAILABILITY",
            "GET_APPOINTED_PATIENTS",
            "SEND_REPORT",
            
            // Patient Permissions
            "GET_DOCTORS",
            "GET_DOCTOR",
            "GET_HISTORY",
            "GET_MY_APPOINTMENTS",
            "MAKE_APPOINTMENT",
            "CANCEL_APPOINTMENT",
            "MAKE_HISTORY"
        );
    }

    public boolean updatePermissions(Long userId, Set<String> permissions) {
        Optional<User> userOpt = authRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getPermissions().clear();
            if (permissions != null) {
                user.getPermissions().addAll(permissions);
            }
            authRepo.save(user);
            return true;
        }
        return false;
    }
}
