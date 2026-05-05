package com.hospita.sys.features.patient.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.patient.entity.historyD;

@Repository
public interface historyrepo extends JpaRepository<historyD, Long> {
    // Optional<User> findByEmail(String Email);
}
