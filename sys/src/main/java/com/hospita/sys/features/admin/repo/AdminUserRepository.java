package com.hospita.sys.features.admin.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.auth.entity.User;
import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
    List<User> findByRoleIgnoreCaseAndAuth(String role, boolean auth);
}
